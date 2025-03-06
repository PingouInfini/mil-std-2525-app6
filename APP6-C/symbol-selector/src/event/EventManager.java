package event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventManager {
    private static EventManager instance;
    private final Map<EventType, Set<Observer>> observers = new HashMap<>();

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
        observers.computeIfAbsent(eventType, k -> new HashSet<>()).add(observer);
    }

    public void unsubscribe(EventType eventType, Observer observer) {
        if (observers.containsKey(eventType)) {
            Set<Observer> values = observers.get(eventType);
            values.remove(observer);

            if (values.isEmpty()) {
                observers.remove(eventType);
            }
        }
    }

    public void fireEvent(String hierarchy) {
        for (Map.Entry<EventType, Set<Observer>> entry : observers.entrySet()) {
            if (entry.getKey().equals(EventType.UI_REDRAW)) {
                for (Observer observer : entry.getValue())
                    observer.update(hierarchy);
            }
        }
    }
}
