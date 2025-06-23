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

    // ✅ Save and Sync
    @PostMapping
    @Transactional
    public ResponseEntity<?> saveReport(@RequestBody Report report) {
        try {
            normalizeMerchandiser(report);

            // Save to DB
            Report saved = reportRepository.save(report);
            System.out.println("✅ Saved report ID: " + saved.getId());

            // Prepare items map
            Map<String, Integer> itemsMap = buildItemsMap(saved.getItems());

            // Sync to Google Sheets
            googleSheetsWriter.appendReport(
                saved.getMerchandiser(),
                saved.getOutlet(),
                saved.getDate().toString(),
                itemsMap
            );

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Error saving report: " + e.getMessage());
        }
    }

    // ✅ Sync Only
    @PostMapping("/sync-single")
    public ResponseEntity<?> syncSingleReport(@RequestBody Report report) {
        try {
            normalizeMerchandiser(report);

            // Prepare items map
            Map<String, Integer> itemsMap = buildItemsMap(report.getItems());

            // Sync to Google Sheets
            googleSheetsWriter.appendReport(
                report.getMerchandiser(),
                report.getOutlet(),
                report.getDate().toString(),
                itemsMap
            );

            return ResponseEntity.ok("✅ Synced to Google Sheets");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Sync failed: " + e.getMessage());
        }
    }

    // ✅ Fetch all reports
    @GetMapping
    @Transactional(readOnly = true)
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    // Helpers
    private void normalizeMerchandiser(Report report) {
        if (report.getMerchandiser() != null && !report.getMerchandiser().isEmpty()) {
            String cleaned = report.getMerchandiser().substring(0, 1).toUpperCase()
                    + report.getMerchandiser().substring(1).toLowerCase();
            report.setMerchandiser(cleaned);
        }
    }

    private Map<String, Integer> buildItemsMap(List<Item> items) {
        Map<String, Integer> map = new LinkedHashMap<>();
        if (items != null) {
            for (Item i : items) {
                map.put(i.getName(), i.getQty());
            }
        }
        return map;
    }
}
