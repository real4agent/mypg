package com.realaicy.study.jpa.book1.test;

import com.realaicy.study.jpa.book1.entity.Department;
import com.realaicy.study.jpa.book1.entity.Employee;
import com.realaicy.study.jpa.book1.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ManyToOneTest {
    static Logger logger = LoggerFactory.getLogger(ManyToOneTest.class);

    EntityManagerFactory emf;
    EntityManager em;

    public static void main(String[] args) {

    }

    @Test
    public void testCreateDep() {
        logger.info("-------------------testCreateDep-------------------------------");
        EmployeeService employeeService = new EmployeeService(em);

        em.getTransaction().begin();
        Department dept = employeeService.createDepartment(
                "test");
        em.getTransaction().commit();
        System.out.println("Persisted " + dept);

    }

    @Test
    public void testCreateEmp() {
        logger.info("-------------------testCreateEmp-------------------------------");
        EmployeeService employeeService = new EmployeeService(em);

        em.getTransaction().begin();
        Employee emp = employeeService.createEmployee("John Doe", 45000);

        em.getTransaction().commit();
        System.out.println("Persisted " + emp);

    }

    @Test
    public void testSetDepForEmp() {
        logger.info("-------------------testSetDepForEmp-------------------------------");

        EmployeeService employeeService = new EmployeeService(em);

        em.getTransaction().begin();
        employeeService.setEmployeeDepartment(1, 2);

        em.getTransaction().commit();

    }

    @Test
    public void testUpdateDepForEmp() {
        logger.info("-------------------testUpdateDepForEmp-------------------------------");

        EmployeeService employeeService = new EmployeeService(em);

        em.getTransaction().begin();

        Employee emp = employeeService.findEmployee(1);
        Department newDep = new Department();
        newDep.setId(3);
        emp.setDepartment(newDep);
        Employee newEmp = em.merge(emp);

        //emp = employeeService.setEmployeeDepartment(1, 2);

        Employee newEmp2 = employeeService.findEmployee(1);
        em.getTransaction().commit();
        Employee newEmp3 = employeeService.findEmployee(1);
        // em.clear();
        //em.detach(newEmp);
        Employee newEmp4 = employeeService.findEmployee(1);


        em.refresh(newEmp);

        Employee newEmp5 = employeeService.findEmployee(1);
    }

    @BeforeMethod
    private void init() {
        emf =
                Persistence.createEntityManagerFactory("EmployeeService");
        em = emf.createEntityManager();
    }

    @AfterMethod
    private void destoy() {
        em.close();
        emf.close();
    }
}
