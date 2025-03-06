package model;

import java.util.ArrayList;
import java.util.List;

public class NodeAPP6
{
    private final String hierarchy;
    private final String symbolCode;
    private final String name;
    private final String nameFR;
    private final String fullPath;
    private NodeAPP6 parent;
    private final List<NodeAPP6> children;

    public NodeAPP6()
    {
        this.hierarchy = "";
        this.symbolCode = "";
        this.name = "";
        this.nameFR = "";
        this.fullPath = "";
        this.parent = null;
        this.children = new ArrayList<>();
    }

    public NodeAPP6(final String hierarchy, final String symbolCode, final String name, final String nameFR, final String fullPath)
    {
        this.hierarchy = hierarchy;
        this.symbolCode = symbolCode;
        this.name = name;
        this.nameFR = nameFR;
        this.fullPath = fullPath;
        this.parent = null;
        this.children = new ArrayList<>();
    }

    public String getHierarchy()
    {
        return this.hierarchy;
    }

    public String getSymbolCode()
    {
        return this.symbolCode;
    }

    public String getName()
    {
        return this.name;
    }

    public String getNameFR()
    {
        return this.nameFR;
    }

    public NodeAPP6 getParent()
    {
        return this.parent;
    }

    public void setParent(final NodeAPP6 parent)
    {
        this.parent = parent;
    }

    public List<NodeAPP6> getChildren()
    {
        return this.children;
    }

    public String getFullPath()
    {
        return this.fullPath;
    }

    public void addChild(final NodeAPP6 child)
    {
        child.setParent(this);
        this.children.add(child);
    }

    public NodeAPP6 findNodeByHierarchy(final String hierarchy)
    {
        if (this.hierarchy.equals(hierarchy))
        {
            return this;
        }

        for (final NodeAPP6 child : this.children)
        {
            final NodeAPP6 foundNode = child.findNodeByHierarchy(hierarchy);
            if (foundNode != null)
            {
                return foundNode;
            }
        }

        return null;
    }

    public NodeAPP6 getParentNodeByHierarchy(final String hierarchy)
    {
        final NodeAPP6 node = findNodeByHierarchy(hierarchy);
        if (node != null)
        {
            return node.getParent();
        }

        return null;
    }

    public List<NodeAPP6> getChildrenNodesByHierarchy(final String hierarchy)
    {
        final NodeAPP6 node = findNodeByHierarchy(hierarchy);
        if (node != null)
        {
            return node.getChildren();
        }

        return new ArrayList<>();
    }
}
