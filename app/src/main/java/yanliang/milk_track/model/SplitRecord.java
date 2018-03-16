package yanliang.milk_track.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ziweizeng on 1/2/15.
 */
public class SplitRecord extends JSONObject{
    public static String SPLIT_TYPE = "type";
    public static String SPLIT_CONTENT = "content";
    public static String SPLIT_VOLUME = "volume";
    public static String SPLIT_TIME = "time";
    public enum SplitType{SPLIT, OMIT};
    public enum SplitContent{MILK, CHEESE};
    public enum SplitVolume{RARE_SMALL, SMALL, HUGE};
    private SplitType mType;
    private SplitContent mContent;
    private SplitVolume mVolume;
    private long time;

    public SplitRecord(long time) {
        this.time = time;
    }

    public SplitRecord() {

    }

    public void setType(SplitType type) {
        this.mType = type;
    }

    public SplitType getType() {
        return mType;
    }

    public void setContent(SplitContent content) {
        this.mContent = content;
    }

    public SplitContent getContent() {
        return mContent;
    }

    public void setVolume (SplitVolume volume) {
        this.mVolume = volume;
    }

    public SplitVolume getVolume() {
        return mVolume;
    }

    public void setTime (long time) {
        this.time = time;
    }

    public long getTime () {
        return time;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(SPLIT_TYPE, mType.ordinal());
        object.put(SPLIT_CONTENT, mContent.ordinal());
        object.put(SPLIT_VOLUME, mVolume.ordinal());
        object.put(SPLIT_TIME, time);
        return object;
    }

    public void fromJson(JSONObject jObject) throws JSONException {
        this.setTime(jObject.getLong(SPLIT_TIME));
        this.setType(SplitType.values()[jObject.getInt(SPLIT_TYPE)]);
        this.setContent(SplitContent.values()[jObject.getInt(SPLIT_CONTENT)]);
        this.setVolume(SplitVolume.values()[jObject.getInt(SPLIT_VOLUME)]);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        sb.append(format.format(calendar.getTime()));
        switch(mType){
            case OMIT:
                sb.append(" Vomit");
                break;
            case SPLIT:
                sb.append(" Spit up");
                break;
        }

        switch(mVolume){
            case RARE_SMALL:
                sb.append(" slight");
                break;
            case SMALL:
                sb.append(" little");
                break;
            case HUGE:
                sb.append(" plenty of");
                break;
        }

        switch(mContent) {
            case MILK:
                sb.append(" milk");
                break;
            case CHEESE:
                sb.append(" cottage cheese like milk");
                break;
        }
        return sb.toString();
    }

}
