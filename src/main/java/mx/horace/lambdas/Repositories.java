package mx.horace.lambdas;

import mx.internetbrands.spring.beana.db.entity.jpa.EmployeesEntity;
import mx.internetbrands.spring.beana.db.entity.jpa.SalariesEntity;
import mx.internetbrands.spring.beana.db.repository.jpa.EmployeeRepository;
import mx.internetbrands.spring.beana.db.repository.jpa.SalariesRepository;
import mx.internetbrands.spring.config.SpringJavaConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class Repositories {


    private Repositories(){

    }

    public static List<EmployeesEntity> employees(){

        ApplicationContext ctx= new AnnotationConfigApplicationContext(SpringJavaConfig.class);

        EmployeeRepository employeeRepository= (EmployeeRepository) ctx.getBean("employeeRepository");
        List<EmployeesEntity> res= employeeRepository.getAllEmployees();

        return res;
    }

    public static List<SalariesEntity> salaries(){

        ApplicationContext ctx= new AnnotationConfigApplicationContext(SpringJavaConfig.class);

        SalariesRepository salariesRepository= (SalariesRepository) ctx.getBean("salariesRepository");
        List<SalariesEntity> res= salariesRepository.getAllSalaries(PageRequest.of(0,1000));

        return res;
    }
}
