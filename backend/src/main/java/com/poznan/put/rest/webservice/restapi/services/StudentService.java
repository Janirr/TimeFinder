package com.poznan.put.rest.webservice.restapi.services;

import com.poznan.put.rest.webservice.restapi.exception.ResourceNotFound;
import com.poznan.put.rest.webservice.restapi.jpa.StudentRepository;
import com.poznan.put.rest.webservice.restapi.student.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student findStudentByEmailOrThrowException(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("There is no student with email: " + email));
    }

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }
}
