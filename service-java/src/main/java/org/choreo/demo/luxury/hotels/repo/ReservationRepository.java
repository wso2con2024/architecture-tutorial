package org.choreo.demo.luxury.hotels.repo;

import org.choreo.demo.luxury.hotels.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE " +
            "(r.checkinDate BETWEEN :givenStartDate AND :givenEndDate) " +
            "OR (r.checkoutDate BETWEEN :givenStartDate AND :givenEndDate) " +
            "OR (r.checkinDate <= :givenStartDate AND r.checkoutDate >= :givenEndDate)")
    List<Reservation> findReservationsWithinRange(@Param("givenStartDate") LocalDate givenStartDate,
                                                  @Param("givenEndDate") LocalDate givenEndDate);



    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId")
    List<Reservation> findByUserId(@Param("userId") String userId);
}
