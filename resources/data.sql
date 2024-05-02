-- Insert or update room types
INSERT INTO room_type (id, name, guest_capacity, price) VALUES (0, 'Single', 1, 80.00)
ON DUPLICATE KEY UPDATE name=VALUES(name), guest_capacity=VALUES(guest_capacity), price=VALUES(price);

INSERT INTO room_type (id, name, guest_capacity, price) VALUES (1, 'Double', 2, 120.00)
ON DUPLICATE KEY UPDATE name=VALUES(name), guest_capacity=VALUES(guest_capacity), price=VALUES(price);

INSERT INTO room_type (id, name, guest_capacity, price) VALUES (2, 'Family', 4, 200.00)
ON DUPLICATE KEY UPDATE name=VALUES(name), guest_capacity=VALUES(guest_capacity), price=VALUES(price);

INSERT INTO room_type (id, name, guest_capacity, price) VALUES (3, 'Suite', 4, 300.00)
ON DUPLICATE KEY UPDATE name=VALUES(name), guest_capacity=VALUES(guest_capacity), price=VALUES(price);



-- Insert rooms, assuming RoomType entries exist
INSERT INTO room (number, type_id) VALUES (101, 0);
INSERT INTO room (number, type_id) VALUES (102, 0);
INSERT INTO room (number, type_id) VALUES (103, 0);
INSERT INTO room (number, type_id) VALUES (104, 0);
INSERT INTO room (number, type_id) VALUES (105, 1);
INSERT INTO room (number, type_id) VALUES (106, 1);
-- Add more inserts for all rooms as per the structure
-- ...
INSERT INTO room (number, type_id) VALUES (406, 3);



INSERT INTO users (id, name, email, mobile_number) VALUES('1111', 'example user', 'email@example.com', '111111111');

INSERT INTO reservation (id, room_number, checkin_date, checkout_date, user_id) VALUES(1, 101, '2024-02-15', '2024-02-17', '1111');
