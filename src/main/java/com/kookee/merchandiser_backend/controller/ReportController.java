package com.kookee.merchandiser_backend.controller;

import com.kookee.merchandiser_backend.model.Report;
import com.kookee.merchandiser_backend.model.Item;
import com.kookee.merchandiser_backend.repository.ReportRepository;
import com.kookee.merchandiser_backend.service.GoogleSheetsWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = {
    "https://kookee-merchandiser-app.netlify.app",
    "http://localhost:3000"
})
public class ReportController {

    private final ReportRepository reportRepository;
    private final GoogleSheetsWriter googleSheetsWriter;

    @Autowired
    public ReportController(ReportRepository reportRepository, GoogleSheetsWriter googleSheetsWriter) {
        this.reportRepository = reportRepository;
        this.googleSheetsWriter = googleSheetsWriter;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> saveReport(@RequestBody Report report) {
        try {
            normalizeMerchandiser(report);

            System.out.println("DEBUG: Received Report:");
            System.out.println("merchandiser = " + report.getMerchandiser());
            System.out.println("outlet = " + report.getOutlet());
            System.out.println("date = " + report.getDate());
            System.out.println("notes = " + report.getNotes());
            System.out.println("Items:");
            if (report.getItems() != null) {
                for (Item i : report.getItems()) {
                    System.out.println(" - name: " + i.getName() + ", qty: " + i.getQty() + ", expiry: " + i.getExpiry() + ", notes: " + i.getNotes());
                }
            }

            // Save to DB
            Report saved = reportRepository.save(report);
            System.out.println("✅ Saved report ID: " + saved.getId());

            // Prepare detailed items map
            Map<String, Object> itemsMap = buildItemsMap(saved.getItems());

            // Sync to Google Sheets
            googleSheetsWriter.appendReport(
                saved.getMerchandiser(),
                saved.getOutlet(),
                saved.getDate().toString(),
                saved.getNotes(),
                itemsMap
            );

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Error saving report: " + e.getMessage());
        }
    }

    // Sync Only endpoint
    @PostMapping("/sync-single")
    public ResponseEntity<?> syncSingleReport(@RequestBody Report report) {
        try {
            normalizeMerchandiser(report);

            System.out.println("DEBUG (sync-single): Received Report:");
            System.out.println("merchandiser = " + report.getMerchandiser());
            System.out.println("outlet = " + report.getOutlet());
            System.out.println("date = " + report.getDate());
            System.out.println("notes = " + report.getNotes());
            System.out.println("Items:");
            if (report.getItems() != null) {
                for (Item i : report.getItems()) {
                    System.out.println(" - name: " + i.getName() + ", qty: " + i.getQty() + ", expiry: " + i.getExpiry() + ", notes: " + i.getNotes());
                }
            }

            Map<String, Object> itemsMap = buildItemsMap(report.getItems());

            googleSheetsWriter.appendReport(
                report.getMerchandiser(),
                report.getOutlet(),
                report.getDate().toString(),
                report.getNotes(),
                itemsMap
            );

            return ResponseEntity.ok("✅ Synced to Google Sheets");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Sync failed: " + e.getMessage());
        }
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    private void normalizeMerchandiser(Report report) {
        if (report.getMerchandiser() != null && !report.getMerchandiser().isEmpty()) {
            String cleaned = report.getMerchandiser().substring(0, 1).toUpperCase()
                    + report.getMerchandiser().substring(1).toLowerCase();
            report.setMerchandiser(cleaned);
        }
    }

    private Map<String, Object> buildItemsMap(List<Item> items) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (items != null) {
            for (Item i : items) {
                System.out.println("DEBUG (buildItemsMap): Adding item to map: name=" + i.getName() + ", qty=" + i.getQty() + ", expiry=" + i.getExpiry() + ", notes=" + i.getNotes());
                Map<String, Object> itemDetails = new HashMap<>();
                itemDetails.put("qty", i.getQty());
                itemDetails.put("expiry", i.getExpiry());
                itemDetails.put("notes", i.getNotes());
                map.put(i.getName(), itemDetails);
            }
        }
        return map;
    }
}
