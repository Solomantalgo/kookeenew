package com.kookee.merchandiser_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.*;

@Service
public class GoogleSheetsWriter {

    private static final Logger logger = LoggerFactory.getLogger(GoogleSheetsWriter.class);
    private static final String SHEET_API_URL = "https://sheet-api-gju6.onrender.com/report";

    public void appendReport(String merchandiser, String outlet, String date, Map<String, Integer> itemsMap) {
        if (itemsMap == null || itemsMap.isEmpty()) {
            logger.warn("❗ Items map is empty or null. Skipping Google Sheets sync.");
            return;
        }

        try {
            // Prepare the HTTP request
            RestTemplate restTemplate = new RestTemplate();
            URL url = new URL(SHEET_API_URL);

            // Build the items payload
            List<Map<String, Object>> itemsList = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : itemsMap.entrySet()) {
                if (entry.getKey() == null || entry.getKey().isEmpty()) continue;

                Map<String, Object> itemObj = new HashMap<>();
                itemObj.put("name", entry.getKey());
                itemObj.put("qty", entry.getValue() != null ? entry.getValue() : 0);
                itemsList.add(itemObj);
            }

            if (itemsList.isEmpty()) {
                logger.warn("⚠️ No valid items to send to Google Sheets. Aborting sync.");
                return;
            }

            // Build the full report payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("merchandiser", merchandiser);
            payload.put("outlet", outlet);
            payload.put("date", date);
            payload.put("items", itemsList);

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Build request
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            // POST request to sheet API
            ResponseEntity<String> response = restTemplate.postForEntity(url.toString(), request, String.class);

            // Handle response
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("✅ Successfully synced report to Google Sheets for {}", merchandiser);
            } else {
                logger.error("❌ Google Sheets sync returned non-2xx status: {}", response.getStatusCode());
                logger.debug("Response body: {}", response.getBody());
            }

        } catch (Exception e) {
            logger.error("❌ Exception during Google Sheets sync: {}", e.getMessage(), e);
        }
    }
}
