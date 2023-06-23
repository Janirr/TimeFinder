package com.poznan.put.rest.webservice.restapi.security;

import com.poznan.put.rest.webservice.restapi.Tutor.Tutor;
import com.poznan.put.rest.webservice.restapi.jpa.StudentRepository;
import com.poznan.put.rest.webservice.restapi.jpa.TutorsRepository;
import com.poznan.put.rest.webservice.restapi.student.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final StudentRepository studentRepository;
    private final TutorsRepository tutorsRepository;

    public AuthController(StudentRepository studentRepository, TutorsRepository tutorsRepository) {
        this.studentRepository = studentRepository;
        this.tutorsRepository = tutorsRepository;
    }

    @PostMapping("/login/student")
    public ResponseEntity<Student> studentLogin(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Student student = studentRepository.findByEmail(email);
        if (student != null && student.getPassword().equals(password)) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    @PostMapping("/login/tutor")
    public ResponseEntity<Tutor> tutorLogin(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Tutor tutor = tutorsRepository.findByEmail(email);
        if (tutor != null && tutor.getPassword().equals(password)) {
            return ResponseEntity.ok(tutor);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
