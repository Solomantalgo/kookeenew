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

    public void appendReport(String merchandiser, String outlet, String date, Map<String, Integer> itemsMap) {
        if (itemsMap == null || itemsMap.isEmpty()) {
            logger.warn("Items map is empty or null. Skipping Google Sheets sync.");
            return;
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            URL url = new URL("https://sheet-api-gju6.onrender.com/report");

            List<Map<String, Object>> itemsList = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : itemsMap.entrySet()) {
                Map<String, Object> itemObj = new HashMap<>();
                itemObj.put("name", entry.getKey());
                itemObj.put("qty", entry.getValue());
                itemsList.add(itemObj);
            }

            Map<String, Object> payload = new HashMap<>();
            payload.put("merchandiser", merchandiser);
            payload.put("outlet", outlet);
            payload.put("date", date);
            payload.put("items", itemsList);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url.toString(), request, String.class);
            logger.info("Google Sheets API response: status={}, body={}", response.getStatusCode(), response.getBody());

        } catch (Exception e) {
            logger.error("Failed to sync with Google Sheets: {}", e.getMessage(), e);
        }
    }
}
