package com.poznan.put.rest.webservice.restapi;

import com.poznan.put.rest.webservice.restapi.jpa.StudentRepository;
import com.poznan.put.rest.webservice.restapi.student.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StudentTests {
    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void saveStudentTest() {
        Student student = new Student();
        student.setName("Jan");
        student.setSurname("Kowalski");
        student.setAdvanced(false);
        student.setTypeOfSchool("Liceum");
        student.setYearOfSchool(4);
        studentRepository.save(student);
        Student savedStudent = studentRepository.findById(student.getId()).orElse(null);
        Assertions.assertThat(savedStudent).isNotNull();
        studentRepository.delete(student);
    }
    @Test
    public void checkStudentNameTest() {
        Student student = new Student();
        student.setName("Jan");
        studentRepository.save(student);
        Student retrievedStudent = studentRepository.findById(student.getId()).orElse(null);
        Assertions.assertThat(retrievedStudent).isNotNull();
        Assertions.assertThat(retrievedStudent.getName()).isEqualTo("Jan");
        studentRepository.delete(student);
    }

    @Test
    public void checkStudentSurnameTest(){
        Student student = new Student();
        student.setSurname("Kowalski");
        studentRepository.save(student);
        Student retrievedStudent = studentRepository.findById(student.getId()).orElse(null);
        Assertions.assertThat(retrievedStudent).isNotNull();
        Assertions.assertThat(retrievedStudent.getSurname()).isEqualTo("Kowalski");
        studentRepository.delete(student);
    }

    @Test
    public void checkStudentAdvancedTest(){
        Student student = new Student();
        student.setAdvanced(true);
        studentRepository.save(student);
        Student retrievedStudent = studentRepository.findById(student.getId()).orElse(null);
        Assertions.assertThat(retrievedStudent).isNotNull();
        Assertions.assertThat(retrievedStudent.isAdvanced()).isEqualTo(true);
        studentRepository.delete(student);
    }

    @Test
    public void deleteStudentTest() {
        Student student = new Student();
        student.setName("Jan");
        student.setSurname("Kowalski");
        studentRepository.save(student);
        studentRepository.delete(student);
        Assertions.assertThat(studentRepository.findById(student.getId())).isEmpty();
    }

    @Test
    public void studentRepositoryEmptyTest(){
        Assertions.assertThat(studentRepository.findAll()).isNotEmpty();
    }

}
