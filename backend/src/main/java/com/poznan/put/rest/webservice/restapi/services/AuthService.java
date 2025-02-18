package com.poznan.put.rest.webservice.restapi.services;

import com.poznan.put.rest.webservice.restapi.jpa.StudentRepository;
import com.poznan.put.rest.webservice.restapi.jpa.TutorsRepository;
import com.poznan.put.rest.webservice.restapi.security.LoginRequest;
import com.poznan.put.rest.webservice.restapi.security.RegisterRequest;
import com.poznan.put.rest.webservice.restapi.student.Student;
import com.poznan.put.rest.webservice.restapi.tutor.Tutor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final StudentRepository studentRepository;
    private final TutorsRepository tutorsRepository;

    public AuthService(StudentRepository studentRepository, TutorsRepository tutorsRepository) {
        this.studentRepository = studentRepository;
        this.tutorsRepository = tutorsRepository;
    }

    public Student registerUser(RegisterRequest registerRequest) {
        // Implement registration logic here
        Student student = new Student();
        // Set student properties from registerRequest
        return studentRepository.save(student);
    }

    public Student studentLogin(LoginRequest loginRequest) {
        return studentRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())
                .orElseThrow(() -> new BadCredentialsException("Invalid student credentials"));
    }

    public Tutor tutorLogin(LoginRequest loginRequest) {
        return tutorsRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())
                .orElseThrow(() -> new BadCredentialsException("Invalid tutor credentials"));
    }
}
