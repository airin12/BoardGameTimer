package mb.boardgametimer;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFS_NAME = "BGTimer";
    private static final String START_NAME = "start";

    private Button startButton;
    private Button endButton;

    private TextView startTextView;
    private TextView endTextView;
    private TextView durationTextView;

    private long start;
    private long end;
    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.start_button_id);
        endButton = (Button) findViewById(R.id.end_button_id);

        startTextView = (TextView) findViewById(R.id.start_text_view_id);
        endTextView = (TextView) findViewById(R.id.end_text_view_id);
        durationTextView = (TextView) findViewById(R.id.duration_text_view_id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME,MODE_PRIVATE);
        sharedPreferences.edit().putLong(START_NAME,start).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME,MODE_PRIVATE);
        start = sharedPreferences.getLong(START_NAME,0);

        updateTextViews();
    }

    public void startMeasure(View view){
        start = System.currentTimeMillis();
        startButton.setEnabled(false);

        duration = 0;
        end = 0;
        updateTextViews();
    }

    private void updateStartTextView() {
        String formattedDate = "";
        if(start != 0){
            formattedDate = getFormattedDate(start);
        }
        startTextView.setText("Start: " + formattedDate);
    }

    private void updateEndTextView() {
        String formattedDate = "";
        if(end != 0){
            formattedDate = getFormattedDate(end);
        }
        endTextView.setText("End: "+ formattedDate);
    }

    private void updateDurationTextView(){
        String durationText = "";
        if(duration != 0){
            durationText = duration + " m";
        }

        durationTextView.setText("Duration: "+durationText);
    }

    private String getFormattedDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss", Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    public void endMeasure(View view){
        end = System.currentTimeMillis();
        long duration = end - start;

        MinutesCalculator calculator = new MinutesCalculator(duration);
        this.duration = calculator.getMinutes();

        updateTextViews();
        startButton.setEnabled(true);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME,MODE_PRIVATE);
        sharedPreferences.edit().remove(START_NAME).apply();
    }

    private void updateTextViews(){
        updateStartTextView();
        updateEndTextView();
        updateDurationTextView();
    }
}
