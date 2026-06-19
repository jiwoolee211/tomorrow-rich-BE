package com.likelion.tomorrowrich.home.service;

import com.likelion.tomorrowrich.expense.entity.Expense;
import com.likelion.tomorrowrich.expense.repository.ExpenseRepository;
import com.likelion.tomorrowrich.global.exception.BusinessException;
import com.likelion.tomorrowrich.global.exception.ErrorCode;
import com.likelion.tomorrowrich.home.dto.BudgetStatus;
import com.likelion.tomorrowrich.home.dto.HomeResponseDTO;
import com.likelion.tomorrowrich.mission.entity.Mission;
import com.likelion.tomorrowrich.mission.entity.MissionStatus;
import com.likelion.tomorrowrich.mission.repository.MissionRepository;
import com.likelion.tomorrowrich.room.entity.Room;
import com.likelion.tomorrowrich.room.repository.RoomRepository;
import com.likelion.tomorrowrich.user.entity.User;
import com.likelion.tomorrowrich.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class HomeService {

    private static final String DEFAULT_NICKNAME = "사용자";
    private static final String DEFAULT_CHARACTER_NAME = "말랑이";
    private static final String DEFAULT_ROOM_IMAGE_URL = "/rooms/default-room.png";

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final MissionRepository missionRepository;
    private final RoomRepository roomRepository;

    public HomeService(
            UserRepository userRepository,
            ExpenseRepository expenseRepository,
            MissionRepository missionRepository,
            RoomRepository roomRepository
    ) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.missionRepository = missionRepository;
        this.roomRepository = roomRepository;
    }

    public HomeResponseDTO getHome(Long userId) {
        User user = findUser(userId);

        Integer monthlyBudget = defaultValue(user.getMonthlyBudget());
        Integer monthlySavingGoal = defaultValue(user.getMonthlySavingGoal());
        /*
         * TODO:
         * 추후 포인트 도메인이 별도 정책을 가지게 되면 PointService 또는 PointRepository를 통해 조회하도록 변경해야 함.
         */
        Integer point = defaultValue(user.getCurrentPoint());

        Integer monthlyExpenseAmount = calculateMonthlyExpenseAmount(user);
        Integer remainingBudget = calculateRemainingBudget(monthlyBudget, monthlyExpenseAmount);
        Integer todayAvailableAmount = calculateTodayAvailableAmount(remainingBudget);

        BudgetStatus budgetStatus = calculateBudgetStatus(
                monthlyBudget,
                remainingBudget,
                todayAvailableAmount
        );

        List<HomeResponseDTO.TodayMission> todayMissions = getTodayMissions(user);
        boolean hasIncompleteMission = hasIncompleteMission(todayMissions);

        HomeResponseDTO.SpeechBubble speechBubble = createSpeechBubble(
                monthlyBudget,
                remainingBudget,
                todayAvailableAmount,
                hasIncompleteMission
        );

        HomeResponseDTO.RoomInfo roomInfo = getRoomInfo(user);

        return new HomeResponseDTO(
                defaultString(user.getNickname(), DEFAULT_NICKNAME),
                defaultString(user.getCharacterName(), DEFAULT_CHARACTER_NAME),
                monthlyBudget,
                monthlySavingGoal,
                monthlyExpenseAmount,
                remainingBudget,
                todayAvailableAmount,
                point,
                budgetStatus.name(),
                speechBubble,
                roomInfo,
                todayMissions
        );
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
    }

    private Integer calculateMonthlyExpenseAmount(User user) {
        YearMonth currentMonth = YearMonth.now();
        LocalDate startDate = currentMonth.atDay(1);
        LocalDate endDate = currentMonth.atEndOfMonth();

        return expenseRepository.findAllByUserAndExpenseDateBetween(user, startDate, endDate)
                .stream()
                .mapToInt(Expense::getAmount)
                .sum();
    }

    private Integer calculateRemainingBudget(Integer monthlyBudget, Integer monthlyExpenseAmount) {
        return monthlyBudget - monthlyExpenseAmount;
    }

    private Integer calculateTodayAvailableAmount(Integer remainingBudget) {
        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(today);

        int lastDayOfMonth = currentMonth.atEndOfMonth().getDayOfMonth();
        int todayDayOfMonth = today.getDayOfMonth();
        int remainingDays = lastDayOfMonth - todayDayOfMonth + 1;

        if (remainingDays <= 0) {
            return remainingBudget;
        }

        return remainingBudget / remainingDays;
    }

    private BudgetStatus calculateBudgetStatus(
            Integer monthlyBudget,
            Integer remainingBudget,
            Integer todayAvailableAmount
    ) {
        if (monthlyBudget == null || monthlyBudget <= 0) {
            return BudgetStatus.NO_BUDGET;
        }

        if (remainingBudget < 0) {
            return BudgetStatus.MONTH_OVER;
        }

        if (todayAvailableAmount < 0) {
            return BudgetStatus.TODAY_OVER;
        }

        return BudgetStatus.NORMAL;
    }
    /*
 * TODO:
 * 말풍선 조건이 여러 개 동시에 해당될 수 있어 현재는 아래 우선순위를 임시로 적용
 *
 * 1. 이번 달 예산 초과
 * 2. 오늘 사용 가능 금액 초과
 * 3. 이번 달 남은 예산 20% 이하
 * 4. 오늘 사용 가능 금액 30% 이하
 * 5. 오늘 미션 미완료
 * 6. 정상
 */

    private HomeResponseDTO.SpeechBubble createSpeechBubble(
            Integer monthlyBudget,
            Integer remainingBudget,
            Integer todayAvailableAmount,
            boolean hasIncompleteMission
    ) {
        if (monthlyBudget == null || monthlyBudget <= 0) {
            return new HomeResponseDTO.SpeechBubble(
                    "NORMAL",
                    "오늘도 부자가 되는 연습을 해볼까요?",
                    "INFO"
            );
        }

        if (remainingBudget < 0) {
            return new HomeResponseDTO.SpeechBubble(
                    "MONTHLY_BUDGET_OVER",
                    "이번 달 예산을 초과했어요. 소비를 한번 점검해봐요.",
                    "DANGER"
            );
        }

        if (todayAvailableAmount < 0) {
            return new HomeResponseDTO.SpeechBubble(
                    "DAILY_BUDGET_OVER",
                    "오늘 예산을 초과했어요. 내일은 조금만 아껴봐요.",
                    "DANGER"
            );
        }

        if (remainingBudget <= monthlyBudget * 0.2) {
            return new HomeResponseDTO.SpeechBubble(
                    "MONTHLY_BUDGET_WARNING",
                    "이번 달 예산이 얼마 남지 않았어요.",
                    "WARNING"
            );
        }

        Integer dailyStandardAmount = calculateDailyStandardAmount(monthlyBudget);

        if (dailyStandardAmount > 0 && todayAvailableAmount <= dailyStandardAmount * 0.3) {
            return new HomeResponseDTO.SpeechBubble(
                    "DAILY_BUDGET_WARNING",
                    "오늘 사용할 수 있는 금액이 얼마 남지 않았어요.",
                    "WARNING"
            );
        }

        if (hasIncompleteMission) {
            return new HomeResponseDTO.SpeechBubble(
                    "MISSION_REMINDER",
                    "오늘의 미션을 완료하면 포인트를 받을 수 있어요.",
                    "INFO"
            );
        }

        return new HomeResponseDTO.SpeechBubble(
                "NORMAL",
                "오늘도 부자가 되는 연습을 해볼까요?",
                "INFO"
        );
    }

    private Integer calculateDailyStandardAmount(Integer monthlyBudget) {
        if (monthlyBudget == null || monthlyBudget <= 0) {
            return 0;
        }

        YearMonth currentMonth = YearMonth.now();
        int daysInMonth = currentMonth.lengthOfMonth();

        return monthlyBudget / daysInMonth;
    }

    private HomeResponseDTO.RoomInfo getRoomInfo(User user) {
        Room room = roomRepository.findByUser(user).orElse(null);

        String backgroundImageUrl = room == null
                ? DEFAULT_ROOM_IMAGE_URL
                : room.getBackgroundImageUrl();

        return new HomeResponseDTO.RoomInfo(
                backgroundImageUrl,
                getDefaultAppliedItems()
        );
    }

    private List<HomeResponseDTO.AppliedItem> getDefaultAppliedItems() {
        /*
         * 현재 Room 엔티티에는 backgroundImageUrl만 존재하고,
         * 실제 적용 아이템을 조회할 수 있는 Item/Inventory/AppliedItem 도메인이 없어 기본 벽지 아이템을 임시로 반환
         */
        return List.of(
                new HomeResponseDTO.AppliedItem(
                        1L,
                        "WALLPAPER",
                        "기본 벽지",
                        "/items/wallpaper-default.png"
                )
        );
    }

    private List<HomeResponseDTO.TodayMission> getTodayMissions(User user) {
        LocalDate today = LocalDate.now();

        return missionRepository.findAllByUserAndMissionDate(user, today)
                .stream()
                /*
                 * TODO:
                 * 홈 화면에는 오늘 미션 일부만 노출하기 위해 현재 최대 2개만 반환되는 걸로 했습니다
                 */
                .limit(2)
                .map(this::toTodayMission)
                .toList();
    }

    private HomeResponseDTO.TodayMission toTodayMission(Mission mission) {
        return new HomeResponseDTO.TodayMission(
                mission.getId(),
                mission.getTitle(),
                mission.getDescription(),
                mission.getProgress(),
                mission.getTargetCount(),
                mission.getRewardPoint(),
                mission.getStatus().name()
        );
    }

    private boolean hasIncompleteMission(List<HomeResponseDTO.TodayMission> todayMissions) {
        return todayMissions.stream()
                .anyMatch(mission -> !MissionStatus.COMPLETED.name().equals(mission.status()));
    }

    private Integer defaultValue(Integer value) {
        return value == null ? 0 : value;
    }

    private String defaultString(String value, String defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return value;
    }
}