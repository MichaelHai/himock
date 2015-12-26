package cn.michaelwang.himock.verify;

import cn.michaelwang.himock.Invocation;

import java.util.List;

public interface Verification extends Invocation {
    boolean satisfyWith(Verifiable verifiable);
    boolean satisfyWith(List<? extends Verifiable> toBeVerified);
}
