package org.choreo.demo.luxury.hotels.service;

import org.choreo.demo.luxury.hotels.dto.ReservationRequest;
import org.choreo.demo.luxury.hotels.dto.UpdateReservationRequest;
import org.choreo.demo.luxury.hotels.model.Reservation;
import org.choreo.demo.luxury.hotels.model.Room;
import org.choreo.demo.luxury.hotels.model.RoomType;
import org.choreo.demo.luxury.hotels.model.User;
import org.choreo.demo.luxury.hotels.repo.ReservationRepository;
import org.choreo.demo.luxury.hotels.repo.RoomRepository;
import org.choreo.demo.luxury.hotels.repo.RoomTypeRepository;
import org.choreo.demo.luxury.hotels.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private RoomRepository roomRepository;

    public List<RoomType> getAvailableRoomTypes(String checkinDate, String checkoutDate, int guestCapacity)
            throws Exception {

        List<Reservation> reservations = reservationRepository.findReservationsWithinRange(convertToDate(checkinDate),
                convertToDate(checkoutDate));

        logger.info("reservations in the date range: " + reservations);

        List<RoomType> roomTypes = roomTypeRepository.findAvailableRoomTypes(convertToDate(checkinDate),
                convertToDate(checkoutDate), guestCapacity);

        return roomTypes;
    }

    public boolean isAvailable(String checkinDate, String checkoutDate, String roomType) {
        return !roomRepository.findAvailableRooms(convertToDate(checkinDate), convertToDate(checkoutDate), roomType)
                .isEmpty();
    }

    public Reservation save(ReservationRequest reservationRequest) {

        Optional<User> userOptional = userRepository.findById(reservationRequest.getUser().getId());
        User user = null;
        if (userOptional.isEmpty()) {
            User u = new User();
            u.setId(reservationRequest.getUser().getId());
            u.setEmail(reservationRequest.getUser().getEmail());
            u.setName(reservationRequest.getUser().getName());
            u.setMobileNumber(reservationRequest.getUser().getMobileNumber());

            user = userRepository.save(u);

        } else {
            user = userOptional.get();
        }

        LocalDate checkinDate = convertToDate(reservationRequest.getCheckinDate());
        LocalDate checkoutDate = convertToDate(reservationRequest.getCheckoutDate());

        List<Room> selectedRooms = roomRepository.findAvailableRooms(checkinDate,
                checkoutDate, reservationRequest.getRoomType());

        if (selectedRooms.isEmpty()) {
            throw new RuntimeException("capacity full for : " + reservationRequest);
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(selectedRooms.get(0));
        reservation.setCheckinDate(checkinDate);
        reservation.setCheckoutDate(checkoutDate);

        return reservationRepository.save(reservation);
    }

    private LocalDate convertToDate(String dateUTC) {
        Instant instant = Instant.parse(dateUTC);
        return LocalDate.ofInstant(instant, ZoneId.systemDefault());// or ZoneId.of("UTC")
    }

    public Reservation update(Long reservationId, UpdateReservationRequest updateReservationRequest) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            reservation.setCheckinDate(convertToDate(updateReservationRequest.getCheckinDate()));
            reservation.setCheckoutDate(convertToDate(updateReservationRequest.getCheckoutDate()));
            reservationRepository.save(reservation);
            return reservation;
        } else {
            throw new RuntimeException("unable to find the reservation: " + updateReservationRequest);
        }
    }

    public Optional<Reservation> delete(Long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        reservation.ifPresent(reservationRepository::delete);
        return reservation;
    }

    public List<Reservation> findByUserId(String userId) {
        return reservationRepository.findByUserId(userId);
    }

    // Other methods to manipulate reservations
}
