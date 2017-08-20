package mb.boardgametimer;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import mb.boardgametimer.model.Action;
import mb.boardgametimer.model.ActionType;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class MinutesCalculatorTest {

    private static final long MINUTE = 60L * 1000L;
    private final MinutesCalculator calculator = new MinutesCalculator();

    @Test
    public void shouldNotFailWhenActionsAreNull() {
        int result = calculator.getMinutes(null);

        assertThat(result).isEqualTo(0);
    }

    @Test
    public void shouldNotFailWhenActionsAreEmpty() {
        int result = calculator.getMinutes(new ArrayList<Action>());

        assertThat(result).isEqualTo(0);
    }

    @Test
    public void shouldProperlyComputeSimpleActions() {
        List<Action> actions = new ArrayList<>();
        actions.add(new Action(ActionType.STOP, 2L * MINUTE));
        actions.add(new Action(ActionType.START, MINUTE));

        int result = calculator.getMinutes(actions);

        assertThat(result).isEqualTo(1);
    }

    @Test
    public void shouldProperlyComputeWithStopAfterPause() {
        List<Action> actions = new ArrayList<>();
        actions.add(new Action(ActionType.STOP, 10L * MINUTE));
        actions.add(new Action(ActionType.PAUSE, 2L * MINUTE));
        actions.add(new Action(ActionType.START, MINUTE));

        int result = calculator.getMinutes(actions);

        assertThat(result).isEqualTo(1);
    }

    @Test
    public void shouldProperlyComputeWithResumeAfterPause() {
        List<Action> actions = new ArrayList<>();
        actions.add(new Action(ActionType.STOP, 11L * MINUTE));
        actions.add(new Action(ActionType.RESUME, 10L * MINUTE));
        actions.add(new Action(ActionType.PAUSE, 2L * MINUTE));
        actions.add(new Action(ActionType.START, MINUTE));

        int result = calculator.getMinutes(actions);

        assertThat(result).isEqualTo(2);
    }

    @Test
    public void shouldProperlyComputeWithResumeAfterStart() {
        List<Action> actions = new ArrayList<>();
        actions.add(new Action(ActionType.STOP, 3L * MINUTE));
        actions.add(new Action(ActionType.RESUME, 2L * MINUTE));
        actions.add(new Action(ActionType.START, MINUTE));

        int result = calculator.getMinutes(actions);

        assertThat(result).isEqualTo(2);
    }

    @Test
    public void shouldProperlyComputeWithMultiplePausesAndResumes() {
        List<Action> actions = new ArrayList<>();
        actions.add(new Action(ActionType.STOP, 21L * MINUTE));
        actions.add(new Action(ActionType.RESUME, 20L * MINUTE));
        actions.add(new Action(ActionType.PAUSE, 11L * MINUTE));
        actions.add(new Action(ActionType.RESUME, 10L * MINUTE));
        actions.add(new Action(ActionType.PAUSE, 2L * MINUTE));
        actions.add(new Action(ActionType.START, MINUTE));

        int result = calculator.getMinutes(actions);

        assertThat(result).isEqualTo(3);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenLastActionIsNotStop() {
        final List<Action> actions = new ArrayList<>();
        actions.add(new Action(ActionType.START, MINUTE));

        calculator.getMinutes(actions);
    }

    @Test
    public void shouldProperlyCeilResult() {
        List<Action> actions = new ArrayList<>();
        actions.add(new Action(ActionType.STOP, 2L * MINUTE + 1));
        actions.add(new Action(ActionType.START, MINUTE));

        int result = calculator.getMinutes(actions);

        assertThat(result).isEqualTo(2);
    }
}
