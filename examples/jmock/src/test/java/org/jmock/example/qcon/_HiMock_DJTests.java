package org.jmock.example.qcon;

import cn.michaelwang.himock.HiMockRunner;
import cn.michaelwang.himock.annotations.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static cn.michaelwang.himock.HiMock.*;

@SuppressWarnings("CodeBlock2Expr")
@RunWith(HiMockRunner.class)
public class _HiMock_DJTests {
    @Mock
    Playlist playlist;
    @Mock
    MediaControl mediaControl;

    DJ dj;

    private static final String LOCATION_A = "location-a";
    private static final String TRACK_A = "track-a";

    private static final String LOCATION_B = "location-b";
    private static final String TRACK_B = "track-b";

    @Before
    public void setUp() {
        // the field injection is done after the object is initialized.
        // In this case, the DJ object must be created in the setup
        dj = new DJ(playlist, mediaControl);

        weakExpect(() -> {
            playlist.hasTrackFor(LOCATION_A);
            willReturn(true);

            playlist.trackFor(LOCATION_A);
            willReturn(TRACK_A);

            playlist.hasTrackFor(LOCATION_B);
            willReturn(true);

            playlist.trackFor(LOCATION_B);
            willReturn(TRACK_B);

            playlist.hasTrackFor(match(any()));
            willReturn(false);
        });
    }

    @Test
    public void testStartPlayingTrackForCurrentLocationWhenLocationFirstDetected() {
        dj.locationChangedTo(LOCATION_A);

        verify(() -> {
            mediaControl.play(TRACK_A);
        });
    }

    @Test
    public void testPlaysTrackForCurrentLocationWhenPreviousTrackFinishesIfLocationChangedWhileTrackWasPlaying() {
        startingIn(LOCATION_A);

        dj.locationChangedTo(LOCATION_B);

        dj.mediaFinished();

        verify(() -> {
           mediaControl.play(TRACK_B);
        });
    }

    @Test
    public void testDoesNotPlayTrackAgainIfStillInTheSameLocation() {
        startingIn(LOCATION_A);

        dj.mediaFinished();

        verify(() -> {
            mediaControl.play(match(any()));
            times(1);
        });
    }

    @Test
    public void testPlaysNewTrackAsSoonAsLocationChangesIfPreviousTrackFinishedWhileInSameLocation() {
        startingIn(LOCATION_A);
        dj.mediaFinished();

        dj.locationChangedTo(LOCATION_B);

        verify(() -> {
            mediaControl.play(TRACK_B);
        });
    }

    private void startingIn(String initialLocation) {
        dj.locationChangedTo(initialLocation);
    }
}
