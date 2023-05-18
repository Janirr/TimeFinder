package com.poznan.put.rest.webservice.restapi;

import com.poznan.put.rest.webservice.restapi.Tutor.Tutor;
import com.poznan.put.rest.webservice.restapi.jpa.ReservationRepository;
import com.poznan.put.rest.webservice.restapi.jpa.StudentRepository;
import com.poznan.put.rest.webservice.restapi.jpa.TutorsRepository;
import com.poznan.put.rest.webservice.restapi.reservation.Reservation;
import com.poznan.put.rest.webservice.restapi.student.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest
public class JpaTests {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TutorsRepository tutorsRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void saveStudentTest() {
        Student student = new Student();
        student.setName("Jan");
        student.setSurname("Studencki");
        student.setAdvanced(false);
        student.setTypeOfSchool("Liceum");
        student.setYearOfSchool(4);

        Tutor tutor = new Tutor();
        tutor.setName("Jan");
        tutor.setSurname("Kowalski");
        tutor.setEmail("example@wp.pl");
        tutor.setPhoneNumber("123456789");

        Reservation reservation = new Reservation();
        reservation.setStudent(student);
        reservation.setTutor(tutor);
        reservation.setDay(LocalDate.now());
        reservation.setLocation("Poznan");
        reservation.setSubject("Matematyka");
        reservation.setStatus("Zarezerwowane");
        reservation.setStartHour(LocalTime.parse("18:00"));
        reservation.setEndHour(LocalTime.parse("19:00"));

        studentRepository.save(student); // Save the Student object first
        reservation.setStudent(student); // Set the saved Student object in Reservation
        reservation.setTutor(tutor);
        tutorsRepository.save(tutor);
        reservationRepository.save(reservation);
        Student savedStudent = studentRepository.findById(student.getId()).orElse(null);
        Assertions.assertThat(savedStudent).isNotNull();
    }
}
