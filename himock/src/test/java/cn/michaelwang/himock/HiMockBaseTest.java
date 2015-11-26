package cn.michaelwang.himock;

import cn.michaelwang.himock.report.HiMockReporter;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class HiMockBaseTest {
    protected HiMock mock;

    @Before
    public void setup() {
        mock = new HiMock();
    }

    @FunctionalInterface
    interface TestProcess {
        void doTest();
    }

    protected void reportTest(TestProcess test, String expectedMessage) {
        try {
            test.doTest();
        } catch (HiMockReporter reporter) {
            assertStringEqualWithWildcardCharacter(expectedMessage, reporter.getMessage());
            return;
        }

        fail("The test should return in the catch block");
    }

    private void assertStringEqualWithWildcardCharacter(String expected, String actually) {
        String[] splitted = expected.split("\\?");

        boolean first = true;
        int start;
        int end = 0;
        for (String sub : splitted) {
            start = actually.indexOf(sub, end);
            end = start + sub.length();

            if (first) {
                if (start != 0) {
                    fail("expected:\n" + expected + "\n" + "actually:\n" + actually);
                    return;
                }
                first = false;
            }

            if (start == -1) {
                fail("expected:\n" + expected + "\n" + "actually:\n" + actually);
                return;
            }
        }

        for (int i = end; i < actually.length(); i++) {
            assertEquals('?', actually.charAt(i));
        }
    }
}
