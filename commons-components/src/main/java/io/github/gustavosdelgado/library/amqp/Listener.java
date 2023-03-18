package io.github.gustavosdelgado.library.amqp;

public interface Listener<T> {

    public void listen(T t);

}
