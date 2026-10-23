package com.example.carRentalSystem.controller;

import com.example.carRentalSystem.entity.Renter;
import com.example.carRentalSystem.repository.RenterRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/renters")
public class RenterController {

    private final RenterRepository renterRepository;


    public RenterController(RenterRepository renterRepository) {
        this.renterRepository = renterRepository;
    }

    @PostMapping
    public Renter saveRenter(@RequestBody Renter renter) {
        return renterRepository.save(renter);
    }

    @GetMapping
    public List<Renter> getAllRenters() {
        return renterRepository.findAll();
    }

}
