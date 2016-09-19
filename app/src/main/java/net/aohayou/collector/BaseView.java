package net.aohayou.collector;

public interface BaseView<T> {

    void setPresenter(T presenter);

    boolean isActive();
}
