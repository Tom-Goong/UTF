package com.yaoting.utf.domain.common;

public interface LifeCycle {

    default boolean isWorking() {
        return isStateOf(ServiceState.WORKING);
    }

    default boolean isStateOf(ServiceState state) {
        return getState() == state;
    }

    default void toState(ServiceState state) {
        setState(state);
    }

    ServiceState getState();

    void setState(ServiceState state);

    default void start() {

    }

    default void stop() {
    }
}
