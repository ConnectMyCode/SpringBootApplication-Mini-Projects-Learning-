package org.spring.core.learn.spring14fitclubmvc.repository;

import org.spring.core.learn.spring14fitclubmvc.entity.Booking;
import org.spring.core.learn.spring14fitclubmvc.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long>{

    List<Booking> findByMember(Long MemberId);

    long countByFitnessClass_IdAndStatus(Long  classId, BookingStatus status);

    boolean existsByMember_IdAndFitnessClass_IdAndStatus(Long memberId, Long classId, BookingStatus status);


    @Query("SELECT b FROM Booking b " +
           "JOIN FETCH b.member " +
           "JOIN FETCH b.fitnessClass "+
        "WHERE b.member.id = :memberId "+
         "ORDER BY b.bookingDate DESC")
    List<Booking> findMemberBookingWithDetails(@Param("memberId") Long memberId);

}
