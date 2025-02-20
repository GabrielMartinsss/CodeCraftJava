package com.gabriel.events.repository;

import com.gabriel.events.dto.SubscriptionRanking;
import com.gabriel.events.model.Event;
import com.gabriel.events.model.Subscription;
import com.gabriel.events.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
    public Subscription findByEventAndSubscriber(Event event, User user);

    @Query(value = "select count(subscription_number) as quantity, indication_user_id, user_name" +
            " from tbl_subscription inner join tbl_user" +
            " on tbl_subscription.indication_user_id = tbl_user.user_id " +
            " where indication_user_id is not null" +
            "    and event_id = :eventId" +
            " group by indication_user_id" +
            " order by quantity desc", nativeQuery = true)
    public List<SubscriptionRanking> generateRanking(@Param("eventId") Integer eventId);
}
