package event;

import java.util.HashMap;
import java.util.Map;

public class EventManager {
    private static EventManager instance;
    private Map<EventType, Observer> observers = new HashMap<>();

    private EventManager() {
        // Empêche l'instanciation directe de la classe
    }

    public static synchronized EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    // Méthode pour s'abonner à un événement
    public void subscribe(EventType eventType, Observer observer) {
        observers.put(eventType, observer);
    }

    // Méthode pour se désabonner d'un événement
    public void unsubscribe(EventType eventType) {
        observers.remove(eventType);
    }

    // Méthode pour déclencher un événement
    public void fireEvent(String hierarchy) {

        for (Map.Entry<EventType, Observer> entry : observers.entrySet()) {
            if(entry.getKey().equals(EventType.UI_REDRAW))
                entry.getValue().update(hierarchy);
        }
    }
}
