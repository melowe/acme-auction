
package com.acme.auction;

import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Predicate;


public interface DataStore {
    
    void saveBid(Bid bid);
    
    List<Bid> findBids(Predicate<Bid> predicate);
    
    static DataStore create() {
        return ServiceLoader.load(DataStore.class)
                .iterator().next();
    }
    
}
