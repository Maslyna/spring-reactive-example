package com.example.reactive.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "t_persons")
public class Person {
    @Id
    private UUID id;
    private String firstname;
    private String lastname;
    private int age;
}
