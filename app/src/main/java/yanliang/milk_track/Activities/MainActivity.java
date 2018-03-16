package yanliang.milk_track.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import yanliang.milk_track.R;
import yanliang.milk_track.database.DBHelper;
import yanliang.milk_track.model.MilkDataSource;

public class MainActivity extends Activity {

    private DBHelper mDBHelper;
    private MilkDataSource milkDataSource;
    private ImageButton createNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNew = (ImageButton) findViewById(R.id.createNew);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_from_top);
        slide.setDuration(800);
        this.createNew.startAnimation(slide);

        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, RecordMilk.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
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
        startActivity(item.getIntent());
        return true;
    }
}
