package org.choreo.demo.luxury.hotels.repo;

import org.choreo.demo.luxury.hotels.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Room r WHERE r.type.name = :roomTypeName AND r.number NOT IN (" +
            "SELECT res.room.number FROM Reservation res WHERE res.checkinDate < :checkoutDate AND res.checkoutDate > :checkinDate)")
    List<Room> findAvailableRooms(
            @Param("checkinDate") LocalDate checkinDate,
            @Param("checkoutDate") LocalDate checkoutDate,
            @Param("roomTypeName") String roomTypeName);

}
