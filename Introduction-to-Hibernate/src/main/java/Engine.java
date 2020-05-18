import entities.*;
import org.w3c.dom.ls.LSOutput;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;
import java.util.Scanner;

public class Engine implements Runnable {
    private final EntityManager manager;
    private final Scanner scanner;

    public Engine(EntityManager manager, Scanner scanner) {
        this.manager = manager;
        this.scanner = scanner;
    }


    @Override
    public void run() {
        System.out.println("Choose the task you want to test(from 2 to 13, Choose 0 to exit (Default 0)): ");
        String task = scanner.nextLine();
        task = task.trim().isEmpty() ? "0" : task;
        while (!task.equals("0")) {

            switch (task) {
                case "1": {
                    System.out.println("Already accomplished (Hopefully :) )");
                    break;
                }
                case "2": {
                    System.out.println("Task 2: ");
                    try {
                        removeObjectsTask();
                    } catch (NoResultException e) {
                        System.out.println("No entities found in the Database!");
                    }
                    System.out.println("End of task 2 \n*************************");
                    break;
                }
                case "3": {
                    System.out.println("Task 3: ");
                    try {
                        containsEmployeeTask();
                    } catch (NoResultException e) {
                        System.out.println("No");
                    }
                    System.out.println("End of task 3 \n*************************");
                    break;
                }
                case "4": {
                    System.out.println("Task 4: ");
                    try {
                        employeesWithSalaryOver50000Task();
                    } catch (NoResultException e) {
                        System.out.println("No entities found in the Database!");
                    }
                    System.out.println("End of task 4 \n*************************");
                    break;
                }
                case "5": {
                    System.out.println("Task 5: ");
                    try {
                        employeesFromDepartmentTask();
                    } catch (NoResultException e) {
                        System.out.println("No entities found in the Database!");
                    }
                    System.out.println("End of task 5 \n*************************");
                    break;
                }
                case "6": {
                    System.out.println("Task 6: ");
                    try {
                        addNewAddressAndUpdateEmployee();
                    } catch (NoResultException e) {
                        System.out.println("No entities found in the Database!");
                    }
                    System.out.println("End of task 6 \n*************************");
                    break;
                }
                case "7": {
                    System.out.println("Task 7: ");
                    try {
                        addressesWithEmployeeCountTask();
                    } catch (NoResultException e) {
                        System.out.println("No entities found in the Database!");
                    }
                    System.out.println("End of task 7 \n*************************");
                    break;
                }
                case "8": {
                    System.out.println("Task 8: ");
                    try {
                        getEmployeeWithProjectTask();
                    } catch (NoResultException e) {
                        System.out.println("No entities found in the Database!");
                    }
                    System.out.println("End of task 8 \n*************************");
                    break;
                }
                case "9": {
                    System.out.println("Task 9: ");
                    try {
                        latest10Projects();
                    } catch (NoResultException e) {
                        System.out.println("No entities found in the Database!");
                    }
                    System.out.println("End of task 9 \n*************************");
                    break;
                }
                case "10": {
                    System.out.println("Task 10: ");
                    try {
                        increaseSalariesTask();
                    } catch (NoResultException e) {
                        System.out.println("No entities found in the Database!");
                    }
                    System.out.println("End of task 10 \n*************************");
                    break;
                }
                case "11": {
                    System.out.println("Task 11: ");
                    try {
                        removeTownsTask();
                    } catch (NoResultException e) {
                        System.out.println("No entities found in the Database!");
                    }
                    System.out.println("End of task 11 \n*************************");
                    break;
                }
                case "12": {
                    System.out.println("Task 12: ");
                    try {
                        findEmployeesByFirstNameTask();
                    } catch (NoResultException e) {
                        System.out.println("No entities found in the Database!");
                    }
                    System.out.println("End of task 12 \n*************************");
                    break;
                }
                case "13": {
                    System.out.println("Dear colleague, this task is not working properly, it is showing results,\n" +
                            "but they differ from the example.\n" +
                            "Interesting, in Workbench using plain SQL I had the same results,\n" +
                            "maybe my database was not uploaded correctly.\n" +
                            "Maybe it will work on your computer. Would you like to test it? (Y/N): ");
                    if (scanner.nextLine().toLowerCase().equals("y")) {
                        System.out.println("Task 13: ");
                        try {
                            employeesMaximumSalaries();
                        } catch (NoResultException e) {
                            System.out.println("No entities found in the Database!");
                        }
                        System.out.println("End of task 13 \n*************************");
                    }
                    break;
                }
                default: {
                    System.out.println("Wrong selection.");
                    break;
                }
            }
            System.out.println("Choose next task(if you want to exit press 0): ");
            task = scanner.nextLine();
            task = task.trim().isEmpty() ? "0" : task;
        }
        System.out.println("You have exited the program!");
    }

