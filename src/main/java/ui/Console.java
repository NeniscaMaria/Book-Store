package ui;

import domain.validators.ClientValidator;
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
        boolean finished=false;
        while(!finished){
            printChoices();
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            int choice=-1;
            try {
                choice = Integer.parseInt(bufferRead.readLine());
            }catch(IOException ex){
                System.out.println(ex.getStackTrace());
            }
            if(isChoiceOK(choice))
                switch (choice) {
                    case 0:
                        finished=true;
                        break;
                    case 1:
                        addClient();
                        break;
                    case 2:
                        printAllClients();
                        break;
                    case 3:
                        filterClients();
                        break;
                    default:
                        break;
                }
        }

    }
    boolean isChoiceOK(int choiceToCheck){
        return choiceToCheck>=0 && choiceToCheck<4;
    }
    private void printChoices(){
        System.out.println("\nChoose one from below:");
        System.out.println("0.Exit");
        System.out.println("1.Add new client.");
        System.out.println("2.Add new book.");
        System.out.println("3.Show all clients.");
        System.out.println("4.Show all books.");
        System.out.println("5.Filter clients.");
        System.out.println("6.Filter books.");
        System.out.println("7.Delete client.");
        System.out.println("8.Delete book.");
        System.out.println("9.Update client.");
        System.out.println("9.Update book.");
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

    private void addClient() {
        domain.Client client = readClient();
        ClientValidator validator = new ClientValidator();
        try {
            validator.validate(client);
            studentService.addClient(client);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private domain.Client readClient() {
        System.out.println("Please enter a new client: ");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("ID: ");
            Long id = Long.parseLong(bufferRead.readLine());
            System.out.println("Serial Number: ");
            String serialNumber = bufferRead.readLine();
            System.out.println("Name: ");
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
