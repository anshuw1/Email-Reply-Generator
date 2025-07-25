package com.email.writer.controller;

import com.email.writer.model.EmailRequest;
import com.email.writer.service.EmailGenraterService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class EmailGeneratorController {

    private final EmailGenraterService service;


    @PostMapping("/genrater")
    public ResponseEntity<String> genrateEmail(@RequestBody EmailRequest emailRequest){
        String response=service.generateEmailReply(emailRequest);
        return ResponseEntity.ok(response);
    }
}
