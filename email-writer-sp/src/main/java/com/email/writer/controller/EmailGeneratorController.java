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
    public ResponseEntity<String> genrateEmail(@RequestBody EmailRequest emailRequest){
        String response = service.generateEmailReply(emailRequest);
        return ResponseEntity.ok(response);
    }

    // ✅ GET: simple test endpoint (visit in browser to confirm backend is up)
    @GetMapping("/test")
    public ResponseEntity<String> testApi() {
        return ResponseEntity.ok("✅ Email Generator Backend is Running");
    }
}

