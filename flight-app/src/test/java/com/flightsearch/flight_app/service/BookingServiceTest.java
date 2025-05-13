package com.flightsearch.flight_app.service;

import com.flightsearch.flight_app.model.Booking;
import com.flightsearch.flight_app.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookFlight() {
        Booking booking = new Booking();
        booking.setUserId(1);
        booking.setFlightId(101);

        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.bookFlight(booking);
        assertNotNull(result, "Booking should not be null");
        assertEquals(1, result.getUserId(), "User ID should match");
        assertEquals(101, result.getFlightId(), "Flight ID should match");

        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testGetAllBookings() {
        Booking booking1 = new Booking();
        booking1.setUserId(1);
        booking1.setFlightId(101);

        Booking booking2 = new Booking();
        booking2.setUserId(2);
        booking2.setFlightId(102);

        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking1, booking2));

        List<Booking> bookings = bookingService.getAllBookings();
        assertEquals(2, bookings.size(), "There should be two bookings");

        verify(bookingRepository, times(1)).findAll();
    }
}