    private void removeObjectsTask() {
        List<Town> towns = this.manager.createQuery("SELECT t from Town as t " +
                "where length(t.name) > 5 ", Town.class).getResultList();
        this.manager.getTransaction().begin();
        for (Town town : towns) {
            this.manager.detach(town);
            town.setName(town.getName().toLowerCase());
            this.manager.merge(town);
        }
        this.manager.flush();
        this.manager.getTransaction().commit();
        this.manager.createQuery("SELECT t from Town as t", Town.class)
                .getResultStream().forEach(t -> System.out.println(t.getName()));
    }

    private void containsEmployeeTask() {
        System.out.println("Enter employee's first and second names: ");
        String employeeFullName = scanner.nextLine();

        Employee employee = this.manager.createQuery("select e from Employee as e" +
                " where concat(e.firstName, ' ', e.lastName) =: param ", Employee.class)
                .setParameter("param", employeeFullName).getSingleResult();
        System.out.println("Yes");

    }

    private void employeesWithSalaryOver50000Task() {
        this.manager.createQuery("select e from Employee as e where e.salary > 50000", Employee.class)
                .getResultStream().forEach(e -> System.out.println(e.getFirstName()));
    }

    private void employeesFromDepartmentTask() {
        String department = "Research and Development";
        this.manager.createQuery("select e from Employee  as e" +
                " where e.department.name =: param " +
                "order by e.salary asc, e.id asc", Employee.class).setParameter("param", department)
                .getResultStream().forEach(e -> System.out.printf("%s %s from %s - $%s%n",
                e.getFirstName(), e.getLastName(), e.getDepartment().getName(), PrivateFormatter.format(e.getSalary())));
    }

    private void addNewAddressAndUpdateEmployee() {
        System.out.println("Select last name of the employee: ");
        String lastName = scanner.nextLine();
        List<Employee> employees = this.manager.createQuery("select e from Employee as e " +
                "where e.lastName =: param", Employee.class).setParameter("param", lastName).getResultList();

        Address newAddress = addNewAddressToAddressesEntity();

        this.manager.getTransaction().begin();
        for (Employee current : employees) {
            this.manager.detach(current);
            current.setAddress(newAddress);
            this.manager.merge(current);
        }

        this.manager.flush();
        this.manager.getTransaction().commit();

        this.manager.createQuery("select e from Employee as e " +
                "where e.lastName =: param", Employee.class).setParameter("param", lastName)
                .getResultStream()
                .forEach(e -> System.out.printf("%s %s %s%n", e.getFirstName(), e.getLastName(), e.getAddress().getText()));
    }

    private Address addNewAddressToAddressesEntity() {
        Address newAddress = new Address();
        newAddress.setText("Vitoshka 15");
        this.manager.getTransaction().begin();
        this.manager.persist(newAddress);
        this.manager.flush();
        this.manager.getTransaction().commit();
        return newAddress;
    }

    private void addressesWithEmployeeCountTask() {
        this.manager.createQuery("select a from Address as a" +
                " order by a.employees.size desc", Address.class).setMaxResults(10).getResultStream()
                .forEach(a -> System.out.printf("%d %s, %s - %d employees%n",
                        a.getId(), a.getText(), a.getTown().getName(), a.getEmployees().size()));
    }

    private void getEmployeeWithProjectTask() {
        System.out.println("Write employee's ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Employee employee = this.manager.createQuery("select e from Employee as" +
                " e where e.id =: param", Employee.class).setParameter("param", id).getSingleResult();
        System.out.printf("%s %s - %s%n", employee.getFirstName(), employee.getLastName(), employee.getDepartment().getName());
        employee.getProjects().stream()
                .sorted((a, b) -> a.getName().compareTo(b.getName()))
                .forEach(p -> System.out.printf("      %s      %n", p.getName()));
    }


