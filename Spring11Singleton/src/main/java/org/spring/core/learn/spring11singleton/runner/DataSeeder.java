package org.spring.core.learn.spring11singleton.runner;

import org.spring.core.learn.spring11singleton.entity.Employee;
import org.spring.core.learn.spring11singleton.repository.EmployeeRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements ApplicationRunner {

private final EmployeeRepository empRepo;

    public DataSeeder(EmployeeRepository empRepo) {
        this.empRepo = empRepo;
    }

    @Override
    public void run(ApplicationArguments args)
    {
        System.out.println("Seeding intitial empoyee data. .. . . . ");


        empRepo.save(new Employee(null ,"Asha" , "Engineering", 75000 ));
        empRepo.save(new Employee(null, "Ravi", "Sales", 55000));

        // Practice reading CLI args, e.g. run with --department=Engineering

        if (args.containsOption("department")){
            String dept = args.getOptionValues("department").get(0);   //getOptionValues return Type  is: List<String>  and get(int index) to get the first element from list
            System.out.println("Filtering by departement: " + dept);
            empRepo.findByDepartement(dept).forEach(System.out::println);

        }


    }

}
