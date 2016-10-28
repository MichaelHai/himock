package cn.michaelwang.himock;

import org.junit.Rule;
import org.junit.Test;

import cn.michaelwang.himock.process.verifiers.VerificationFailedReporter;

import static cn.michaelwang.himock.HiMock.*;

/*
 * The tests in this class should be run together. Any single case in this test class should pass.
 * However, without the HiMockTestRule, the second test will fail when run after the first test.
 */
@SuppressWarnings({"CodeBlock2Expr", "Convert2MethodRef"})
public class HiMockRuleTest {
    @Rule
    public HiMockTestRule rule = new HiMockTestRule();

    @SuppressWarnings("Duplicates")
    @Test(expected = VerificationFailedReporter.class)
    public void testTwoMockObjectOfTheSameInterfaceBothExpectedAndCalledFromWrongObjectShouldFail() {
        MockedInterface dummy1 = mock(MockedInterface.class);
        MockedInterface dummy2 = mock(MockedInterface.class);

        expect(() -> {
            dummy1.doNothing();
            dummy2.returnInt();
            willReturn(1);
        });

        dummy2.doNothing();
        dummy1.returnInt();

        verify();
    }

    @Test
    public void testVerifyCalledInvocationShouldPass() {
        MockedInterface dummy = mock(MockedInterface.class);

        expect(() -> {
            dummy.doNothing();
        });

        dummy.doNothing();

        verify();
    }
}
