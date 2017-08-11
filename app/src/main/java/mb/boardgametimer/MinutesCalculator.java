package mb.boardgametimer;

public class MinutesCalculator {

    private final long duration;

    public MinutesCalculator(long duration) {
        this.duration = duration;
    }

    int getMinutes(){
        double seconds = ((double) duration) / 1000d;
        double minutes = seconds / 60d;

        return (int) Math.ceil(minutes);
    }
}
