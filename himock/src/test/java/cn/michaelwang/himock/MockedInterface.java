package cn.michaelwang.himock;

public interface MockedInterface {
    void doNothing();

    int returnInt();

    int returnInt(int i);

    boolean returnBoolean();

    Object returnObject();

    void withOneIntArgument(int i);

    void withMultipleIntArguments(int i, int i1);

    void withObjectArguments(String o1, String o2);

    int withStringArgument(String string);

    void withBooleanParameter(boolean b);

    int throwException() throws UserException;

    UserUncheckedException returnUserUncheckedException();

    UserException returnUserException();

    UserException returnUserExceptionAndThrow() throws Exception;
}
