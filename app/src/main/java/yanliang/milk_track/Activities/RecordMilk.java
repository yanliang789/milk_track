package yanliang.milk_track.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yanliang.milk_track.R;
import yanliang.milk_track.model.MilkDataSource;
import yanliang.milk_track.model.MilkRecord;
import yanliang.milk_track.model.SplitRecord;
import yanliang.milk_track.util.EsAnimationUtil;


public class RecordMilk extends Activity {
    private MilkDataSource dataSource;
    private RadioGroup sideGroup;
    private RadioGroup typeGroup;
    private RadioGroup contentGroup;
    private RadioGroup volumeGroup;
    private RadioButton sideBtn;
    private RadioButton typeBtn;
    private RadioButton contentBtn;
    private RadioButton volumeBtn;
    private Button startBtn;
    private Button pauseBtn;
    private Button stopBtn;
    private Button createSplitBtn;
    private TextView leftTimer;
    private TextView rightTimer;
    private MilkRecord myMilkRecord;
    private SplitRecord.SplitType splitType = SplitRecord.SplitType.SPLIT;
    private SplitRecord.SplitContent splitContent = SplitRecord.SplitContent.MILK;
    private SplitRecord.SplitVolume splitVolume = SplitRecord.SplitVolume.SMALL;
    private Side side = Side.RIGHT;
    private long startTime = 0;
    private long duration = 0;
    private String pauseStr = "Pause";

    public enum Side {LEFT, RIGHT}

    public List<String> splitRecords = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        startBtn = (Button) findViewById(R.id.start_button);
        pauseBtn = (Button) findViewById(R.id.timer_button);
        stopBtn = (Button) findViewById(R.id.stop_button);

        myMilkRecord = new MilkRecord();

        dataSource = new MilkDataSource(this);
        dataSource.open();

