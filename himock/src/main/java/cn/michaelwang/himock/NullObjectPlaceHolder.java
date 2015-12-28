package cn.michaelwang.himock;

public class NullObjectPlaceHolder {
    private static NullObjectPlaceHolder instance;

    private NullObjectPlaceHolder() {
    }

    public static NullObjectPlaceHolder getInstance() {
        if (instance == null) {
            instance = new NullObjectPlaceHolder();
        }

        return instance;
    }
}
