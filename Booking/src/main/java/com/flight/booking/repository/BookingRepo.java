package com.flight.booking.repository;
import com.flight.booking.entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Bookings, Integer> {

    List<Bookings> findByEmailId(String email);

}
