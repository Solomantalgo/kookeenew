package com.kookee.merchandiser_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.*;

@Service
public class GoogleSheetsWriter {

    private static final Logger logger = LoggerFactory.getLogger(GoogleSheetsWriter.class);

    @Value("${sheetapi.url}")
    private String sheetApiUrl;

    @SuppressWarnings("unchecked")
    public void appendReport(String merchandiser, String outlet, String date, String notes, Map<String, Object> itemsMap) {
        if (itemsMap == null || itemsMap.isEmpty()) {
            logger.warn("Items map is empty or null. Skipping Google Sheets sync.");
            return;
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            URL url = new URL(sheetApiUrl);

            List<Map<String, Object>> itemsList = new ArrayList<>();

            System.out.println("DEBUG (GoogleSheetsWriter): Processing items for Google Sheets API:");
            for (Map.Entry<String, Object> entry : itemsMap.entrySet()) {
                String itemName = entry.getKey();
                Object itemValue = entry.getValue();

                Map<String, Object> itemObj = new HashMap<>();
                itemObj.put("name", itemName);

                if (itemValue instanceof Map) {
                    Map<String, Object> valueMap = (Map<String, Object>) itemValue;
                    Object qtyVal = valueMap.getOrDefault("qty", 0);
                    Object expiryVal = valueMap.getOrDefault("expiry", "");
                    Object notesVal = valueMap.getOrDefault("notes", "");
                    System.out.println(" - item: " + itemName + ", qty: " + qtyVal + ", expiry: " + expiryVal + ", notes: " + notesVal);

                    itemObj.put("qty", qtyVal);
                    itemObj.put("expiry", expiryVal);
                    itemObj.put("notes", notesVal);
                } else {
                    System.out.println(" - item: " + itemName + ", qty (no map): " + itemValue);
                    itemObj.put("qty", itemValue);
                    itemObj.put("expiry", "");
                    itemObj.put("notes", "");
                }

                itemsList.add(itemObj);
            }

            Map<String, Object> payload = new HashMap<>();
            payload.put("merchandiser", merchandiser);
            payload.put("outlet", outlet);
            payload.put("date", date);
            payload.put("notes", notes);
            payload.put("items", itemsList);

            System.out.println("DEBUG (GoogleSheetsWriter): Final payload sent to Google Sheets API: " + payload);

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
