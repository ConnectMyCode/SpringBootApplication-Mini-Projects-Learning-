package org.spring.core.learn.spring14fitclubmvc.service;

import org.spring.core.learn.spring14fitclubmvc.dto.request.MemberRegistrationRequest;
import org.spring.core.learn.spring14fitclubmvc.entity.FitnessClass;
import org.spring.core.learn.spring14fitclubmvc.entity.Member;

import java.util.Set;

/*
     * ================= SPRING CORE REVISION:in detail explanation in .md file  =================
     * Coding to an interface: AuthController depends on the ABSTRACTION
     * (MemberService), not the concrete class. Spring's IoC container
     * decides at startup which concrete bean (MemberServiceImpl) to hand to
     * AuthController - "Inversion of Control". It's also what lets us swap
     * in a Mockito fake in tests via @MockBean - the controller can't tell
     * the difference between the real bean and a test double.
     */
public interface MemberService {


    Member register(MemberRegistrationRequest request);


    Member login(String username , String rawPassword);



    // Uses the ManyToMany UNIDIRECTIONAL relationship (Member.favoriteClasses)
    void addFavoriteClass(Long memberId, Long classId);

    void removeFavoriteClass(Long memberId, Long classId);

    Set<FitnessClass> getFavoriteClasses(Long memberId);




}