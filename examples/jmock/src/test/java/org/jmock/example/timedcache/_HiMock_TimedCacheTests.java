package org.jmock.example.timedcache;

import cn.michaelwang.himock.HiMockRunner;
import cn.michaelwang.himock.annotations.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;

import static cn.michaelwang.himock.HiMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(HiMockRunner.class)
public class _HiMock_TimedCacheTests {
    final private Object KEY = "key";
    final private Object VALUE = "value";
    final private Object NEW_VALUE = "newValue";

    @Mock
    private Clock clock;
    @Mock
    private ObjectLoader loader;
    @Mock
    private ReloadPolicy reloadPolicy;

    private TimedCache cache;

    private Date loadTime = time(1);
    private Date fetchTime = time(2);

    @Before
    public void setup() {
        cache = new TimedCache(loader, clock, reloadPolicy);
    }

    @Test
    public void testLoadsObjectThatIsNotCached() {
        final Object VALUE1 = "value1";
        final Object VALUE2 = "value2";

        expect(() -> {
            clock.time();
            willReturn(loadTime);

            loader.load("key1");
            willReturn(VALUE1);
            times(1);

            loader.load("key2");
            willReturn(VALUE2);
            times(1);
        });

        Object actualValue1 = cache.lookup("key1");
        Object actualValue2 = cache.lookup("key2");

        verify();
        assertEquals("lookup with key1", VALUE1, actualValue1);
        assertEquals("lookup with key2", VALUE2, actualValue2);
    }

    @Test
    public void testReturnsCachedObjectWithinTimeout() {
        expect(() -> {
            clock.time();
            willReturn(loadTime);
            times(1);

            clock.time();
            willReturn(fetchTime);
            times(1);

            reloadPolicy.shouldReload(loadTime, fetchTime);
            willReturn(false);

            loader.load(KEY);
            willReturn(VALUE);
            times(1);
        });

        Object actualValueFromFirstLookup = cache.lookup(KEY);
        Object actualValueFromSecondLookup = cache.lookup(KEY);

        verify();
        assertSame("should be loaded object", VALUE, actualValueFromFirstLookup);
        assertSame("should be cached object", VALUE, actualValueFromSecondLookup);
    }

    @Test
    public void testReloadsCachedObjectAfterTimeout() {
        expect(() -> {
            reloadPolicy.shouldReload(loadTime, fetchTime);
            willReturn(true);

            clock.time();
            willReturn(loadTime);
            times(1);

            loader.load(KEY);
            willReturn(VALUE);
            times(1);

            clock.time();
            willReturn(fetchTime);
            times(1);

            loader.load(KEY);
            willReturn(NEW_VALUE);
            times(1);
        });

        Object actualValueFromFirstLookup = cache.lookup(KEY);
        Object actualValueFromSecondLookup = cache.lookup(KEY);

        verify();

        assertSame("should be loaded object", VALUE, actualValueFromFirstLookup);
        assertSame("should be reloaded object", NEW_VALUE, actualValueFromSecondLookup);
    }


    private Date time(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DAY_OF_YEAR, i);
        return calendar.getTime();
    }
}
