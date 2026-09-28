package org.spring.core.learn.spring14fitclubmvc.service;

import jakarta.transaction.Transactional;
import org.spring.core.learn.spring14fitclubmvc.entity.Booking;
import org.spring.core.learn.spring14fitclubmvc.entity.BookingStatus;
import org.spring.core.learn.spring14fitclubmvc.entity.FitnessClass;
import org.spring.core.learn.spring14fitclubmvc.entity.Member;
import org.spring.core.learn.spring14fitclubmvc.exception.AlreadyBookedException;
import org.spring.core.learn.spring14fitclubmvc.exception.ClassFullException;
import org.spring.core.learn.spring14fitclubmvc.exception.ResourceNotFoundException;
import org.spring.core.learn.spring14fitclubmvc.repository.BookingRepository;
import org.spring.core.learn.spring14fitclubmvc.repository.FitnessClassRepository;
import org.spring.core.learn.spring14fitclubmvc.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClassBookingServiceImpl implements ClassBookingService {

    private final MemberRepository memberRepository;
    private final FitnessClassRepository fitnessClassRepository;
    private final BookingRepository bookingRepository;


    public ClassBookingServiceImpl(MemberRepository memberRepository, FitnessClassRepository fitnessClassRepository, BookingRepository bookingRepository) {
        this.memberRepository = memberRepository;
        this.fitnessClassRepository = fitnessClassRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<FitnessClass> listAllClasses() {
        // STREAM: sort the collection by class name, declaratively

        return fitnessClassRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(FitnessClass::getClassName))
                .collect(Collectors.toList());
    }


    /*
     * ================= SPRING CORE / JPA REVISION: @Transactional =================
     * This method does multiple database operations (read member, read
     * class, count existing bookings, then insert). @Transactional makes
     * them all happen inside ONE database transaction - if anything after
     * the first write fails, everything rolls back together.
     */
    @Transactional
    @Override
    public Booking bookClass(Long memberId, Long classId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found: " + memberId));

        FitnessClass fitnessClass = fitnessClassRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + classId));

        boolean alreadyBooked = bookingRepository.existsByMember_IdAndFitnessClass_IdAndStatus(
                memberId, classId, BookingStatus.CONFIRMED);
        if (alreadyBooked) {
            throw new AlreadyBookedException("You already booked: " + fitnessClass.getClassName());
        }

        long currentBookings = bookingRepository.countByFitnessClass_IdAndStatus(classId, BookingStatus.CONFIRMED);
        if (currentBookings >= fitnessClass.getCapacity()) {
            throw new ClassFullException("Class is full: " + fitnessClass.getClassName());
        }

        Booking booking = new Booking(null, member, fitnessClass, LocalDateTime.now(), BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);


    }

    @Override
    public List<Booking> getMemberBookings(Long memberId) {
        return bookingRepository.findMemberBookingWithDetails(memberId);
    }

    @Override
    public Map<String, Long> getConfirmedBookingCountByClassName() {

        return bookingRepository.findAll()
                .stream()
                .filter(b-> b.getStatus()== BookingStatus.CONFIRMED)
                .collect(Collectors.groupingBy(
                        b -> b.getFitnessClass().getClassName(),
                        Collectors.counting() ));
    }
}