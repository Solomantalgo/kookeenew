package com.kookee.merchandiser_backend.config;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Configuration
public class SheetsRestConfig {

    @Bean
    public HttpRequestFactory googleSheetsRequestFactory() throws Exception {
        // Read the credentials JSON string from the environment variable
        String credentialsJson = System.getenv("GOOGLE_CREDENTIALS_JSON");

        if (credentialsJson == null || credentialsJson.isEmpty()) {
            throw new IllegalStateException("Environment variable GOOGLE_CREDENTIALS_JSON not found or empty");
        }

        // Convert the JSON string to an input stream
        ByteArrayInputStream credentialsStream =
                new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8));

        // Create credentials with Google Sheets scope
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets"));

        // Build the Sheets-compatible HTTP request factory
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        return httpTransport.createRequestFactory(new HttpCredentialsAdapter(credentials));
    }
}
