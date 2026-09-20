package org.spring.core.learn.spring14fitclubmvc.config;

import org.jspecify.annotations.NonNull;
import org.spring.core.learn.spring14fitclubmvc.entity.ClassSchedule;
import org.spring.core.learn.spring14fitclubmvc.entity.FitnessClass;
import org.spring.core.learn.spring14fitclubmvc.entity.Trainer;
import org.spring.core.learn.spring14fitclubmvc.repository.FitnessClassRepository;
import org.spring.core.learn.spring14fitclubmvc.repository.TrainerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.util.Set;

@Configuration
public class DataSeeder implements CommandLineRunner {

        private final FitnessClassRepository fitnessClassRepository;
        private final TrainerRepository trainerRepository;


    public DataSeeder(FitnessClassRepository fitnessClassRepository, TrainerRepository trainerRepository) {
            this.fitnessClassRepository = fitnessClassRepository;
            this.trainerRepository = trainerRepository;
    }



    @Override
    public void run(String @NonNull ... args) throws Exception {

        if (fitnessClassRepository.count() > 0) {
            return; // already seeded - don't duplicate on every restart
        }

        Trainer asha = trainerRepository.save(new Trainer(null, "Asha Patel", "Yoga & Flexibility"));
        Trainer ravi = trainerRepository.save(new Trainer(null, "Ravi Kumar", "HIIT & Conditioning"));
        Trainer meera = trainerRepository.save(new Trainer(null, "Meera Iyer", "Strength Training"));

        FitnessClass yoga = new FitnessClass(null, "Morning Yoga", "Gentle flow to start your day", 15);
        yoga.setSchedule(new ClassSchedule(null, "MONDAY", LocalTime.of(7, 0), LocalTime.of(8, 0)));
        yoga.setTrainers(Set.of(asha)); // owning side of the ManyToMany - creates the join table rows

        FitnessClass hiit = new FitnessClass(null, "HIIT Blast", "High intensity interval training", 12);
        hiit.setSchedule(new ClassSchedule(null, "WEDNESDAY", LocalTime.of(18, 0), LocalTime.of(19, 0)));
        hiit.setTrainers(Set.of(ravi, meera)); // a class with TWO trainers - genuine many-to-many

        FitnessClass strength = new FitnessClass(null, "Strength Basics", "Introductory weight training", 8);
        strength.setSchedule(new ClassSchedule(null, "FRIDAY", LocalTime.of(17, 0), LocalTime.of(18, 0)));
        strength.setTrainers(Set.of(meera));

        // cascade = ALL on FitnessClass.schedule means saving the class
        // also saves its (new, unsaved) ClassSchedule automatically.
        fitnessClassRepository.save(yoga);
        fitnessClassRepository.save(hiit);
        fitnessClassRepository.save(strength);
    }


    }

