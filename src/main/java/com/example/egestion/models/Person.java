package com.example.egestion.models;

import com.example.egestion.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE )
@NoArgsConstructor
@Data
@DiscriminatorColumn(name = "userType",discriminatorType = DiscriminatorType.STRING)

public abstract class Person {
    @Id @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;
    private String firstname;
    private String secondeName;
    private String phone;
    private String password;
    private String username;


    public Person(String firstname,String secondeName,String username,String password,String phone){
        this.firstname = firstname;
        this.secondeName = secondeName;
        this.username = username;
        this.phone = phone;
        this.password = password;
    }

}
