package com.poznan.put.rest.webservice.restapi.services;

import com.poznan.put.rest.webservice.restapi.controllers.requests.LoginRequest;
import com.poznan.put.rest.webservice.restapi.controllers.requests.RegisterRequest;
import com.poznan.put.rest.webservice.restapi.exception.RegisterException;
import com.poznan.put.rest.webservice.restapi.jpa.StudentRepository;
import com.poznan.put.rest.webservice.restapi.jpa.TutorsRepository;
import com.poznan.put.rest.webservice.restapi.jpa.model.Student;
import com.poznan.put.rest.webservice.restapi.jpa.model.Tutor;
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

    public Object registerUser(RegisterRequest registerRequest) {
        if (registerRequest.isTutor()) {
            return registerTutor(registerRequest);
        } else {
            return registerStudent(registerRequest);
        }
    }

    public Student registerStudent(RegisterRequest registerRequest) {
        Student student = new Student();
        student.setEmail(registerRequest.email());
        student.setName(registerRequest.name());
        student.setSurname(registerRequest.surname());
        student.setPhoneNumber(registerRequest.phoneNumber());
        // FIXME: hashing password
        student.setPassword(registerRequest.password());
        return studentRepository.save(student);
    }

    public Tutor registerTutor(RegisterRequest registerRequest) {
        Tutor tutor = new Tutor();

        tutor.setName(registerRequest.name());
        tutor.setSurname(registerRequest.surname());

        if (tutorsRepository.findByPhoneNumber(registerRequest.phoneNumber()).isPresent()) {
            throw new RegisterException("Tutor with this phone number already exists");
        }
        tutor.setPhoneNumber(registerRequest.phoneNumber());

        if (tutorsRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new RegisterException("Tutor with this email already exists");
        }
        tutor.setEmail(registerRequest.email());

        // FIXME: hashing password
        tutor.setPassword(registerRequest.password());

        return tutorsRepository.save(tutor);
    }

    public Student studentLogin(LoginRequest loginRequest) {
        return studentRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new BadCredentialsException("Invalid student credentials"));
    }

    public Tutor tutorLogin(LoginRequest loginRequest) {
        return tutorsRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new BadCredentialsException("Invalid tutor credentials"));
    }
}
