package org.spring.core.learn.spring14fitclubmvc.service;


import jakarta.transaction.Transactional;
import org.spring.core.learn.spring14fitclubmvc.dto.request.MemberRegistrationRequest;
import org.spring.core.learn.spring14fitclubmvc.entity.FitnessClass;
import org.spring.core.learn.spring14fitclubmvc.entity.Member;
import org.spring.core.learn.spring14fitclubmvc.exception.InvalidCredentialsException;
import org.spring.core.learn.spring14fitclubmvc.exception.ResourceNotFoundException;
import org.spring.core.learn.spring14fitclubmvc.exception.UserAlreadyExistsException;
import org.spring.core.learn.spring14fitclubmvc.repository.FitnessClassRepository;
import org.spring.core.learn.spring14fitclubmvc.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;




/*
 * ================= SPRING CORE REVISION: STEREOTYPES =================
 * @Service is one of Spring's "stereotype" annotations (alongside
 * @Component, @Repository, @Controller) - all four mark a class to be
 * picked up by @ComponentScan and turned into a Spring-managed BEAN.
 */

@Service
public class MemberServiceImpl implements MemberService{

/*@Autowired is implicit. becuase there is only 1 constructor ...*/
    private final MemberRepository memberRepository;
    private final FitnessClassRepository fitnessClassRepository;



    /*
     * ================= SPRING CORE REVISION: DEPENDENCY INJECTION =================
     * Constructor injection - Spring sees this is the ONLY constructor,
     * so @Autowired is implicit. The IoC container builds both
     * repository beans and passes them in here at startup.
     */

    public MemberServiceImpl(MemberRepository memberRepository, FitnessClassRepository fitnessClassRepository) {
        this.memberRepository = memberRepository;
        this.fitnessClassRepository = fitnessClassRepository;
    }

    @Override
    public Member register(MemberRegistrationRequest request) {
        if(memberRepository.findByUsername(request.getUsername()).isPresent()){
            throw new UserAlreadyExistsException("Username already taken: "+request.getUsername());
        }

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered: " + request.getEmail());
        }
        Member member = new Member(null, request.getUsername(), request.getEmail(), request.getPassword(), LocalDateTime.now());
        return memberRepository.save(member);
    }


    @Override
    public Member login(String username, String rawPassword) {
        Member member = memberRepository.searchByUsername(username)
                .orElseThrow( ()-> new InvalidCredentialsException("Invalid username or password") );

        if(!member.getPassword().equals(rawPassword)){
            throw new InvalidCredentialsException("Invalid username or password");
        }
        return member;
    }




    /*
     * ================= WORKING WITH THE @ManyToMany RELATIONSHIP =================
     * Because Member.favoriteClasses is a Set, adding the SAME class
     * twice is naturally a no-op - no extra "already favorited" check
     * needed, unlike bookClass()'s explicit AlreadyBookedException check.
     */
    @Transactional
    @Override
    public void addFavoriteClass(Long memberId, Long classId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found: " + memberId));
        FitnessClass fitnessClass = fitnessClassRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + classId));

        member.getFavoriteClasses().add(fitnessClass);
        memberRepository.save(member);
    }

    @Transactional
    @Override
    public void removeFavoriteClass(Long memberId, Long classId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found: " + memberId));

        member.getFavoriteClasses().removeIf(fc -> fc.getId().equals(classId));
        memberRepository.save(member);
    }

    @Override
    public Set<FitnessClass> getFavoriteClasses(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found: " + memberId));
        return member.getFavoriteClasses();
    }
}
