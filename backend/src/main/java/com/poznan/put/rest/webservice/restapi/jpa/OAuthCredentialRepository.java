package com.poznan.put.rest.webservice.restapi.jpa;

import com.poznan.put.rest.webservice.restapi.jpa.model.OAuthCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthCredentialRepository extends JpaRepository<OAuthCredential, Integer> {
    Optional<OAuthCredential> findByTutorId(int tutorId);
}
