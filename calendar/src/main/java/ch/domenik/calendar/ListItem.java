package ch.domenik.calendar;

public class ListItem {
    private String date;
    private String text;
    private String type;

    public ListItem(String date, String text, String type) {
        this.date = date;
        this.text = text;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

}
