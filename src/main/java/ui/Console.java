package ro.ubb.catalog.ui;

import ro.ubb.catalog.domain.validators.ValidatorException;
import ro.ubb.catalog.service.StudentService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

/**
 * @author radu.
 */
public class Console {
    private StudentService studentService;

    public Console(StudentService studentService) {
        this.studentService = studentService;
    }

    public void runConsole() {
//        addStudents();
        printAllStudents();
        filterStudents();
    }

    private void filterStudents() {
        System.out.println("filtered students (name containing 's2'):");
        Set<ro.ubb.catalog.domain.Client> students = studentService.filterStudentsByName("s2");
        students.stream().forEach(System.out::println);
    }

    private void printAllStudents() {
        Set<ro.ubb.catalog.domain.Client> students = studentService.getAllStudents();
        students.stream().forEach(System.out::println);
    }

    private void addStudents() {
        while (true) {
            ro.ubb.catalog.domain.Client student = readStudent();
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

    private ro.ubb.catalog.domain.Client readStudent() {
        System.out.println("Read student {id,serialNumber, name, group}");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            Long id = Long.valueOf(bufferRead.readLine());// ...
            String serialNumber = bufferRead.readLine();
            String name = bufferRead.readLine();
            int group = Integer.parseInt(bufferRead.readLine());// ...

            ro.ubb.catalog.domain.Client student = new ro.ubb.catalog.domain.Client(serialNumber, name, group);
            student.setId(id);

            return student;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
