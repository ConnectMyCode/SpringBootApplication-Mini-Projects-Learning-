package org.spring.core.learn.spring14fitclubmvc.repository;

import org.spring.core.learn.spring14fitclubmvc.entity.FitnessClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FitnessClassRepository extends JpaRepository<FitnessClass, Long> {
List<FitnessClass> findByClassNameContainingIgnoreCase(String keywword);



}
