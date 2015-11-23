package cn.michaelwang.himock;

public interface MockedInterface {
    void doNothing();

    int returnInt();

    boolean returnBoolean();

    Object returnObject();

    void withOneIntParameter(int i);

    void withMultipleIntParameters(int i, int i1);

    void withObjectParameters(String o1, String o2);
}
