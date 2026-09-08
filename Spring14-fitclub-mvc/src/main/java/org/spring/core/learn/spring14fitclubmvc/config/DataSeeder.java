package org.spring.core.learn.spring14fitclubmvc.config;

import org.springframework.boot.CommandLineRunner;

public class DataSeeder implements CommandLineRunner {

        private final FitnessClassRepository fitnessClassRepository;
        private final TrainerRepository trainerRepository;


    public DataSeeder(FitnessClassRepository fitnessClassRepository, TrainerRepository trainerRepository) {
            this.fitnessClassRepository = fitnessClassRepository;
            this.trainerRepository = trainerRepository;
    }



    @Override
    public void run(String... args) throws Exception {



    }
}
