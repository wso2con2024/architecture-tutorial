package org.choreo.demo.luxury.hotels.repo;

import org.choreo.demo.luxury.hotels.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    @Query(value = "SELECT rt FROM RoomType rt WHERE rt.guestCapacity >= :guestCapacity AND EXISTS (" +
            "SELECT r FROM Room r WHERE r.type = rt AND NOT EXISTS (" +
            "SELECT res FROM Reservation res WHERE res.room = r " +
            "AND res.checkinDate < :checkoutDate AND res.checkoutDate > :checkinDate))")
    List<RoomType> findAvailableRoomTypes(@Param("checkinDate") LocalDate checkinDate,
                                          @Param("checkoutDate") LocalDate checkoutDate,
                                          @Param("guestCapacity") int guestCapacity);

}
