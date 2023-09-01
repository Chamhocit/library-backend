package com.example.aptech.spring.library.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;


public class SetMessageResponse {
    public void SetMessage(HttpServletResponse response, String Message){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonError = objectMapper.writeValueAsString(Collections.singletonMap("message", Message));
            response.setContentType("application/json");
            response.getWriter().write(jsonError);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
