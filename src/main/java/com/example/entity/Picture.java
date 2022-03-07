package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NonNull
public class Picture {

    private String name;
    private String type;
    private String source;
    private long size;


}
