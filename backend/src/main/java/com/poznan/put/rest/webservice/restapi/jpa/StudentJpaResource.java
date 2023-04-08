package com.poznan.put.rest.webservice.restapi.jpa;

import com.poznan.put.rest.webservice.restapi.reservation.Reservation;
import com.poznan.put.rest.webservice.restapi.student.Student;
import com.poznan.put.rest.webservice.restapi.student.StudentNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class StudentJpaResource {
    // private fields
    private final StudentRepository StudentRepository;
    private final ReservationRepository ReservationRepository;

    // public Constructor
    public StudentJpaResource(StudentRepository studentRepository, ReservationRepository reservationRepository) {
        this.StudentRepository = studentRepository;
        this.ReservationRepository = reservationRepository;
    }

    // display all Students details
    @GetMapping("/students")
    public List<Student> retrieveAllStudents(){
        return StudentRepository.findAll();
    }

    // display Student details
    @GetMapping("/students/{id}")
    public Optional<Student> retrieveStudentById(@PathVariable int id){
        Optional<Student> Student = StudentRepository.findById(id);
        if(Student.isEmpty()){
            throw new StudentNotFoundException("There is no student with id: "+id);
        }
        return Student;
    }

    // create new Student
    @PostMapping("/students")
    public ResponseEntity<Student> createNewStudent(@Valid @RequestBody Student student){
        // add Student into the ArrayList in Service
        Student savedStudent = StudentRepository.save(student);
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

    // delete Student
    @DeleteMapping("/students/{id}")
    public void deleteStudentById(@PathVariable int id){
        StudentRepository.deleteById(id);
    }

    @GetMapping("/students/{id}/reservations")
    public List<Reservation> retrieveReservationsForStudent(@PathVariable int id){
        Optional<Student> Student = StudentRepository.findById(id);
        if(Student.isEmpty()){
            throw new StudentNotFoundException("id:"+id);
        }
        return Student.get().getReservationList();
    }

    @PostMapping("/students/{id}/reservations")
    public ResponseEntity<Object> createdateForStudent(@PathVariable int id, @Valid @RequestBody Reservation reservation){
        Optional<Student> Student = StudentRepository.findById(id);
        if(Student.isEmpty()){
            throw new StudentNotFoundException("id:"+id);
        }
        reservation.setStudent(Student.get());
        Reservation savedReservation = ReservationRepository.save(reservation);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedReservation.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/reservations")
    public List<Reservation> retrieveAllReservations(){
        return ReservationRepository.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createNewReservation(@Valid @RequestBody Reservation reservation){
        // add Student into the ArrayList in Service
        Reservation savedReservation = ReservationRepository.save(reservation);
        // get new Location for the Student to be created in
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedReservation.getId())
                .toUri();
        /* return the ResponseEntity when trying to
           create Student in location and build it   */
        return ResponseEntity.created(location).build();
    }
}
