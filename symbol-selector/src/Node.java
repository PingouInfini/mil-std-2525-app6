import java.util.ArrayList;
import java.util.List;

public class Node {
    private String hierarchy;
    private String symbolCode;
    private String name;
    private String nameFR;
    private Node parent;
    private List<Node> children;

    public Node() {
        this.hierarchy = "";
        this.symbolCode = "";
        this.name = "";
        this.nameFR = "";
        this.parent = null;
        this.children = new ArrayList<>();
    }

    public Node(String hierarchy, String symbolCode, String name, String nameFR) {
        this.hierarchy = hierarchy;
        this.symbolCode = symbolCode;
        this.name = name;
        this.nameFR = nameFR;
        this.parent = null;
        this.children = new ArrayList<>();
    }

    public String getHierarchy() {
        return hierarchy;
    }

    public String getSymbolCode() {
        return symbolCode;
    }

    public String getName() {
        return name;
    }

    public String getNameFR() {
        return nameFR;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void addChild(Node child) {
        child.setParent(this);
        children.add(child);
    }

    public Node findNodeByHierarchy(String hierarchy) {
        if (this.hierarchy.equals(hierarchy)) {
            return this;
        }

        for (Node child : children) {
            Node foundNode = child.findNodeByHierarchy(hierarchy);
            if (foundNode != null) {
                return foundNode;
            }
        }

        return null;
    }

    public Node getParentNodeByHierarchy(String hierarchy) {
        Node node = findNodeByHierarchy(hierarchy);
        if (node != null) {
            return node.getParent();
        }

        return null;
    }

    public List<Node> getChildrenNodesByHierarchy(String hierarchy) {
        Node node = findNodeByHierarchy(hierarchy);
        if (node != null) {
            return node.getChildren();
        }

        return new ArrayList<>();
    }
}
