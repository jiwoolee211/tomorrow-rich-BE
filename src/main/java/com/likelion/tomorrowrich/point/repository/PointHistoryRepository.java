package com.likelion.tomorrowrich.point.repository;

import com.likelion.tomorrowrich.point.entity.PointHistory;
import com.likelion.tomorrowrich.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findAllByUserOrderByCreatedAtDesc(User user);
}
