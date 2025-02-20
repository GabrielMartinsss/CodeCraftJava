package com.gabriel.events.service;

import com.gabriel.events.dto.SubscriptionRanking;
import com.gabriel.events.dto.SubscriptionRankingByUser;
import com.gabriel.events.dto.SubscriptionResponse;
import com.gabriel.events.exception.EventNotFoundException;
import com.gabriel.events.exception.SubscriptionConflictException;
import com.gabriel.events.exception.UserIndicatorNotFoundException;
import com.gabriel.events.model.Event;
import com.gabriel.events.model.Subscription;
import com.gabriel.events.model.User;
import com.gabriel.events.repository.EventRepository;
import com.gabriel.events.repository.SubscriptionRepository;
import com.gabriel.events.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class SubscriptionService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public SubscriptionResponse createNewSubscription(String eventPrettyName, User user, Integer userIndicatorId) {

        Event event = eventRepository.findByPrettyName(eventPrettyName);
        if (event == null) {
            throw new EventNotFoundException("Event not found with pretty name: " + eventPrettyName);
        }

        User userRecovered = userRepository.findByEmail(user.getEmail());
        if (userRecovered == null) {
            userRecovered = userRepository.save(user);
        }

        User indicator = null;
        if (userIndicatorId != null){
            indicator = userRepository.findById(userIndicatorId).orElse(null);
            if (indicator == null) {
                throw new UserIndicatorNotFoundException("user indicator doesn't exists!");
            }
        }

        Subscription subscription = new Subscription();
        subscription.setEvent(event);
        subscription.setSubscriber(userRecovered);
        subscription.setIndication(indicator);

        Subscription userAlreadySubscribed = subscriptionRepository.findByEventAndSubscriber(event, userRecovered);
        if (userAlreadySubscribed != null) {
            throw new SubscriptionConflictException(user.getEmail() + " already is subscribed in the " + event.getTitle());
        }
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        String baseURLResponse = "http://codecraft.com/"
                + savedSubscription.getEvent().getPrettyName()
                + "/" + savedSubscription.getSubscriptionNumber();

        return new SubscriptionResponse(savedSubscription.getSubscriptionNumber(), baseURLResponse);
    }

    public List<SubscriptionRanking> getCompleteRanking(String eventPrettyName) {
        Event event = eventRepository.findByPrettyName(eventPrettyName);
        if (event == null) {
            throw new EventNotFoundException("Event "+ eventPrettyName + " not found.");
        }
        return subscriptionRepository.generateRanking(event.getEventId());
    }

    public SubscriptionRankingByUser getRankingByUser(String prettyName, Integer userId) {
        List<SubscriptionRanking> ranking = getCompleteRanking(prettyName);
        SubscriptionRanking userFound = ranking
                .stream()
                .filter(item -> item.userId().equals(userId))
                .findFirst().orElse(null);
        if (userFound == null) {
            throw new UserIndicatorNotFoundException("The user doesn't indicate anybody.");
        }

        int position = IntStream.range(0, ranking.size())
                .filter(p -> ranking.get(p).userId().equals(userId))
                .findFirst().getAsInt();

        return new SubscriptionRankingByUser(userFound, position + 1);
    }
}
