package service;

import domain.validators.ValidatorException;
import repository.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientService {
    private Repository<Long, domain.Client> repository;

    public ClientService(Repository<Long, domain.Client> repository) {
        this.repository = repository;
    }

    public void addStudent(domain.Client student) throws ValidatorException {
        repository.save(student);
    }

    public Set<domain.Client> getAllStudents() {
        Iterable<domain.Client> students = repository.findAll();
        return StreamSupport.stream(students.spliterator(), false).collect(Collectors.toSet());
    }

    /*POST:Returns all students whose name contain the given string.
     PRE: @param s
     */
    public Set<domain.Client> filterStudentsByName(String s) {
        Iterable<domain.Client> students = repository.findAll();
        //version 1
//        Set<Student> filteredStudents = StreamSupport.stream(students.spliterator(), false)
//                .filter(student -> student.getName().contains(s)).collect(Collectors.toSet());

        //version 2
        Set<domain.Client> filteredStudents= new HashSet<>();
        students.forEach(filteredStudents::add);
        filteredStudents.removeIf(student -> !student.getName().contains(s));

        return filteredStudents;
    }
}
