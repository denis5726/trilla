package ru.trilla.masking;

public interface ObjectMasker {

    boolean isAcceptable(Object o);

    Object mask(Object o);
}
