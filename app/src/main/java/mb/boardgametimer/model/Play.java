package mb.boardgametimer.model;

public class Play {
    private final int duration;
    private final String text;
    private final String timestamp;

    public Play(int duration, String text, String timestamp) {
        this.duration = duration;
        this.text = text;
        this.timestamp = timestamp;
    }

    public int getDuration() {
        return duration;
    }

    public String getText() {
        return text;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
