package com.acme.auction;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class UserTest {

    @Test
    public void createValid() {

        final String id = "USER1";
        final String name = "Lesley Crowther";

        final User user = User.Builder.create()
                .withId(id)
                .withName(name)
                .build();

        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("Lesley Crowther");
        assertThat(user.getId()).isEqualTo("USER1");

    }

    @Test
    public void usersWithSameIdAreEqual() {
        
        final String id = "USER1";
        final String name = "User One";

        final User user = User.Builder.create()
                .withId(id)
                .withName(name)
                .build();

        final User otherUser = User.Builder.create()
                .withId(id)
                .withName("Other User")
                .build();

        final User yetAnotherUser = User.Builder.create()
                .withId("OtherId")
                .withName(name)
                .build();

        assertThat(user)
                .isEqualTo(otherUser)
                .isNotEqualTo(yetAnotherUser);

        assertThat(user.hashCode()).isEqualTo(otherUser.hashCode());
        assertThat(user).isSameAs(user).isEqualTo(user);
        //coverage: check null is not equal
        assertThat(user).isNotEqualTo(null);
        //covergae: check different type is not equal
        assertThat(user).isNotEqualTo(mock(Object.class));
    }

}
