package com.example.carRentalSystem.controller;

import com.example.carRentalSystem.entity.Booking;
import com.example.carRentalSystem.enums.BookingStatus;
import com.example.carRentalSystem.service.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public Booking rentCar(
            @RequestParam Long carId,
            @RequestParam Long renterId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endTime
            ){
        return bookingService.rentCar(carId,renterId,startTime,endTime);
    }

    @PutMapping("/{bookingId}/return")
    public Booking returnCar(@PathVariable Long bookingId){
        return bookingService.returnCar(bookingId);
    }

    @PutMapping("/{bookingId}/cancel")
    public Booking cancelBooking(@PathVariable Long bookingId){
        return bookingService.cancelBooking(bookingId);
    }

    @PutMapping("/{bookingId}/checkStatus")
    public BookingStatus checkBookingStatus(@PathVariable Long bookingId){
        return bookingService.checkVehicleBookingStatus(bookingId);
    }
}
