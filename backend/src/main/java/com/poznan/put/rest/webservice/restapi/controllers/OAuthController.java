package com.poznan.put.rest.webservice.restapi.controllers;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.calendar.Calendar;
import com.poznan.put.rest.webservice.restapi.configuration.CalendarConfig;
import com.poznan.put.rest.webservice.restapi.jpa.OAuthCredentialRepository;
import com.poznan.put.rest.webservice.restapi.jpa.model.OAuthCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import static com.poznan.put.rest.webservice.restapi.configuration.CalendarConfig.JSON_FACTORY;

@RestController
public class OAuthController {
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    @Autowired
    private OAuthCredentialRepository credentialRepository;

    // Handle the callback after the user is redirected from Google
    @GetMapping("/oauth-callback")
    public String handleGoogleCallback(@RequestParam("code") String code, @RequestParam("state") String state)
            throws GeneralSecurityException, IOException {
        String decodedState = new String(Base64.getDecoder().decode(state));
        int tutorId = Integer.parseInt(decodedState); // Assuming tutorId is an integer

        // Exchange the authorization code for an access token and create the Calendar service
        Calendar service = getCalendarService(code, tutorId);

        // You can now use the service to interact with the user's Google Calendar API
        try {
            // Fetch the calendar list as an example
            service.calendarList().list().execute();
            return "Successfully authenticated and connected to Google Calendar!";
        } catch (Exception e) {
            return "Failed to connect to Google Calendar: " + e.getMessage();
        }
    }

    // Exchange the authorization code for an access token and create a Calendar service
    public Calendar getCalendarService(String code, int tutorId) throws IOException, GeneralSecurityException {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(Objects.requireNonNull(CalendarConfig.class.getResourceAsStream(CREDENTIALS_FILE_PATH))));

        // Exchange the code for an access token
        GoogleAuthorizationCodeTokenRequest tokenRequest = new GoogleAuthorizationCodeTokenRequest(
                HTTP_TRANSPORT, JSON_FACTORY, "https://oauth2.googleapis.com/token", clientSecrets.getDetails().getClientId(),
                clientSecrets.getDetails().getClientSecret(), code, "http://localhost:8080/oauth-callback"
        );

        // Get the credentials from the token request
        GoogleTokenResponse tokenResponse = tokenRequest.execute();

        // Create the OAuthCredential object
        OAuthCredential credential = new OAuthCredential();
        credential.setTutorId(tutorId);
        credential.setAccessToken(tokenResponse.getAccessToken());
        credential.setRefreshToken(tokenResponse.getRefreshToken());
        credential.setExpirationTimeMilliseconds(tokenResponse.getExpiresInSeconds() * 1000 + System.currentTimeMillis());

        // Save credentials to the database
        saveCredentials(credential);

        // Build the Calendar service using the saved credentials
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, createCredential(credential))
                .setApplicationName("TimeFinder")
                .build();
    }

    private void saveCredentials(OAuthCredential credential) {
        credentialRepository.save(credential);  // Save to database
    }

    private Credential createCredential(OAuthCredential oauthCredential) {
        return new Credential(BearerToken.authorizationHeaderAccessMethod())
                .setAccessToken(oauthCredential.getAccessToken())
                .setRefreshToken(oauthCredential.getRefreshToken())
                .setExpirationTimeMilliseconds(oauthCredential.getExpirationTimeMilliseconds());
    }

    @GetMapping("/oauth-test")
    public List<OAuthCredential> getCredentials() {
        return credentialRepository.findAll();
    }
}
