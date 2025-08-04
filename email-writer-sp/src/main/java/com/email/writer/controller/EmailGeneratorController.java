package com.email.writer.controller;

import com.email.writer.model.EmailRequest;
import com.email.writer.service.EmailGenraterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class EmailGeneratorController {

    private final EmailGenraterService service;

    // ✅ POST: actual email generation endpoint (used by frontend or curl/Postman)
    @PostMapping("/generate")
    public ResponseEntity<String> genrateEmail(@RequestBody EmailRequest emailRequest) {
        try {
            String response = service.generateEmailReply(emailRequest);

            if (response == null || response.trim().isEmpty()) {
                System.err.println("❌ Gemini API returned empty or null response.");
                return ResponseEntity.status(500).body("❌ Gemini API returned no content.");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Exception while generating email reply: " + e.getMessage());
            e.printStackTrace();  // <-- This will print full stack trace in logs
            return ResponseEntity.status(500).body("❌ Failed to generate email reply. Please try again.");
        }
    }

    // ✅ GET: simple test endpoint (visit in browser to confirm backend is up)
    @GetMapping("/test")
    public ResponseEntity<String> testApi() {
        return ResponseEntity.ok("✅ Email Generator Backend is Running");
    }
}

