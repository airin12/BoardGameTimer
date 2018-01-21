package mb.boardgametimer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;

import mb.boardgametimer.model.Action;
import mb.boardgametimer.model.ActionType;
import mb.boardgametimer.model.Play;
import mb.boardgametimer.sqlite.ActionReaderContract;
import mb.boardgametimer.sqlite.ActionReaderDbHelper;
import mb.boardgametimer.sqlite.PlayReaderDbHelper;

public class MainActivity extends AppCompatActivity {
    private Button startButton;
    private Button stopButton;
    private Button pauseButton;
    private Button resumeButton;

    private TextView durationTextView;

    private ActionReaderDbHelper actionDbHelper;
    private PlayReaderDbHelper playDbHelper;
    private LinkedList<Action> actions;
    private LinkedList<Play> plays;
    private ArrayAdapter<Action> actionAdapter;
    private ArrayAdapter<Play> playAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.start_button_id);
        stopButton = (Button) findViewById(R.id.stop_button_id);
        pauseButton = (Button) findViewById(R.id.pause_button_id);
        resumeButton = (Button) findViewById(R.id.resume_button_id);

        updateButtonsWithState(ActionType.STOP);

        durationTextView = (TextView) findViewById(R.id.duration_text_view_id);

        ListView actionListView = (ListView) findViewById(R.id.action_list_view_id);
        actions = new LinkedList<>();
        actionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, actions);
        actionListView.setAdapter(actionAdapter);

        ListView playListView = (ListView) findViewById(R.id.play_list_view_id);
        plays = new LinkedList<>();
        playAdapter = new ArrayAdapter<Play>(this, android.R.layout.simple_list_item_1, plays);
        playListView.setAdapter(playAdapter);

        actionDbHelper = new ActionReaderDbHelper(getApplicationContext());
        playDbHelper = new PlayReaderDbHelper(getApplicationContext());
    }

    private void updateButtonsWithState(ActionType type) {
        switch (type) {
            case RESUME:
            case START:
                startButton.setEnabled(false);
                resumeButton.setEnabled(false);
                stopButton.setEnabled(true);
                pauseButton.setEnabled(true);
                break;
            case STOP:
                startButton.setEnabled(true);
                resumeButton.setEnabled(false);
                stopButton.setEnabled(false);
                pauseButton.setEnabled(false);
                break;
            case PAUSE:
                startButton.setEnabled(false);
                resumeButton.setEnabled(true);
                stopButton.setEnabled(true);
                pauseButton.setEnabled(false);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actions.clear();

        loadActionsData();
    }

    private void loadActionsData() {
        SQLiteDatabase actionsDb = actionDbHelper.getReadableDatabase();

        String[] projection = {
                ActionReaderContract.ActionEntry.COLUMN_NAME_TYPE,
                ActionReaderContract.ActionEntry.COLUMN_NAME_TIMESTAMP
        };

        Cursor cursor = actionsDb.query(ActionReaderContract.ActionEntry.TABLE_NAME, projection, null, null, null, null, null);
        while (cursor.moveToNext()) {
            ActionType type = ActionType.valueOf(cursor.getString(
                    cursor.getColumnIndex(ActionReaderContract.ActionEntry.COLUMN_NAME_TYPE)));
            long timestamp = cursor.getLong(cursor.getColumnIndex(ActionReaderContract.ActionEntry.COLUMN_NAME_TIMESTAMP));
            actions.addFirst(new Action(type, timestamp));
        }
        cursor.close();
        actionAdapter.notifyDataSetChanged();

        updateButtonsWithState(actions.getFirst().getType());
        if (actions.getFirst().getType() == ActionType.STOP) {
            updateDurationTextViewWithMinutes();
        } else {
            resetDurationTextView();
        }
    }

    public void startMeasure(View view) {
        actions.clear();
        actionDbHelper.getWritableDatabase().delete(ActionReaderContract.ActionEntry.TABLE_NAME, null, null);
        long timestamp = System.currentTimeMillis();
        addNewActionAndUpdateDb(ActionType.START, timestamp);
        updateButtonsWithState(ActionType.START);
        resetDurationTextView();
    }

    public void stopMeasure(View view) {
        long timestamp = System.currentTimeMillis();
        addNewActionAndUpdateDb(ActionType.STOP, timestamp);
        updateButtonsWithState(ActionType.STOP);

        updateDurationTextViewWithMinutes();
    }

    private void updateDurationTextViewWithMinutes() {
        MinutesCalculator minutesCalculator = new MinutesCalculator();
        int minutes = minutesCalculator.getMinutes(actions);
        durationTextView.setText(String.format("%s %s %s", getResources().getString(R.string.duration), minutes, getResources().getString(R.string.minutes)));
    }

    private void resetDurationTextView() {
        durationTextView.setText(R.string.duration);
    }

    public void pauseMeasure(View view) {
        long timestamp = System.currentTimeMillis();
        addNewActionAndUpdateDb(ActionType.PAUSE, timestamp);
        updateButtonsWithState(ActionType.PAUSE);
    }

    private long addNewActionAndUpdateDb(ActionType type, long timestamp) {
        Action action = new Action(type, timestamp);
        actions.addFirst(action);
        actionAdapter.notifyDataSetChanged();

        SQLiteDatabase db = actionDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ActionReaderContract.ActionEntry.COLUMN_NAME_TYPE, action.getType().toString());
        values.put(ActionReaderContract.ActionEntry.COLUMN_NAME_TIMESTAMP, action.getTimestamp());

        return db.insert(ActionReaderContract.ActionEntry.TABLE_NAME, null, values);
    }

    @Override
    protected void onDestroy() {
        actionDbHelper.close();
        playDbHelper.close();
        super.onDestroy();
    }

    public void resumeMeasure(View view) {
        long timestamp = System.currentTimeMillis();
        addNewActionAndUpdateDb(ActionType.RESUME, timestamp);
        updateButtonsWithState(ActionType.RESUME);
    }
}
