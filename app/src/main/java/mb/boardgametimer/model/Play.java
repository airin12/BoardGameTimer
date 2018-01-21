package mb.boardgametimer.model;

public class Play {
    private final int duration;
    private final String text;
    private final long timestamp;

    public Play(int duration, String text, long timestamp) {
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

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s m: %s", duration, text);
    }
}
