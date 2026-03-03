package com.example.egestion.models;

import com.example.egestion.enums.Plan;
import com.example.egestion.enums.SubscriptionDuration;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
public  class Subscription {
    @Id @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;
    @Enumerated(value = EnumType.STRING)
    private SubscriptionDuration duration;
    @CreationTimestamp
    private LocalDate startDate;
    private LocalDate expirationDate;
    private boolean status;
    private Plan plan ;
    @OneToOne(mappedBy = "subscription")
    Employer employer;

    public Subscription(SubscriptionDuration duration, boolean status, Employer employer, Plan plan) {
        this.duration = duration;
        this.status = status;
        this.employer = employer;
        this.plan = plan;
    }
}
