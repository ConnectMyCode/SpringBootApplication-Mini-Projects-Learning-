package org.spring.core.learn.spring14fitclubmvc.service;

import org.spring.core.learn.spring14fitclubmvc.entity.Booking;
import org.spring.core.learn.spring14fitclubmvc.entity.FitnessClass;

import java.util.List;
import java.util.Map;

public interface ClassBookingService {

    List<FitnessClass> listAllClasses();

    Booking bookClass(Long memberId, Long classId);

    List<Booking> getMemberBookings(Long memberId);

    Map<String, Long> getConfirmedBookingCountByClassName();


}
