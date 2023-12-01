package model;

import java.util.Map;

public class ExtractedData {
    private Node node;
    private Map<String, String> mapDescriptionHierarchy;

    public ExtractedData(Node node, Map<String, String> mapDescriptionHierarchy) {
        this.node = node;
        this.mapDescriptionHierarchy = mapDescriptionHierarchy;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Map<String, String> getMapDescriptionHierarchy() {
        return mapDescriptionHierarchy;
    }

    public void setMapDescriptionHierarchy(Map<String, String> mapDescriptionHierarchy) {
        this.mapDescriptionHierarchy = mapDescriptionHierarchy;
    }
}
