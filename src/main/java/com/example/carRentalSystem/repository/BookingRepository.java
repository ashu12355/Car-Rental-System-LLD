package com.example.carRentalSystem.repository;

import com.example.carRentalSystem.entity.Booking;
import com.example.carRentalSystem.entity.Car;
import com.example.carRentalSystem.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    @Query("""
    SELECT b FROM Booking b
    WHERE b.car = :car
        AND b.status <> :cancelledStatus
        AND b.datePeriod.startDate < :requestedEndDate
        AND (
            b.status = :completedStatus
                AND b.actualReturnDate IS NOT NULL
                AND b.actualReturnDate > :requestedStartDate)
             OR
             (b.status <> :completedStatus
                AND b.datePeriod.endDate > : requestedStartDate
             )
""")
    List<Booking> findOverlappingBookings(
            @Param("car") Car car,
            @Param("requestedStartDate") LocalDate requestedStartDate,
            @Param("requestedEndDate") LocalDate requestedEndDate,
            @Param("cancelledStatus") BookingStatus cancelledStatus,
            @Param("completedStatus") BookingStatus completedStatus
    );

    List<Booking> findByStatusAndDatePeriod_StartDateLessThanEqualAndDatePeriod_EndDateAfter(
            BookingStatus status,
            LocalDate startDate,
            LocalDate endDate
    );


}


/*
findByCar — checks bookings for the specified car.
DatePeriod_StartDateBefore — existing booking starts before the requested end date.
DatePeriod_EndDateAfter — existing booking ends after the requested start date.
StatusNot — excludes bookings with the specified status, here CANCELLED.
 */