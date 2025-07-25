package com.email.writer.service;

import com.email.writer.model.EmailRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Map;

@Service
public class EmailGenraterService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public EmailGenraterService(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }


    public String generateEmailReply(EmailRequest emailRequest) {
        // Build the prompt
        String prompt = buildPrompt(emailRequest);

        // Craft a request
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        // Do request and get response
        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Extract Response and Return
        return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            return "Error processing request: " + e.getMessage();
        }
    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply for hte following email content. Please don't generate a subject line ");
        if (emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
            prompt.append("Use a ").append(emailRequest.getTone()).append(" tone.");
        }
        prompt.append("\nOriginal email: \n").append(emailRequest.getEmailContent());
        return prompt.toString();
    }



   /* private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public EmailGenraterService(WebClient.Builder webClient) {
        this.webClient =webClient.build() ;
    }


    public String genarteEmailReply(EmailRequest emailRequest){
        //Build the Prompt
        String prompt=builedPrompt(emailRequest);
        // Craft a request
        Map<String,Object> reqestBody=Map.of(
                "contents",new Object[]{
                      Map.of("text",prompt)
                });
        //Do request and get response
       // String fullUrl = geminiApiUrl + "?key=" + geminiApiKey;
        String response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(geminiApiUrl) // just the path, no query params
                        .queryParam("key", geminiApiKey)
                        .build())
                .header("Content-Type","application/json")
                .bodyValue(reqestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //Extract Response & return
        return extractResponceContent(response);

    }

    private String extractResponceContent(String response) {
        try {
            ObjectMapper mapper=new ObjectMapper();
            JsonNode rootNode=mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            return "Error Prossesing Request"+e.getMessage();
        }
    }

    private String builedPrompt(EmailRequest emailRequest) {
        StringBuilder prompt=new StringBuilder();
        prompt.append("Generate a professional email reply for  the following email content. Please don't generate a subject line ");
        if(emailRequest.getTone()!= null && !emailRequest.getTone().isEmpty()){
            prompt.append("Use a").append(emailRequest.getTone()).append("Tone");
        }
        prompt.append("\nOringnal email: \n").append(emailRequest.getEmailContent());
        return prompt.toString();
    }*/


}
