package cn.michaelwang.himock;

public class NullObjectPlaceHolder {
    private NullObjectPlaceHolder(){}

    private static NullObjectPlaceHolder instance;
    public static NullObjectPlaceHolder getInstance() {
        if (instance == null) {
            instance = new NullObjectPlaceHolder();
        }

        return instance;
    }
}
