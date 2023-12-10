package search;

public class DataSearch {

    private String text;
    private String hierarchy;
    private boolean inHistory;

    public DataSearch() {
    }

    public DataSearch(String text, String hierarchy, boolean inHistory) {
        this.text = text;
        this.hierarchy = hierarchy;
        this.inHistory = inHistory;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isInHistory() {
        return inHistory;
    }

    public void setInHistory(boolean inHistory) {
        this.inHistory = inHistory;
    }

    public String getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
    }
}
