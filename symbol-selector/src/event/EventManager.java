package event;

import java.util.HashMap;
import java.util.Map;

public class EventManager {
    private static EventManager instance;
    private final Map<EventType, Observer> observers = new HashMap<>();

    private EventManager() {
        // singleton
    }

    public static synchronized EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    public void subscribe(EventType eventType, Observer observer) {
        observers.put(eventType, observer);
    }

    public void unsubscribe(EventType eventType) {
        observers.remove(eventType);
    }

    public void fireEvent(String hierarchy) {
        for (Map.Entry<EventType, Observer> entry : observers.entrySet()) {
            if(entry.getKey().equals(EventType.UI_REDRAW))
                entry.getValue().update(hierarchy);
        }
    }
}
