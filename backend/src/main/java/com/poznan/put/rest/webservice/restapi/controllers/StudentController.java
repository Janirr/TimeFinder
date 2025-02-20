package com.poznan.put.rest.webservice.restapi.controllers;

import com.poznan.put.rest.webservice.restapi.model.Reservation;
import com.poznan.put.rest.webservice.restapi.model.Student;
import com.poznan.put.rest.webservice.restapi.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<Reservation>> getReservationsForStudent(@PathVariable int id) {
        return ResponseEntity.ok(studentService.getReservationsForStudent(id));
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        Student savedStudent = studentService.createStudent(student);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedStudent.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedStudent);
    }

    @PostMapping("/{id}/reservations")
    public ResponseEntity<Reservation> createReservationForStudent(@PathVariable int id,
                                                                   @Valid @RequestBody Reservation reservation) {
        Reservation savedReservation = studentService.createReservationForStudent(id, reservation);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedReservation.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedReservation);
    }
}
