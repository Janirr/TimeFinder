package com.poznan.put.rest.webservice.restapi.security;

import com.poznan.put.rest.webservice.restapi.jpa.StudentRepository;
import com.poznan.put.rest.webservice.restapi.student.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final StudentRepository studentRepository;

    public AuthController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Student> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Student student = studentRepository.findByLogin(username);
        if (student != null && student.getPassword().equals(password)) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
