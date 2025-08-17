package ru.practicum.shareit.booking.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    @Query("""
            SELECT b FROM Booking b
            JOIN FETCH b.item i
            JOIN FETCH b.booker
            WHERE i.id IN :itemIds
            """)
    List<Booking> findAllByItemIdIn(@Param("itemIds") List<Long> itemIds);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :userId
            AND b.status = APPROVED
            AND b.start < :now
            AND b.end > :now
            """)
    List<Booking> findCurrentByBookerId(@Param("userId") Long userId,
                                        @Param("now") LocalDateTime now);

    List<Booking> findByBookerIdAndStatus(Long userId, Status status);

    List<Booking> findByBookerIdAndStatusAndEndBefore(Long userId, Status status, LocalDateTime now);

    List<Booking> findByBookerIdAndStartAfter(Long userId, LocalDateTime now);

    List<Booking> findByBookerIdOrderByStartDesc(Long userId);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = :ownerId
            AND b.status = APPROVED
            AND b.start < :now
            AND b.end > :now
            """)
    List<Booking> findCurrentByOwnerId(@Param("ownerId") Long ownerId,
                                       @Param("now") LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStatus(Long ownerId, Status status);

    List<Booking> findByItemOwnerIdAndStatusAndEndBefore(Long userId, Status status, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStartAfter(Long ownerId, LocalDateTime now);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    @Query("""
        SELECT COUNT(b) > 0
        FROM Booking b
        WHERE b.item.id = :itemId
        AND (
            :startNewBooking <= b.end AND :endNewBooking >= b.start
            )
        AND b.status = APPROVED
        """)
    Boolean timeCrossingCheck(@Param("itemId") Long itemId,
                              @Param("startNewBooking") LocalDateTime startNewBooking,
                              @Param("endNewBooking") LocalDateTime endNewBooking);

    @Query("""
        SELECT COUNT(b) > 0
        FROM Booking b
        WHERE b.booker.id = :userId
        AND b.item.id = :itemId
        AND b.status = APPROVED
        AND b.end < :now
        """)
    Boolean existsPastBookingsByBookerIdAndItemId(@Param("userId") Long userId,
                                                  @Param("itemId") Long itemId,
                                                  @Param("now") LocalDateTime now);

}