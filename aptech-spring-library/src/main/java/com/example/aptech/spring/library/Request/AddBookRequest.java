package com.example.aptech.spring.library.Request;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AddBookRequest {
    private String title;

    private String author;

    private String description;

    private int copies;


    private String category;

    private String img;


}
