package ui;

import domain.validators.ValidatorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

public class Console {
    private service.ClientService studentService;

    public Console(service.ClientService studentService) {
        this.studentService = studentService;
    }

    public void runConsole() {
//        addStudents();
        printAllClients();
        filterClients();
    }

    private void filterClients() {
        System.out.println("filtered clients (name containing 's2'):");
        Set<domain.Client> students = studentService.filterClientsByName("s2");
        students.stream().forEach(System.out::println);
    }

    private void printAllClients() {
        Set<domain.Client> students = studentService.getAllClients();
        students.stream().forEach(System.out::println);
    }

    private void addClients() {
        while (true) {
            domain.Client student = readClient();
            if (student == null || student.getId() < 0) {
                break;
            }
            try {
                studentService.addClient(student);
            } catch (ValidatorException e) {
                e.printStackTrace();
            }
        }
    }

    private domain.Client readClient() {
        System.out.println("Read client {id,serialNumber, name}");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            Long id = Long.valueOf(bufferRead.readLine());
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
