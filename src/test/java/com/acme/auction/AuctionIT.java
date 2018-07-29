package com.acme.auction;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.SortedSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuctionIT {

    private SortedSet<Bid> bids;

    private Auction auction;

    private static final int THREADS = 1000;

    private static CountDownLatch countDownLatch;

    private ExecutorService executorService;

    public AuctionIT() {
    }

    @Before
    public void setUp() {
        bids = new ConcurrentSkipListSet<>();
        countDownLatch = new CountDownLatch(THREADS);
        auction = new Auction(new DefaultDataStore(bids));
        executorService = Executors.newFixedThreadPool(THREADS);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void execute() throws Exception {

        Random random = new Random();

        List<User> someusers = someUsers();
        Collections.shuffle(someusers);

        List<Item> someitems = someItems();
        Collections.shuffle(someitems);

        List<BidAction> actions = IntStream.range(0, THREADS)
                .mapToObj(i -> new BidAction(
                auction,
                someusers.get(random.nextInt(someusers.size() - 1)),
                BigDecimal.valueOf(random.nextDouble()),
                someitems.get(random.nextInt(someitems.size() - 1))))
                .collect(Collectors.toList());

        Map<Item, BidAction> highestBids = actions.stream()
                .collect(Collectors.toMap(
                        BidAction::getItem,
                        Function.identity(),
                        (o1, o2) -> o1.amount.compareTo(o2.amount) >= 0 ? o1 : o2
                ));

        Collections.shuffle(actions);

        executorService.invokeAll(actions);

        countDownLatch.await(30, TimeUnit.SECONDS);

        executorService.shutdown();

        assertThat(bids).hasSize(THREADS);

        Item item = actions.stream().map(BidAction::getItem).findAny().get();

        Bid highestBidForItem = bids.stream()
                .filter(b -> Objects.equals(b.getItem(), item))
                .sorted(Comparator.comparing(Bid::getAmount).reversed())
                .findFirst()
                .get();


        Bid winningBid = auction.findWinningBid(item).get();
        
        assertThat(highestBidForItem).isEqualTo(winningBid);
        
        assertThat(highestBidForItem.getAmount())
                .isEqualTo(highestBids.get(item).getAmount());
        
        assertThat(highestBidForItem.getUser())
                .isEqualTo(highestBids.get(item).getUser());

        
    }

    static class BidAction implements Callable<Bid> {

        private final Auction auction;

        private final User user;

        private final BigDecimal amount;

        private final Item item;

        public BidAction(Auction auction, User user, BigDecimal amount, Item item) {
            this.user = user;
            this.amount = amount;
            this.item = item;
            this.auction = auction;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public Item getItem() {
            return item;
        }

        public User getUser() {
            return user;
        }

        @Override
        public Bid call() throws Exception {

            try {
                return auction.executeBid(user, amount, item);
            } finally {
                countDownLatch.countDown();
            }
        }

        @Override
        public String toString() {
            return String.join("/", item.getId(),
                    amount.stripTrailingZeros().toPlainString(),
                    user.getId()
            );
        }

    }

    static List<Item> someItems() {
        return IntStream.range(0, 10)
                .mapToObj(i -> Item.Builder.create()
                .withId("ITEM" + i)
                .withDescription("Some item" + i)
                .build())
                .collect(Collectors.toList());
    }

    static List<User> someUsers() {
        return IntStream.range(0, 10)
                .mapToObj(i -> User.Builder.create()
                .withId("USER" + i)
                .withName("Some user" + i)
                .build())
                .collect(Collectors.toList());
    }

}
