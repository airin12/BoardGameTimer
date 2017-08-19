package mb.boardgametimer;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import mb.boardgametimer.model.Action;
import mb.boardgametimer.model.ActionType;
import mb.boardgametimer.sqlite.ActionReaderContract;
import mb.boardgametimer.sqlite.ActionReaderDbHelper;

public class MainActivity extends AppCompatActivity {
    private Button startButton;
    private Button endButton;
    private Button pauseButton;

    private TextView durationTextView;

    private ActionReaderDbHelper dbHelper;
    private LinkedList<Action> actions;
    private ArrayAdapter<Action> actionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.start_button_id);
        endButton = (Button) findViewById(R.id.end_button_id);
        pauseButton = (Button) findViewById(R.id.pause_button_id);

        durationTextView = (TextView) findViewById(R.id.duration_text_view_id);

        ListView actionListView = (ListView) findViewById(R.id.action_list_view_id);
        actions = new LinkedList<>();
        actionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, actions);
        actionListView.setAdapter(actionAdapter);

        dbHelper = new ActionReaderDbHelper(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTextViews();
    }

    public void startMeasure(View view){
        actions.clear();
        dbHelper.getWritableDatabase().execSQL("DELETE FROM "+ActionReaderContract.ActionEntry.TABLE_NAME);
        long timestamp = System.currentTimeMillis();
        addNewActionAndUpdateDb(ActionType.START, timestamp);
    }

    private void updateStartTextView() {
        String formattedDate = "";
    }

    private void updateEndTextView() {
        String formattedDate = "";
    }

    private void updateDurationTextView(){
        String durationText = "";
//        if(duration != 0){
//            durationText = duration + " m";
//        }

        durationTextView.setText("Duration: "+durationText);
    }

    public void endMeasure(View view){
        long timestamp = System.currentTimeMillis();
        addNewActionAndUpdateDb(ActionType.STOP, timestamp);
    }

    private void updateTextViews(){
        updateStartTextView();
        updateEndTextView();
        updateDurationTextView();
    }

    public void pauseMeasure(View view) {
        long timestamp = System.currentTimeMillis();
        addNewActionAndUpdateDb(ActionType.PAUSE, timestamp);
    }

    private long addNewActionAndUpdateDb(ActionType type, long timestamp){
        Action action = new Action(type, timestamp);
        actions.addFirst(action);
        actionAdapter.notifyDataSetChanged();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ActionReaderContract.ActionEntry.COLUMN_NAME_TYPE, action.getType().toString());
        values.put(ActionReaderContract.ActionEntry.COLUMN_NAME_TIMESTAMP, action.getTimestamp());

        return db.insert(ActionReaderContract.ActionEntry.TABLE_NAME, null, values);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    public void resumeMeasure(View view) {
        long timestamp = System.currentTimeMillis();
        addNewActionAndUpdateDb(ActionType.RESUME, timestamp);
    }
}
