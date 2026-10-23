package com.example.carRentalSystem.repository;

import com.example.carRentalSystem.entity.Car;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {

    @Query(
            """
        SELECT c from Car c 
        WHERE (:type IS NULL OR c.type = :type )
        AND (:costPerHour IS NULL OR c.costPerHour <= :costPerHour)
"""
    )
    List<Car> findCarByTypeAndCostPerHour(@Param("type") String type,
                                          @Param("costPerHour") BigDecimal costPerHour);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Car c WHERE c.id = :carId")
    Optional<Car> findByIdWithLock(@Param("carId") Long carId);
}