        final ListView listview = (ListView) findViewById(R.id.split_list);

        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, splitRecords);
        listview.setAdapter(adapter);

        addListenerOnButton(adapter);
    }


    private void addListenerOnButton(final ArrayAdapter adapter) {
        //side group
        sideGroup = (RadioGroup) findViewById(R.id.radioSideGroup);
        startBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int selectedId = sideGroup.getCheckedRadioButtonId();
                sideBtn = (RadioButton) findViewById(selectedId);
                if (sideBtn.getTag().equals("left")) {
                    side = Side.LEFT;
                } else {
                    side = Side.RIGHT;
                }

                Date date = new Date();
                Animation animation = EsAnimationUtil.createItemDisappearAnimation(400);
                animation.setInterpolator(new AccelerateInterpolator());
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        startTimer();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                startBtn.startAnimation(animation);
                if (side == Side.RIGHT) {
                    myMilkRecord.setRightStartTime(date.getTime());
                } else {
                    myMilkRecord.setLeftStartTime(date.getTime());
                }
            }
        });

        stopBtn = (Button) findViewById(R.id.stop_button);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                if (side == Side.RIGHT) {
                    myMilkRecord.setRightEndTime(date.getTime());
                } else {
                    myMilkRecord.setLeftEndTime(date.getTime());
                }
                stopBtn.setVisibility(View.GONE);
                pauseBtn.setVisibility(View.GONE);
                startBtn.setVisibility(View.VISIBLE);
                updateTimerView();
            }
        });

        //type group
        typeGroup = (RadioGroup) findViewById(R.id.radioTypeGroup);
        contentGroup = (RadioGroup) findViewById(R.id.radioContentGroup);
        volumeGroup = (RadioGroup) findViewById(R.id.radioVolumeGroup);
        createSplitBtn = (Button) findViewById(R.id.split_button);
        createSplitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long leftDiff = 0;
                long rightDiff = 0;
                if (myMilkRecord.getLeftEndTime() != 0 && myMilkRecord.getLeftStartTime() != 0) {
                    leftDiff = myMilkRecord.getLeftEndTime() - myMilkRecord.getLeftStartTime();
                }
                if (myMilkRecord.getRightEndTime() != 0 && myMilkRecord.getRightStartTime() != 0) {
                    rightDiff = myMilkRecord.getRightEndTime() - myMilkRecord.getRightStartTime();
                }
                if (leftDiff == 0 && rightDiff == 0) {
                    Toast.makeText(getApplicationContext(), "请先纪录哺乳！", Toast.LENGTH_SHORT).show();
                    return;
                }

                Date date = new Date();
                SplitRecord splitRecord = new SplitRecord(date.getTime());
                //selected split type
                int selectedType = typeGroup.getCheckedRadioButtonId();
                typeBtn = (RadioButton) findViewById(selectedType);
                if (typeBtn.getTag().equals("omit")) {
                    splitType = SplitRecord.SplitType.OMIT;
                } else {
                    splitType = SplitRecord.SplitType.SPLIT;
                }
                //selected split content
                int selectedContent = contentGroup.getCheckedRadioButtonId();
                contentBtn = (RadioButton) findViewById(selectedContent);
                if (contentBtn.getTag().equals("milk")) {
                    splitContent = SplitRecord.SplitContent.MILK;
                } else {
                    splitContent = SplitRecord.SplitContent.CHEESE;
                }
                //selected split volume
                int selectedVolume = volumeGroup.getCheckedRadioButtonId();
                volumeBtn = (RadioButton) findViewById(selectedVolume);
                if (volumeBtn.getTag().equals("rare")) {
                    splitVolume = SplitRecord.SplitVolume.RARE_SMALL;
                } else if (volumeBtn.getTag().equals("small")) {
                    splitVolume = SplitRecord.SplitVolume.SMALL;
                } else {
                    splitVolume = SplitRecord.SplitVolume.HUGE;
                }
                splitRecord.setVolume(splitVolume);
                splitRecord.setContent(splitContent);
                splitRecord.setType(splitType);
                myMilkRecord.addRecord(splitRecord);
                splitRecords.add(splitRecord.toString());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void startTimer() {
        startBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);
        duration = 0;
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().toString().startsWith("Pause")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    duration += System.currentTimeMillis() - startTime;
                    pauseStr = "Resume";
                    updatePauseText(0);
                } else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    pauseStr = "Pause";
                }
            }
        });
        stopBtn.setVisibility(View.VISIBLE);
    }

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long newAdded = System.currentTimeMillis() - startTime;
            updatePauseText(newAdded);

            timerHandler.postDelayed(this, 500);
        }
    };

    private void updatePauseText(long newAdded) {
        int seconds = (int) ((duration + newAdded) / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        pauseBtn.setText(pauseStr + " : " + String.format("%d:%02d", minutes, seconds));
    }

    private void saveToDB() {
        try {
            long leftDiff = 0;
            long rightDiff = 0;
            if (myMilkRecord.getLeftEndTime() != 0 && myMilkRecord.getLeftStartTime() != 0) {
                leftDiff = myMilkRecord.getLeftEndTime() - myMilkRecord.getLeftStartTime();
            }
            if (myMilkRecord.getRightEndTime() != 0 && myMilkRecord.getRightStartTime() != 0) {
                rightDiff = myMilkRecord.getRightEndTime() - myMilkRecord.getRightStartTime();
            }
            if (leftDiff != 0 || rightDiff != 0 || splitRecords.size() > 0) {
                dataSource.createRecord(myMilkRecord.getLeftStartTime(), myMilkRecord.getLeftEndTime(), myMilkRecord.getRightStartTime(), myMilkRecord.getRightEndTime(), myMilkRecord.getSplitRecord());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateTimerView() {
        leftTimer = (TextView) findViewById(R.id.left_timer);
        rightTimer = (TextView) findViewById(R.id.right_timer);
        DecimalFormat df = new DecimalFormat("#");
        if (myMilkRecord.getLeftEndTime() != 0 && myMilkRecord.getLeftStartTime() != 0) {
            long timeDiffInSec = (myMilkRecord.getLeftEndTime() / 1000 - myMilkRecord.getLeftStartTime() / 1000);
            if (timeDiffInSec > 0) {
                leftTimer.setText("Left Side: " + df.format(timeDiffInSec) + " sec");
            }
        }
        if (myMilkRecord.getRightEndTime() != 0 && myMilkRecord.getRightStartTime() != 0) {
            long timeDiffInSec = (myMilkRecord.getRightEndTime() / 1000 - myMilkRecord.getRightStartTime() / 1000);
            if (timeDiffInSec > 0) {
                rightTimer.setText("Right Side: " + df.format(timeDiffInSec) + " sec");
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveToDB();
        dataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_settings).setIntent(new Intent(this, AllRecordActivity.class));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        this.closeOptionsMenu();
        saveToDB();
        dataSource.close();
        startActivity(item.getIntent());
        this.finish();
        return true;
    }
}
