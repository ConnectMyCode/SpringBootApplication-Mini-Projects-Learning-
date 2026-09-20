package org.spring.core.learn.spring14fitclubmvc.repository;

import org.spring.core.learn.spring14fitclubmvc.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer,Long> {

}
