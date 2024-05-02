package org.choreo.demo.luxury.hotels.dto;

import org.choreo.demo.luxury.hotels.model.Room;
import org.choreo.demo.luxury.hotels.model.Reservation;

import java.time.LocalDateTime;

public class ReservationDto {
    private int id;

    private Room room;

    private LocalDateTime checkinDate;
    private LocalDateTime checkoutDate;

    private UserDto user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDateTime getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(LocalDateTime checkinDate) {
        this.checkinDate = checkinDate;
    }

    public LocalDateTime getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDateTime checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public static ReservationDto from(Reservation reservation){
        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId());
        dto.setCheckinDate(reservation.getCheckinDate().atStartOfDay());
        dto.setCheckoutDate(reservation.getCheckoutDate().atStartOfDay());
        dto.setUser(UserDto.from(reservation.getUser()));
        dto.setRoom(reservation.getRoom());
        return dto;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", room=" + room +
                ", checkinDate=" + checkinDate +
                ", checkoutDate=" + checkoutDate +
                ", user=" + user +
                '}';
    }

}
