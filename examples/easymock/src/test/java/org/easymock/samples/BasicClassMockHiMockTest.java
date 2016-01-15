package org.easymock.samples;

import cn.michaelwang.himock.HiMockRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static cn.michaelwang.himock.HiMock.mock;
import static cn.michaelwang.himock.HiMock.verify;

@RunWith(HiMockRunner.class)
public class BasicClassMockHiMockTest {
    private Printer printer;
    private Document document;

    @Before
    public void setUp() {
        printer = mock(Printer.class);
        document = new Document(printer);
    }

    @Test
    public void testPrintContent() {
        document.setContent("Hello World");
        document.print();

        verify(() -> printer.print("Hello World"));
    }

    @Test
    public void testPrintEmptyContent() {
        document.setContent("");
        document.print();

        verify(() -> printer.print(""));
    }
}
