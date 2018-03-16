package yanliang.milk_track.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MilkRecord {
    private long id;
    private long leftStartTime = 0;
    private long mLeftTime = 0;
    private long rightStartTime = 0;
    private long mRightTime = 0;
    private List<SplitRecord> splitRecords;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLeftStartTime() {
        return leftStartTime;
    }

    public void setLeftStartTime(long leftStartTime) {
        this.leftStartTime = leftStartTime;
    }

    public long getLeftEndTime() {
        return mLeftTime;
    }

    public void setLeftEndTime(long mLeftTime) {
        this.mLeftTime = mLeftTime;
    }

    public long getRightStartTime() {
        return rightStartTime;
    }

    public void setRightStartTime(long rightStartTime) {
        this.rightStartTime = rightStartTime;
    }

    public long getRightEndTime() {
        return mRightTime;
    }

    public void setRightEndTime(long rightTime) {
        this.mRightTime = rightTime;
    }

    public String getSplitRecord() throws JSONException {
        JSONArray array = new JSONArray();
        if(splitRecords != null && splitRecords.size() > 0) {
            for(SplitRecord r : splitRecords) {
                array.put(r.toJson());
            }
        }
        return array.toString();
    }

    public void setSplitRecords(String splitRecords) throws JSONException {
        if(splitRecords == null || splitRecords.length() == 0)
            return;
        JSONArray jsonArray = new JSONArray(splitRecords);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            SplitRecord sr = new SplitRecord();
            sr.fromJson(jsonObject);
            addRecord(sr);
        }

    }

    public void addRecord(SplitRecord record) {
        if(splitRecords == null) {
            splitRecords = new ArrayList<SplitRecord>();
        }
        this.splitRecords.add(record);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        if(getLeftEndTime() != 0) {
            long timeDiffInMin = (getLeftEndTime()/1000) /60;
            if(timeDiffInMin >= 0) {
                sb.append(format.format(getLeftStartTime()));
                sb.append(" Left Side: " + df.format(timeDiffInMin) + " min");
            }
        }

        if(getRightEndTime() != 0) {
            long timeDiffInMin = (getRightEndTime()/1000) /60;
            if(timeDiffInMin > 0){
                sb.append("\n");
                sb.append(format.format(getRightStartTime()));
                sb.append(" Right side: " + df.format(timeDiffInMin) + " min");
            }
        }

        if(splitRecords != null && splitRecords.size() > 0) {
            for(SplitRecord sr: splitRecords) {
                if(sb.toString().length() > 0) {
                    sb.append("\n");
                }
                sb.append(sr.toString());
            }
        }
        return sb.toString();
    }
}