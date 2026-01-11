package com.example.egestion.models;

import com.example.egestion.enums.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Employee extends Person {
    @Enumerated(value = EnumType.STRING)
    private Post post;
    @ManyToOne
    private Store store;
    @ManyToOne
    private Employer employer;
}
