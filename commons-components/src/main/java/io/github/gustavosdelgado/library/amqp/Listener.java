package io.github.gustavosdelgado.library.amqp;

public interface Listener<T> {

    void listen(T t);

}
