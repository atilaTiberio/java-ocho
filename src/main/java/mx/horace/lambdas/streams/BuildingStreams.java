package mx.horace.lambdas.streams;

import mx.horace.lambdas.Repositories;
import mx.internetbrands.spring.beana.db.entity.jpa.EmployeesEntity;
import mx.internetbrands.spring.beana.db.entity.jpa.SalariesEntity;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

public class BuildingStreams {

    public static void main(String[] args) {

        List<EmployeesEntity> employeesEntities = Repositories.employees();

        // it takes all the elements that doesn't fulfill such condition
        long total = employeesEntities.stream()
                .dropWhile(employeesEntity -> employeesEntity.getEmpNo() < 10110)
                .count();

        // it stops when it finds the first element that brokes the condition
        long total2 = employeesEntities.stream()
                .takeWhile(employeesEntity -> employeesEntity.getEmpNo() < 10110)
                .count();

        // it iterates the whole stream
        long total3 = employeesEntities.stream()
                .filter(employeesEntity -> employeesEntity.getEmpNo() < 10110)
                .count();

        employeesEntities.stream()
                .filter(employeesEntity -> employeesEntity.getGender().equals("F"))
                .findAny()
                .ifPresent(employeesEntity -> System.out.println(employeesEntity));

        boolean any = employeesEntities.stream()
                .anyMatch(employeesEntity -> employeesEntity.getGender().equals("F"));

        List<String> femaleNames = employeesEntities.stream()
                .filter(employeesEntity -> employeesEntity.getGender().equals("M"))
                .map(EmployeesEntity::getFirstName)
                .limit(15)
                .sorted()
                .collect(Collectors.toList());

        System.out.println(femaleNames);

        System.out.println(String.format("%d / %d / %d", total, total2, total3));

        List<String> genders= employeesEntities.stream()
                .limit(300)
                .map(EmployeesEntity::getGender)
                .distinct()
                .collect(Collectors.toList());
        System.out.println(genders);


        OptionalInt a=employeesEntities.stream()
                .mapToInt(e -> e.getFirstName().length())
                .max();


        System.out.println(a.orElse(-1));

        List<SalariesEntity> salariesEntities= Repositories.salaries();


       Map<Integer,List<Integer>> salByEmp=salariesEntities.stream()
                .collect(
                        groupingBy(SalariesEntity::getEmpNo,mapping(SalariesEntity::getSalary,toList())));

        System.out.println(salByEmp);

       Map<Boolean,Map<Integer,List<Integer>>> resPartition=salariesEntities.stream()
                .collect(partitioningBy((salariesEntity -> salariesEntity.getSalary()<=50000),
                        groupingBy(SalariesEntity::getEmpNo,mapping(SalariesEntity::getEmpNo,toList()))
        ));
        System.out.println(resPartition);


    }


}
