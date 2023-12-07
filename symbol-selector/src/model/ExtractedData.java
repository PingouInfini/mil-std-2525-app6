package model;

import java.util.Map;

public class ExtractedData {
    private NodeAPP6 node;
    private Map<String, String> mapDescriptionHierarchy;

    public ExtractedData(NodeAPP6 node, Map<String, String> mapDescriptionHierarchy) {
        this.node = node;
        this.mapDescriptionHierarchy = mapDescriptionHierarchy;
    }

    public NodeAPP6 getNode() {
        return node;
    }

    public void setNode(NodeAPP6 node) {
        this.node = node;
    }

    public Map<String, String> getMapDescriptionHierarchy() {
        return mapDescriptionHierarchy;
    }

    public void setMapDescriptionHierarchy(Map<String, String> mapDescriptionHierarchy) {
        this.mapDescriptionHierarchy = mapDescriptionHierarchy;
    }
}
