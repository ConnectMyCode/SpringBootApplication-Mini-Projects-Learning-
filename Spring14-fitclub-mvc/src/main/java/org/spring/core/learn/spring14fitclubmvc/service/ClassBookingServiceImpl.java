package org.spring.core.learn.spring14fitclubmvc.service;

import org.spring.core.learn.spring14fitclubmvc.entity.Booking;
import org.spring.core.learn.spring14fitclubmvc.entity.FitnessClass;
import org.spring.core.learn.spring14fitclubmvc.repository.BookingRepository;
import org.spring.core.learn.spring14fitclubmvc.repository.FitnessClassRepository;
import org.spring.core.learn.spring14fitclubmvc.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
        return List.of();
    }

    @Override
    public void bookClass(Long memberId, Long classId) {
    }

    @Override
    public List<Booking> getMemberBookings(Long memberId) {
        return bookingRepository.findMemberBookingWithDetails(memberId);
    }

    @Override
    public Map<String, Long> getConfirmedBookingCountByClassName() {
        return Map.of();
    }
}
