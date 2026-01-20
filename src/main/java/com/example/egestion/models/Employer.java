package com.example.egestion.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.annotation.Inherited;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employer extends Person implements UserDetails {
    @OneToMany
    private List<Store> stores = new ArrayList<>();
    @OneToMany(mappedBy = "employer",orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_EMPLOYER"));
    }

    @Override
    public @Nullable String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
