package org.choreo.demo.luxury.hotels;

import org.choreo.demo.luxury.hotels.dto.ReservationDto;
import org.choreo.demo.luxury.hotels.dto.ReservationRequest;
import org.choreo.demo.luxury.hotels.dto.UpdateReservationRequest;
import org.choreo.demo.luxury.hotels.model.Reservation;
import org.choreo.demo.luxury.hotels.model.ReservationEvent;
import org.choreo.demo.luxury.hotels.model.RoomType;
import org.choreo.demo.luxury.hotels.service.EmailRequest;
import org.choreo.demo.luxury.hotels.service.NotificationService;
import org.choreo.demo.luxury.hotels.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/emails")
public class EmailController {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    //@Autowired
    //NotificationService notificationService;

    @PostMapping("/")
    public ResponseEntity<String> createReservation(@RequestBody EmailRequest emailRequest) {

        // notificationService.sendEmail(emailRequest)
        //         .subscribe(
        //                 successResponse -> logger.info("Email sent successfully: {}", successResponse),
        //                 error -> logger.error("Error sending email: ", error));
        return new ResponseEntity<>("email sent", HttpStatus.OK);
    }

}
