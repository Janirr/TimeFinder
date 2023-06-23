package com.poznan.put.rest.webservice.restapi.jpa;

import com.poznan.put.rest.webservice.restapi.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Student findByEmail(String email);
}
