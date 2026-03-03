package com.example.egestion.models;

import com.example.egestion.enums.Plan;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.annotation.Inherited;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("Employer")
@Data
@Entity
public class Employer extends Person implements UserDetails {
    @OneToMany(mappedBy = "employer")
    @JsonIgnore
    private List<Store> stores = new ArrayList<>();
    @OneToMany(mappedBy = "employer",orphanRemoval = true)
    @JsonIgnore
    private List<Employee> employees = new ArrayList<>();
    @OneToOne()
    private Subscription subscription;
    public Employer(String firstname, String secondeName, String username, String password, String phone, List<Store> stores, List<Employee> employees) {
        super(firstname, secondeName, username, password, phone);
        this.stores = stores;
        this.employees = employees;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_EMPLOYER"));
    }

    @Override
    public @Nullable String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        if(this.subscription == null) return true;
        return this.subscription.getExpirationDate().isAfter(LocalDate.now());
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
