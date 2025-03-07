package com.poznan.put.rest.webservice.restapi.controllers;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.calendar.Calendar;
import com.poznan.put.rest.webservice.restapi.configuration.CalendarConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.logging.Logger;

import static com.poznan.put.rest.webservice.restapi.configuration.CalendarConfig.CREDENTIALS_FILE_PATH;
import static com.poznan.put.rest.webservice.restapi.configuration.CalendarConfig.JSON_FACTORY;

@RestController
public class OAuthController {

    private static final Logger logger = Logger.getLogger(OAuthController.class.getName());

    // Handle the callback after the user is redirected from Google
    @GetMapping("/oauth-callback")
    public String handleGoogleCallback(@RequestParam("code") String code, @RequestParam("state") String state)
            throws GeneralSecurityException, IOException {
        logger.info("Received code: " + code);
        logger.info("Received state: " + state);

        // Get the credentials from the authorization code
        Calendar service = getCalendarService(code);

        // You can now use the service to interact with the user's Google Calendar API
        // Example: Fetch the calendar list
        try {
            // Do something with the service, like fetching events or calendar list
            service.calendarList().list().execute();
            return "Successfully authenticated and connected to Google Calendar!";
        } catch (Exception e) {
            return "Failed to connect to Google Calendar: " + e.getMessage();
        }
    }

    // Exchange the authorization code for an access token and create a Calendar service
    private Calendar getCalendarService(String code) throws IOException, GeneralSecurityException {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(CalendarConfig.class.getResourceAsStream(CREDENTIALS_FILE_PATH)));

        GoogleAuthorizationCodeTokenRequest tokenRequest = new GoogleAuthorizationCodeTokenRequest(
                HTTP_TRANSPORT, JSON_FACTORY, "https://oauth2.googleapis.com/token", clientSecrets.getDetails().getClientId(),
                clientSecrets.getDetails().getClientSecret(), code, "http://localhost:4200/oauth-callback"
        );

        GoogleAuthorizationCodeTokenResponse tokenResponse = tokenRequest.execute();
        Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(tokenResponse.getAccessToken());

        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("TimeFinder")
                .build();
    }
}

