package com.alexcorp.example.itextsandbox.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuadroRow {
    private float[] relativeWidths;
    private List<QuadroCell> cells;
}
