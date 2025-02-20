package com.poznan.put.rest.webservice.restapi.services;

import com.poznan.put.rest.webservice.restapi.exception.ResourceNotFound;
import com.poznan.put.rest.webservice.restapi.jpa.ReservationRepository;
import com.poznan.put.rest.webservice.restapi.jpa.StudentRepository;
import com.poznan.put.rest.webservice.restapi.model.Reservation;
import com.poznan.put.rest.webservice.restapi.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final ReservationRepository reservationRepository;

    public StudentService(StudentRepository studentRepository, ReservationRepository reservationRepository) {
        this.studentRepository = studentRepository;
        this.reservationRepository = reservationRepository;
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

    public Reservation createReservationForStudent(int id, Reservation reservation) {
        Student student = getStudentById(id);
        reservation.setStudent(student);
        return reservationRepository.save(reservation);
    }


}
