package com.likelion.tomorrowrich.mission.repository;

import com.likelion.tomorrowrich.mission.entity.Mission;
import com.likelion.tomorrowrich.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> findAllByUserAndMissionDate(User user, LocalDate missionDate);

    Optional<Mission> findByIdAndUser(Long id, User user);
}
