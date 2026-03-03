package com.example.egestion.services.interfaces;

import com.example.egestion.enums.SubscriptionDuration;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Subscription;

public interface SubscriptionService {
   Subscription create(Subscription subscription, Employer employer, SubscriptionDuration duration);


}
