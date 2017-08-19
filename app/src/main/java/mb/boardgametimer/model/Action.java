package mb.boardgametimer.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Action {
    private final ActionType type;
    private final long timestamp;

    public Action(ActionType type, long timestamp) {
        this.type = type;
        this.timestamp = timestamp;
    }

    public ActionType getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    private String getFormattedDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss", Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    @Override
    public String toString() {
        return String.format("%s %s", type.toString(), getFormattedDate(timestamp));
    }
}
