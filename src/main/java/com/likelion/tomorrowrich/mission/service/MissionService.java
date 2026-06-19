package com.likelion.tomorrowrich.mission.service;

import com.likelion.tomorrowrich.global.exception.BusinessException;
import com.likelion.tomorrowrich.global.exception.ErrorCode;
import com.likelion.tomorrowrich.mission.dto.MissionCompleteRequestDTO;
import com.likelion.tomorrowrich.mission.dto.MissionCompleteResponseDTO;
import com.likelion.tomorrowrich.mission.dto.MissionListResponseDTO;
import com.likelion.tomorrowrich.mission.entity.Mission;
import com.likelion.tomorrowrich.mission.entity.MissionStatus;
import com.likelion.tomorrowrich.mission.entity.MissionType;
import com.likelion.tomorrowrich.mission.repository.MissionRepository;
import com.likelion.tomorrowrich.point.entity.PointHistory;
import com.likelion.tomorrowrich.point.entity.PointHistoryType;
import com.likelion.tomorrowrich.point.repository.PointHistoryRepository;
import com.likelion.tomorrowrich.user.entity.User;
import com.likelion.tomorrowrich.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MissionService {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public MissionService(
            MissionRepository missionRepository,
            UserRepository userRepository,
            PointHistoryRepository pointHistoryRepository
    ) {
        this.missionRepository = missionRepository;
        this.userRepository = userRepository;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    public MissionListResponseDTO getMissions(Long userId, String date) {
        User user = findUser(userId);
        LocalDate missionDate = parseDate(date);

        List<MissionListResponseDTO.MissionDTO> missions = missionRepository
                .findAllByUserAndMissionDate(user, missionDate)
                .stream()
                .map(this::toMissionDTO)
                .toList();

        return new MissionListResponseDTO(
                missionDate.toString(),
                missionDate.isAfter(LocalDate.now()),
                missions
        );
    }

    @Transactional
    public MissionCompleteResponseDTO completeMission(
            Long userId,
            Long missionId,
            MissionCompleteRequestDTO request
    ) {
        User user = findUser(userId);
        LocalDate requestDate = parseDate(request.date());
        Mission mission = findMission(missionId, user);

        validateMissionDate(mission, requestDate);
        validateCompletable(mission);

        mission.complete();
        user.addPoint(mission.getRewardPoint());
        pointHistoryRepository.save(new PointHistory(
                user,
                PointHistoryType.EARN,
                mission.getRewardPoint(),
                mission.getTitle() + " 미션 완료"
        ));

        return new MissionCompleteResponseDTO(
                mission.getId(),
                mission.getStatus().name(),
                mission.getRewardPoint(),
                user.getCurrentPoint(),
                mission.getPointReceived()
        );
    }

    @Transactional
    public List<Long> updateExpenseRecordMissionProgress(User user, LocalDate missionDate) {
        return missionRepository.findAllByUserAndMissionDate(user, missionDate)
                .stream()
                .filter(mission -> mission.getMissionType() == MissionType.EXPENSE_RECORD)
                .filter(mission -> mission.getStatus() != MissionStatus.COMPLETED)
                .peek(mission -> mission.updateProgress(mission.getTargetCount()))
                .map(Mission::getId)
                .toList();
    }

    private MissionListResponseDTO.MissionDTO toMissionDTO(Mission mission) {
        return new MissionListResponseDTO.MissionDTO(
                mission.getId(),
                mission.getTitle(),
                mission.getDescription(),
                mission.getMissionType().name(),
                mission.getProgress(),
                mission.getTargetCount(),
                mission.getRewardPoint(),
                mission.getStatus().name(),
                mission.getPointReceived()
        );
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
    }

    private Mission findMission(Long missionId, User user) {
        return missionRepository.findByIdAndUser(missionId, user)
                .orElseThrow(() -> new BusinessException(ErrorCode.MISSION_NOT_FOUND));
    }

    private LocalDate parseDate(String date) {
        if (date == null || date.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException exception) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
    }

    private void validateMissionDate(Mission mission, LocalDate requestDate) {
        if (!mission.getMissionDate().equals(requestDate)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        if (requestDate.isAfter(LocalDate.now())) {
            throw new BusinessException(ErrorCode.FUTURE_MISSION_NOT_ALLOWED);
        }
    }

    private void validateCompletable(Mission mission) {
        if (mission.getStatus() == MissionStatus.COMPLETED || mission.getPointReceived()) {
            throw new BusinessException(ErrorCode.MISSION_ALREADY_COMPLETED);
        }

        if (mission.getStatus() != MissionStatus.COMPLETABLE) {
            throw new BusinessException(ErrorCode.MISSION_NOT_COMPLETABLE);
        }
    }
}
