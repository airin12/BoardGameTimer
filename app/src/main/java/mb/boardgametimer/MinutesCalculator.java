package mb.boardgametimer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mb.boardgametimer.model.Action;

class MinutesCalculator {

    int getMinutes(List<Action> actions) {
        long duration = getDuration(actions);

        double seconds = ((double) duration) / 1000d;
        double minutes = seconds / 60d;

        return (int) Math.ceil(minutes);
    }

    private long getDuration(List<Action> actions) {
        long duration = 0L;
        if (actions == null) {
            return duration;
        }

        List<Action> reversedActions = new ArrayList<>(actions);
        Collections.reverse(reversedActions);

        long start = 0L;
        for (Action action : reversedActions) {
            switch (action.getType()) {
                case START:
                case RESUME:
                    if (start == 0) {
                        start = action.getTimestamp();
                    }
                    break;
                case STOP:
                case PAUSE:
                    if (start != 0) {
                        duration += action.getTimestamp() - start;
                        start = 0L;
                    }
                    break;
            }
        }

        return duration;
    }
}
