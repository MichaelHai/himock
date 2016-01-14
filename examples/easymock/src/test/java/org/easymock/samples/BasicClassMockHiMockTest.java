package org.easymock.samples;

import cn.michaelwang.himock.HiMock;
import org.junit.Before;
import org.junit.Test;

public class BasicClassMockHiMockTest {
    private Printer printer;
    private Document document;
    private HiMock mock = new HiMock();

    @Before
    public void setUp() {
        printer = mock.mock(Printer.class);
        document = new Document(printer);
    }

    @Test
    public void testPrintContent() {
        document.setContent("Hello World");
        document.print();

        mock.verify(() -> printer.print("Hello World"));
    }

    @Test
    public void testPrintEmptyContent() {
        document.setContent("");
        document.print();

        mock.verify(() -> printer.print(""));
    }
}
