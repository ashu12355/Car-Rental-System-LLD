package com.example.carRentalSystem.util;

import com.example.carRentalSystem.model.DatePeriod;

public class DatePeriodUtil {

    public static boolean areOverlapping(DatePeriod first, DatePeriod second) {

        return second.getStartDate().isBefore(first.getEndDate())
                && second.getEndDate().isAfter(first.getStartDate());
    }

}
