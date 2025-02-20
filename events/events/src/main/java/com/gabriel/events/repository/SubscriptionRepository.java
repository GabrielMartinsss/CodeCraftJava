package com.gabriel.events.repository;

import com.gabriel.events.model.Event;
import com.gabriel.events.model.Subscription;
import com.gabriel.events.model.User;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
    public Subscription findByEventAndSubscriber(Event event, User user);
}
