package poo2025_1.spaceinvaders.core;

import javafx.event.Event;
import javafx.event.EventType;

public class GameEvent extends Event {

    public static final EventType<GameEvent> GAME_OVER = new EventType<>(Event.ANY, "GAME_OVER");

    public GameEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}