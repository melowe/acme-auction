package com.acme.auction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.Objects;

public class Bid implements Comparable<Bid> {

    private final BigDecimal amount;

    private final User user;

    private final Item item;

    private final Instant executionTime;

    private Bid(BigDecimal amount, User user, Item item, Instant executionTime) {
        this.amount = amount;
        this.user = user;
        this.item = item;
        this.executionTime = executionTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public User getUser() {
        return user;
    }

    public Item getItem() {
        return item;
    }

    public Instant getExecutionTime() {
        return executionTime;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.amount);
        hash = 47 * hash + Objects.hashCode(this.user);
        hash = 47 * hash + Objects.hashCode(this.item);
        hash = 47 * hash + Objects.hashCode(this.executionTime);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bid other = (Bid) obj;
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.item, other.item)) {
            return false;
        }
        return Objects.equals(this.executionTime, other.executionTime);
    }

    @Override
    public String toString() {
       return String.join("/", item.getId(),
                amount.stripTrailingZeros().toPlainString(),
                user.getId(),
                executionTime.toString()
                
       );
    }

    @Override
    public int compareTo(Bid o) {
        return Comparator
                .comparing(Bid::getItem)
                .thenComparing(Bid::getAmount)
                .compare(o, this);
    }

    
    
    public static class Builder {

        private BigDecimal amount;

        private User user;

        private Item item;

        private Instant executionTime;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Builder withItem(Item item) {
            this.item = item;
            return this;
        }

        public Builder withExecutionTime(Instant executionTime) {
            this.executionTime = executionTime;
            return this;
        }

        public Bid build() {
            Objects.requireNonNull(amount, "Amount is required");
            Objects.requireNonNull(user, "User is required");
            Objects.requireNonNull(item, "Item is required");
            Objects.requireNonNull(executionTime, "executionTime is required");
            return new Bid(amount, user, item, executionTime);
        }

    }

}
