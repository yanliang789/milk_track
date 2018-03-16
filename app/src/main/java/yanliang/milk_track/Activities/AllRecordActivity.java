package yanliang.milk_track.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import yanliang.milk_track.R;
import yanliang.milk_track.model.MilkDataSource;
import yanliang.milk_track.model.MilkRecord;


public class AllRecordActivity extends Activity {
    private yanliang.milk_track.model.MilkDataSource dataSource;
    private List<String>allRecords = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_record);

        dataSource = new MilkDataSource(this);
        dataSource.open();

        try {
            List<MilkRecord> values = dataSource.getAllRecords();
            if(values.size() > 0) {
                for (MilkRecord mr : values) {
                    allRecords.add(mr.toString());
                }
            }

            final ListView listview = (ListView) findViewById(R.id.all_records);

            final ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, allRecords);
            listview.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }
}
