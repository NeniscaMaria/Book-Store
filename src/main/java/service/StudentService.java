package ro.ubb.catalog.service;

import ro.ubb.catalog.domain.validators.ValidatorException;
import ro.ubb.catalog.repository.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author radu.
 */
public class StudentService {
    private Repository<Long, ro.ubb.catalog.domain.Client> repository;

    public StudentService(Repository<Long, ro.ubb.catalog.domain.Client> repository) {
        this.repository = repository;
    }

    public void addStudent(ro.ubb.catalog.domain.Client student) throws ValidatorException {
        repository.save(student);
    }

    public Set<ro.ubb.catalog.domain.Client> getAllStudents() {
        Iterable<ro.ubb.catalog.domain.Client> students = repository.findAll();
        return StreamSupport.stream(students.spliterator(), false).collect(Collectors.toSet());
    }

    /**
     * Returns all students whose name contain the given string.
     * 
     * @param s
     * @return
     */
    public Set<ro.ubb.catalog.domain.Client> filterStudentsByName(String s) {
        Iterable<ro.ubb.catalog.domain.Client> students = repository.findAll();
        //version 1
//        Set<Student> filteredStudents = StreamSupport.stream(students.spliterator(), false)
//                .filter(student -> student.getName().contains(s)).collect(Collectors.toSet());

        //version 2
        Set<ro.ubb.catalog.domain.Client> filteredStudents= new HashSet<>();
        students.forEach(filteredStudents::add);
        filteredStudents.removeIf(student -> !student.getName().contains(s));

        return filteredStudents;
    }
}
