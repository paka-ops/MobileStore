package com.example.egestion.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS )
@NoArgsConstructor
@Data
public abstract class Person {
    @Id @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;
    private String firstname;
    private String secondname;
    private String phone;

    public Person(String firstname,String secondname,String phone){
        this.firstname = firstname;
        this.secondname = secondname;
        this.phone = phone;
    }

}
