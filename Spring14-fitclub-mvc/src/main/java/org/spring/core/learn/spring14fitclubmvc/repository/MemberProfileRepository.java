package org.spring.core.learn.spring14fitclubmvc.repository;

import org.spring.core.learn.spring14fitclubmvc.entity.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {

Optional<MemberProfile> findByMember_Id(Long memberId);

}