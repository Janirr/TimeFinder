package com.poznan.put.rest.webservice.restapi.jpa;

import com.poznan.put.rest.webservice.restapi.exception.ResourceNotFound;
import com.poznan.put.rest.webservice.restapi.reservation.Reservation;
import com.poznan.put.rest.webservice.restapi.student.Student;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentJpaResource {
    private final StudentRepository studentRepository;
    private final ReservationRepository reservationRepository;

    public StudentJpaResource(StudentRepository studentRepository, ReservationRepository reservationRepository) {
        this.studentRepository = studentRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    public List<Student> retrieveAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Student> retrieveStudentById(@PathVariable int id) {
        Optional<Student> Student = studentRepository.findById(id);
        if(Student.isEmpty()){
            throw new ResourceNotFound("There is no student with id: "+id);
        }
        return Student;
    }

    @GetMapping("/{id}/reservations")
    public List<Reservation> retrieveReservationsForStudent(@PathVariable int id) {
        Optional<Student> Student = studentRepository.findById(id);
        if(Student.isEmpty()){
            throw new ResourceNotFound("There is no student with id: "+id);
        }
        return Student.get().getReservationList();
    }

    @PostMapping
    public ResponseEntity<Student> createNewStudent(@Valid @RequestBody Student student) {
        // add Student into the ArrayList in Service
        Student savedStudent = studentRepository.save(student);
        // get new Location for the Student to be created in
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedStudent.getId())
                .toUri();
        /* return the ResponseEntity when trying to
           create Student in location and build it   */
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{id}/reservations")
    public ResponseEntity<Object> createReservationForStudent(@PathVariable int id, @Valid @RequestBody Reservation reservation) {
        Optional<Student> Student = studentRepository.findById(id);
        if(Student.isEmpty()){
            throw new ResourceNotFound("There is not student with id: "+id);
        }
        reservation.setStudent(Student.get());
        Reservation savedReservation = reservationRepository.save(reservation);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedReservation.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public void deleteStudentById2(@PathVariable int id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFound("There is no student with id: "+id);
        }
        studentRepository.deleteById(id);
    }
}
