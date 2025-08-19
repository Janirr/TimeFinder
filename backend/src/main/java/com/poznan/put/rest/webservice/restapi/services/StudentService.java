package com.poznan.put.rest.webservice.restapi.services;

import com.poznan.put.rest.webservice.restapi.exception.ResourceNotFound;
import com.poznan.put.rest.webservice.restapi.jpa.StudentRepository;
import com.poznan.put.rest.webservice.restapi.jpa.model.Reservation;
import com.poznan.put.rest.webservice.restapi.jpa.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("There is no student with id: " + id));
    }

    public List<Reservation> getReservationsForStudent(int id) {
        Student student = getStudentById(id);
        return student.getReservationList();
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudentByEmailOrThrowException(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("There is no student with email: " + email));
    }

    public Optional<Student> findByEmailAndPassword(String email, String password) {
        return studentRepository.findByEmailAndPassword(email, password);
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }
}
