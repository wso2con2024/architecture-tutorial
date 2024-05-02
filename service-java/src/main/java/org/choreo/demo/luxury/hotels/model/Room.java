package org.choreo.demo.luxury.hotels.model;

import jakarta.persistence.*;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @Column(name = "number")
    private int number;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private RoomType type;

    // Constructors, getters, and setters


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Room{" +
                "number=" + number +
                ", type=" + type +
                '}';
    }
}
