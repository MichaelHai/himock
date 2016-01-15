package cn.michaelwang.himock;

import cn.michaelwang.himock.report.HiMockReporter;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(HiMockRunner.class)
public abstract class HiMockBaseTest {
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

            if (first) {
                if (start != 0) {
                    assertEquals(expected, actually);
                    return;
                }
                first = false;
            } else {
                if (start == -1) {
                    assertEquals(expected, actually);
                    return;
                } else {
                    String number = actually.substring(end, start);
                    if (!number.matches("[0-9]+")) {
                        assertEquals(expected, actually);
                        return;
                    }
                }
            }

            end = start + sub.length();
        }

        for (int i = end; i < actually.length(); i++) {
            if (actually.charAt(i) != '?') {
                assertEquals(expected, actually);
            }
        }
    }

    @FunctionalInterface
    interface TestProcess {
        void doTest();
    }
}
