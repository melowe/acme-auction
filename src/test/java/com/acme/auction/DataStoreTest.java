
package com.acme.auction;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;


public class DataStoreTest {
    

    @Test
    public void createFromServiceLoader() {
        
        DataStore dataStore = DataStore.create();
        assertThat(dataStore).isInstanceOf(MockDataStore.class);
 
    }
    
}
