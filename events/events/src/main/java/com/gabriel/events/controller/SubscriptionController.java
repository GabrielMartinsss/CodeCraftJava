package com.gabriel.events.controller;

import com.gabriel.events.dto.ErrorMessage;
import com.gabriel.events.dto.SubscriptionResponse;
import com.gabriel.events.exception.EventNotFoundException;
import com.gabriel.events.exception.SubscriptionConflictException;
import com.gabriel.events.exception.UserIndicatorNotFoundException;
import com.gabriel.events.model.Subscription;
import com.gabriel.events.model.User;
import com.gabriel.events.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping({"/subscription/{prettyName}", "/subscription/{prettyName}/{userIndicatorId}"})
    public ResponseEntity<?> createSubscription(
            @PathVariable String prettyName,
            @RequestBody User subscriber,
            @PathVariable(required = false) Integer userIndicatorId
    ) {
        try {
            SubscriptionResponse res = subscriptionService.createNewSubscription(prettyName, subscriber, userIndicatorId);
            if (res != null) {
                return ResponseEntity.ok(res);
            }
        }
        catch (EventNotFoundException | UserIndicatorNotFoundException exception) {
            return ResponseEntity.status(404).body(new ErrorMessage(exception.getMessage()));
        }
        catch (SubscriptionConflictException exception) {
            return ResponseEntity.status(409).body(new ErrorMessage(exception.getMessage()));
        }
        return ResponseEntity.badRequest().build();
    }
}
