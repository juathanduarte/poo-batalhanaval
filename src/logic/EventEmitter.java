package logic;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

public class EventEmitter<T> {

    private List<Consumer<T>> listeners = new ArrayList<>();

    public void addListener(Consumer<T> listener) {
        this.listeners.add(listener);
    }

    public void emit(T value) {
        for (var listener : listeners) {
            listener.accept(value);
        }
    }
}