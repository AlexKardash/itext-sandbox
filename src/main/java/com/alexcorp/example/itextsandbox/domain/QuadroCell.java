package com.alexcorp.example.itextsandbox.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuadroCell {
    private Integer number;
    private String description;
    private String content;
}
