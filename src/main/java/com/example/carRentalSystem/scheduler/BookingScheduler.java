package com.example.carRentalSystem.scheduler;

import com.example.carRentalSystem.service.BookingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class BookingScheduler {

    private final BookingService bookingService;


    public BookingScheduler(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Scheduled(fixedRate = 60000)
    public void activateBooking() {
        System.out.println("Schdeduler started At"+ LocalDateTime.now());
        bookingService.setActiveStatus();
        System.out.println("Schdeduler Ended At"+ LocalDateTime.now());
    }


}
