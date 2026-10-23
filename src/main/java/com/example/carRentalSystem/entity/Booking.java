package com.example.carRentalSystem.entity;

import com.example.carRentalSystem.enums.BookingStatus;
import com.example.carRentalSystem.model.DatePeriod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "renter_id")
    private Renter renter;

    @Embedded
    private DatePeriod datePeriod;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDate actualReturnDate;

}
