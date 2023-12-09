package search;

public class DataSearch {

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

    public DataSearch(String text, boolean inHistory) {
        this.text = text;
        this.inHistory = inHistory;
    }

    public DataSearch() {
    }

    private String text;
    private boolean inHistory;
}
