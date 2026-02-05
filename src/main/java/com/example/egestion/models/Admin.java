package com.example.egestion.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("Admin")
@Entity
public class Admin extends Person implements UserDetails {
    private String adminRole;
    public Admin(String firstname, String secondeName, String username, String password, String phone,String adminRole) {
        super(firstname, secondeName, username, password, phone);
        this.adminRole = adminRole;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public @Nullable String getPassword() {
        return  super.getPassword();
    }

    @Override
    public String getUsername() {
        return  super.getUsername();
    }
}
