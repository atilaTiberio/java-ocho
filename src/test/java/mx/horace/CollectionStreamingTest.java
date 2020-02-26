package mx.horace;

import mx.internetbrands.spring.beana.db.entity.jpa.SalariesEntity;
import mx.internetbrands.spring.beana.db.repository.jpa.EmployeeRepository;
import mx.internetbrands.spring.beana.db.repository.jpa.SalariesRepository;
import mx.internetbrands.spring.config.SpringJavaConfig;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringJavaConfig.class)
public class CollectionStreamingTest {

    private Logger logger= Logger.getLogger(CollectionStreamingTest.class);

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    SalariesRepository salariesRepository;


    @Test
    public void repositoriesNotNull(){

        assertNotNull(employeeRepository);
        assertNotNull(employeeRepository);
    }

    @Test
    public void testSalariesPartition(){

        Map<Boolean, List<Integer>> res=salariesRepository
                .getAllSalaries(PageRequest.of(0,1000))
                .stream().collect(groupingBy(salary -> salary.getSalary()<=60000, mapping(SalariesEntity::getSalary,toList())));


        assertNotNull(res);
        assertTrue(res.get(false).get(0)>60000);
        assertTrue(res.get(true).get(0)<=60000);
    }

    @Test
    public void mapAndReduceEmpSalaries(){

        List<SalariesEntity> salariesEntities=salariesRepository.getAllSalaries(PageRequest.of(0,1001));
        Long totalEmp=salariesEntities
                .stream()
                .map(SalariesEntity::getEmpNo)
                .distinct().count();

        Map<Integer,List<SalariesEntity>> gettingAllSalariesEntitiesByEmpNo=salariesEntities.stream()
                .collect(groupingBy(SalariesEntity::getEmpNo));

        Map<Integer,List<Integer>> gettingAllSalariesByEmpNo=salariesEntities.stream()
                .collect(groupingBy(SalariesEntity::getEmpNo, mapping(SalariesEntity::getSalary,toList())));

        //groupingBy with reducing
        Map<Integer,Integer> salarySumByEmployee= salariesEntities.stream()
                .collect(groupingBy(SalariesEntity::getEmpNo,reducing(0,SalariesEntity::getSalary,Integer::sum)));

        //groupingby replaced with toMap getting max salary
        Map<Integer,Integer> salaryMaxByEmployee= salariesEntities.stream()
                .collect(toMap(SalariesEntity::getEmpNo,SalariesEntity::getSalary,Integer::max));

        //using comparators for getting max
        Map<Integer,Optional<SalariesEntity>> salaryMaxByEmployeeNoReducing= salariesEntities.stream()
                .collect(groupingBy(SalariesEntity::getEmpNo,maxBy(comparing(SalariesEntity::getSalary))));

        Map<Integer,Integer> salaryMaxByEmployeeCollectingAndThen= salariesEntities.stream()
                .collect(groupingBy(SalariesEntity::getEmpNo,collectingAndThen(maxBy(comparing(SalariesEntity::getSalary)),opt -> opt.get().getSalary())));

//
        logger.info(gettingAllSalariesEntitiesByEmpNo);
        logger.info(gettingAllSalariesByEmpNo);
        logger.info(salarySumByEmployee);
        logger.info(salaryMaxByEmployee);
        logger.info(salaryMaxByEmployeeNoReducing);
        logger.info(salaryMaxByEmployeeCollectingAndThen);

        assertTrue(totalEmp==salarySumByEmployee.size());
        assertTrue(salaryMaxByEmployee.equals(salaryMaxByEmployeeCollectingAndThen));
    }

    @Test
    public void summarizing(){
        List<SalariesEntity> salariesEntities=salariesRepository.getAllSalaries(PageRequest.of(0,1001));

       Map<Integer,IntSummaryStatistics> res=salariesEntities
                .stream()
                .collect(groupingBy(SalariesEntity::getEmpNo, summarizingInt(SalariesEntity::getSalary)));

       List<SalariesEntity> top10=salariesRepository.findTop10BySalaryIsNotNull();

       assertNotNull(res);
       assertThat(res.entrySet().stream().findAny().get().getValue(),instanceOf(IntSummaryStatistics.class));
       assertEquals(10,top10.size());

    }


}
