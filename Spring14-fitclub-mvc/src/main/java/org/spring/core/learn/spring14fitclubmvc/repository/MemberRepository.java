package org.spring.core.learn.spring14fitclubmvc.repository;


import org.spring.core.learn.spring14fitclubmvc.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Boolean> findByUsername(String username);

    Optional<Member> searchByUsername(String username);

    boolean existsByEmail(String email);
}
