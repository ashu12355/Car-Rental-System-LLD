package com.example.carRentalSystem.controller;

import com.example.carRentalSystem.entity.Car;
import com.example.carRentalSystem.repository.CarRepository;
import com.example.carRentalSystem.service.BookingService;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarRepository carRepository;

    public CarController(CarRepository carRepository) {

        this.carRepository = carRepository;
    }

    @PostMapping()
    public Car saveCar(@RequestBody Car car){
        return carRepository.save(car);
    }

    @GetMapping()
    public List<Car> getAllCars(){
        return carRepository.findAll();
    }

    @GetMapping("/search")
    public List<Car> searchCars(
            @RequestParam(required = false) String type,
            @RequestParam(required = false)BigDecimal maxCostPerHour
            ){
        return carRepository.findCarByTypeAndCostPerHour(type,maxCostPerHour);

    }
}
