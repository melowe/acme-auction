package com.acme.auction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Auction {

    private final DataStore dataStore;

    public Auction(DataStore dataStore) {
        this.dataStore = Objects.requireNonNull(dataStore);
    }

    public Bid executeBid(User user, BigDecimal amount,Item item) {

        final Bid bid = Bid.Builder.create()
                .withAmount(amount)
                .withUser(user)
                .withItem(item)
                .withExecutionTime(Instant.now())
                .build();

        dataStore.saveBid(bid);

        return bid;
    }

    public List<Bid> findBidsForUser(User user) {
        Predicate<Bid> matchUser = b -> b.getUser().equals(user);
        return dataStore.findBids(matchUser);
    }
    
    public Set<Item> findItemsBidOnByUser(User user) {
        return findBidsForUser(user)
                .stream()
                .map(Bid::getItem)
                .collect(Collectors.toSet());
    }
    
    public Optional<Bid> findWinningBid(Item item) {
        Predicate<Bid> matchItem = b -> b.getItem().equals(item);
        return dataStore
                .findBids(matchItem)
                .stream()
                .sorted()
                .findFirst();
    }
    

}
