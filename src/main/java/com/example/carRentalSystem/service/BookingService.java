package com.example.carRentalSystem.service;

import com.example.carRentalSystem.entity.Booking;
import com.example.carRentalSystem.entity.Car;
import com.example.carRentalSystem.entity.Renter;
import com.example.carRentalSystem.enums.BookingStatus;
import com.example.carRentalSystem.exception.ResourceNotFoundException;
import com.example.carRentalSystem.model.Criteria;
import com.example.carRentalSystem.model.DatePeriod;
import com.example.carRentalSystem.repository.BookingRepository;
import com.example.carRentalSystem.repository.CarRepository;
import com.example.carRentalSystem.repository.RenterRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final RenterRepository renterRepository;

    public BookingService(
            BookingRepository bookingRepository,
            CarRepository carRepository,
            RenterRepository renterRepository) {

        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
        this.renterRepository = renterRepository;
    }

    // Create a new car booking
    @Transactional
    public Booking rentCar(
            Long carId,
            Long renterId,
            LocalDate startTime,
            LocalDate endTime) {

        // TODO 1: Validate start and end dates
        LocalDate today = LocalDate.now();

        if (startTime.isBefore(today)) {
            throw new IllegalArgumentException(
                    "Start Date Can't be in Past");
        }

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException(
                    "Start date must be before end date");
        }

        // TODO 2: Find the car
        Car car = carRepository.findByIdWithLock(carId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Car Doesn't Exist"));

        // TODO 3: Find the renter
        Renter renter = renterRepository.findById(renterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Renter Not Found"));

        // TODO 4: Check for overlapping bookings
        List<Booking> overlappingBookings =
                bookingRepository
                        .findOverlappingBookings(
                                car,
                                endTime,
                                startTime,
                                BookingStatus.CANCELLED,
                                BookingStatus.COMPLETED
                        );

        if (!overlappingBookings.isEmpty()) {
            throw new IllegalStateException(
                    "Car is already booked for these dates.");
        }

        // TODO 5: Create and save the booking
        DatePeriod datePeriod = new DatePeriod();
        datePeriod.setStartDate(startTime);
        datePeriod.setEndDate(endTime);

        Booking booking = new Booking();
        booking.setCar(car);
        booking.setRenter(renter);
        booking.setDatePeriod(datePeriod);
        booking.setStatus(BookingStatus.CONFIRMED);

        return bookingRepository.save(booking);
    }

    // Return a car: only ACTIVE bookings can be completed // or can be return earlier ->implemented
    @Transactional
    public Booking returnCar(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking Doesn't Exist"));

        if (booking.getStatus() == BookingStatus.ACTIVE) {

            booking.setActualReturnDate(LocalDate.now());
            booking.setStatus(BookingStatus.COMPLETED);
            return bookingRepository.save(booking);
        }

        throw new IllegalStateException(
                "Cannot return a Booking with Status: "
                        + booking.getStatus());
    }

    // Search cars using criteria
    public List<Car> getAllCar(Criteria criteria) {

        if (criteria == null) {
            return carRepository.findAll();
        }

        return carRepository.findCarByTypeAndCostPerHour(
                criteria.getType(),
                criteria.getMaxCostPerHour()
        );
    }

    // Cancel a confirmed booking
    @Transactional
    public Booking cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking Doesn't Exist"));

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            booking.setStatus(BookingStatus.CANCELLED);
            return bookingRepository.save(booking);
        }

        throw new IllegalStateException("Invalid Action");
    }

    // Automatically activate eligible bookings
    @Transactional
    public List<Booking> setActiveStatus() {

        LocalDate today = LocalDate.now();

        System.out.println(
                "Set Active Status Method Started: " + LocalDateTime.now());

        List<Booking> ongoingBookings =
                bookingRepository
                        .findByStatusAndDatePeriod_StartDateLessThanEqualAndDatePeriod_EndDateAfter(
                                BookingStatus.CONFIRMED,
                                today,
                                today
                        );

        ongoingBookings.forEach(
                booking -> booking.setStatus(BookingStatus.ACTIVE)
        );

        List<Booking> updatedBookings =
                bookingRepository.saveAll(ongoingBookings);

        System.out.println(
                "Set Active Status Method Ended: " + LocalDateTime.now());

        return updatedBookings;
    }

    public BookingStatus checkVehicleBookingStatus(Long bookingId){

        Booking booking =  bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Booking Doesn't Exist")
        );

        return booking.getStatus();
    }
}