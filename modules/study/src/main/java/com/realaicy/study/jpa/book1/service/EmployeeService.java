package com.realaicy.study.jpa.book1.service;

import com.realaicy.study.jpa.book1.entity.Department;
import com.realaicy.study.jpa.book1.entity.Employee;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collection;

public class EmployeeService {
    protected EntityManager em;

    public EmployeeService(EntityManager em) {
        this.em = em;
    }

    public Employee

    createEmployee(String name, long salary) {
        Employee emp = new Employee();
        emp.setName(name);
        emp.setSalary(salary);
        em.persist(emp);

        return emp;
    }

    public Employee findEmployee(int id) {
        return em.find(Employee.class, id);
    }

    public Employee setEmployeeDepartment(int empId, int deptId) {
        Employee emp = em.find(Employee.class, empId);
        Department dept = em.find(Department.class, deptId);
        emp.setDepartment(dept);
        return emp;
    }

    public Collection<Employee> findAllEmployees() {
        Query query = em.createQuery("SELECT e FROM Employee e");
        return (Collection<Employee>) query.getResultList();
    }

    public Department createDepartment(String name) {
        Department dept = new Department();
        dept.setName(name);
        em.persist(dept);

        return dept;
    }

    public Collection<Department> findAllDepartments() {
        Query query = em.createQuery("SELECT d FROM Department d");
        return (Collection<Department>) query.getResultList();
    }
}
