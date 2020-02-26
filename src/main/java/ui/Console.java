package ui;

import domain.validators.ValidatorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

/**
 * @author radu.
 */
public class Console {
    private service.ClientService studentService;

    public Console(service.ClientService studentService) {
        this.studentService = studentService;
    }

    public void runConsole() {
//        addStudents();
        printAllStudents();
        filterStudents();
    }

    private void filterStudents() {
        System.out.println("filtered students (name containing 's2'):");
        Set<domain.Client> students = studentService.filterStudentsByName("s2");
        students.stream().forEach(System.out::println);
    }

    private void printAllStudents() {
        Set<domain.Client> students = studentService.getAllStudents();
        students.stream().forEach(System.out::println);
    }

    private void addStudents() {
        while (true) {
            domain.Client student = readStudent();
            if (student == null || student.getId() < 0) {
                break;
            }
            try {
                studentService.addStudent(student);
            } catch (ValidatorException e) {
                e.printStackTrace();
            }
        }
    }

    private domain.Client readStudent() {
        System.out.println("Read student {id,serialNumber, name, group}");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            Long id = Long.valueOf(bufferRead.readLine());// ...
            String serialNumber = bufferRead.readLine();
            String name = bufferRead.readLine();

            domain.Client student = new domain.Client(serialNumber, name);
            student.setId(id);

            return student;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
