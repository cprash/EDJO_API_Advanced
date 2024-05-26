package com.epam.pet.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Pet {
    private int id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Tags> tags;
    private String status;
}