    private void latest10Projects() {
        List<Project> projects = this.manager.createQuery("select p from Project as p order by p.startDate desc ", Project.class)
                .setMaxResults(10).getResultList();

        projects.stream().sorted((a, b) -> a.getName().compareTo(b.getName())).forEach(p -> System.out.printf("Project name: %s%n" +
                        "        Project Description: %s%n" +
                        "        Project Start Date:%s%n" +
                        "        Project End Date: %s%n",
                p.getName(), p.getDescription(), p.getStartDate(), p.getEndDate()));
    }

    private void increaseSalariesTask() {
        List<Employee> employees = this.manager.createQuery("select e from Employee as e " +
                "where e.department.name in ('Engineering', 'Tool Design', 'Marketing', 'Information Services' )", Employee.class)
                .getResultList();
        this.manager.getTransaction().begin();
        for (Employee employee : employees) {
            this.manager.detach(employee);
            employee.setSalary(employee.getSalary().multiply(new BigDecimal(1.12)));
            this.manager.merge(employee);
        }
        this.manager.flush();
        this.manager.getTransaction().commit();


        this.manager.createQuery("select e from Employee as e " +
                "where e.department.name in ('Engineering', 'Tool Design', 'Marketing', 'Information Services' )", Employee.class)
                .getResultList().forEach(e -> System.out.printf("%s %s ($%s)%n",
                e.getFirstName(), e.getLastName(), PrivateFormatter.format(e.getSalary())));
    }

    private void removeTownsTask() {
        System.out.println("Write the name of the town: ");
        String town = scanner.nextLine();
        removeTheAddressesOfTheEmployeesLivingInTheTown(town);
        int removedAddresses = removeAddressesInTheTown(town);
        removeTown(town);
        if (removedAddresses > 1) {
            System.out.printf("%d addresses in %s deleted%n", removedAddresses, town);
        } else {
            System.out.printf("%d address in %s deleted%n", removedAddresses, town);
        }
    }

    private void removeTown(String town) {
        Town toRemove = this.manager.createQuery("select t from Town as t where t.name =: param", Town.class)
                .setParameter("param", town)
                .setParameter("param", town).getSingleResult();
        this.manager.getTransaction().begin();
        this.manager.remove(toRemove);
        this.manager.getTransaction().commit();
    }

    private int removeAddressesInTheTown(String town) {
        List<Address> addresses = this.manager.createQuery("select a from Address as a " +
                "where a.town.name =: param", Address.class).setParameter("param", town).getResultList();

        this.manager.getTransaction().begin();
        addresses.forEach(this.manager::remove);
        this.manager.getTransaction().commit();
        return addresses.size();
    }

    private void removeTheAddressesOfTheEmployeesLivingInTheTown(String town) {
        this.manager.getTransaction().begin();
        List<Employee> employees = this.manager.createQuery("select e from Employee as e " +
                "where e.address.town.name =: param", Employee.class)
                .setParameter("param", town).getResultList();
        for (Employee employee : employees) {
            this.manager.detach(employee);
            employee.setAddress(null);
            this.manager.merge(employee);
        }
        this.manager.flush();
        this.manager.getTransaction().commit();
    }

    private void findEmployeesByFirstNameTask() {
        System.out.println("Select employee's first name pattern:");
        String pattern = scanner.nextLine() + "%";

        this.manager.createQuery("select e from Employee as e where e.firstName like : param", Employee.class)
                .setParameter("param", pattern).getResultStream()
                .forEach(e -> System.out.printf("%s %s - %s - ($%s)%n",
                        e.getFirstName(), e.getLastName(), e.getJobTitle(), PrivateFormatter.format(e.getSalary())));
    }

    private void employeesMaximumSalaries() {
        this.manager.createQuery("select e from Employee as e " +
                "where e.salary = (select max(em.salary) from Employee as em " +
                "where em.department.id = e.department.id)" +
                "and (e.salary < 30000 or e.salary > 70000)" +
                "group by e.department.name", Employee.class).getResultStream()
                .forEach(e -> System.out.printf("%s %.2f%n", e.getDepartment().getName(), e.getSalary()));
    }
}

