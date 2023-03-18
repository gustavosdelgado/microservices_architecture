package io.github.gustavosdelgado.amqp;

public interface Listener<T> {

    public void listen(T t);

}
