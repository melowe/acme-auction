
package com.acme.auction;

import java.util.List;
import java.util.function.Predicate;

/*
Only used for service loader lookup test for DataStore.create();
*/
public class MockDataStore implements DataStore {

    @Override
    public void saveBid(Bid bid) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public List<Bid> findBids(Predicate<Bid> predicate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
