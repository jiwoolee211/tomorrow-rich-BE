package com.likelion.tomorrowrich.room.repository;

import com.likelion.tomorrowrich.room.entity.Room;
import com.likelion.tomorrowrich.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByUser(User user);
}
