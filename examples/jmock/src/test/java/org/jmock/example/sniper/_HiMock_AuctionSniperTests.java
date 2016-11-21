package org.jmock.example.sniper;

import cn.michaelwang.himock.HiMockRunner;
import cn.michaelwang.himock.annotations.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static cn.michaelwang.himock.HiMock.*;

@RunWith(HiMockRunner.class)
public class _HiMock_AuctionSniperTests {
    Money increment = new Money(2);
    Money maximumBid = new Money(20);
    Money beatableBid = new Money(10);
    Money unbeatableBid = maximumBid.add(new Money(1));

    @Mock
    Auction auction;
    @Mock
    AuctionSniperListener listener;

    AuctionSniper sniper;

    @Before
    public void setup() {
        sniper = new AuctionSniper(auction, increment, maximumBid, listener);
    }

    @Test
    public void testTriesToBeatTheLatestHighestBid() {
        final Money expectedBid = beatableBid.add(increment);

        sniper.bidAccepted(auction, beatableBid);

        verify(() -> auction.bid(expectedBid));
    }

    @Test
    public void testWillNotBidPriceGreaterThanMaximum() {
        sniper.bidAccepted(auction, unbeatableBid);

        verify(() -> {
            auction.bid(match(any()));
            never();
        });
    }

    @Test
    public void testWillLimitBidToMaximum() {
        sniper.bidAccepted(auction, maximumBid.subtract(new Money(1)));

        verify(() -> {
            auction.bid(maximumBid);
            times(1);
        });
    }

    @Test
    public void testWillNotBidWhenToldAboutBidsOnOtherItems(@Mock Auction otherLot) {
        sniper.bidAccepted(otherLot, beatableBid);

        verify(() -> {
            otherLot.bid(new Money(10));
            never();
        });
    }

    @Test
    public void testWillAnnounceItHasFinishedIfPriceGoesAboveMaximum() {
        sniper.bidAccepted(auction, unbeatableBid);

        verify(() -> {
            listener.sniperFinished(sniper);
            times(1);
        });
    }

    @Test
    public void testCatchesExceptionsAndReportsThemToErrorListener() {
        final AuctionException exception = new AuctionException("test");

        expect(() -> {
            auction.bid(match(any()));
            willThrow(exception);

            listener.sniperBidFailed(sniper, exception);
            times(1);
        });

        sniper.bidAccepted(auction, beatableBid);
    }
}
