package com.neo.xutils3demo.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Neo on 2018/11/21.
 */

public class EventBean implements Parcelable{

    public static final int EVENT_REQUEST_SHORT = 0;
    public static final int EVENT_REQUEST_LONG = 1;

    private String title;
    private List<Event> eventList;
    private String extend;

    public EventBean(){
        title = "";
        eventList = null;
        extend = "";
    }
    protected EventBean(Parcel in) {
        title = in.readString();
        eventList = in.createTypedArrayList(Event.CREATOR);
        extend = in.readString();
    }

    public static final Creator<EventBean> CREATOR = new Creator<EventBean>() {
        @Override
        public EventBean createFromParcel(Parcel in) {
            return new EventBean(in);
        }

        @Override
        public EventBean[] newArray(int size) {
            return new EventBean[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeTypedList(eventList);
        dest.writeString(extend);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title1) {
        this.title = title1;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList1) {
        this.eventList = eventList1;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend1) {
        this.extend = extend1;
    }

    public static class Event implements Parcelable {

        private String title;
        private boolean multselectable;
        private String event;
        private int event_type;
        private boolean needTTS;
        private String extend;


        public Event() {
            title = "";
            multselectable = false;
            event = "";
            event_type = EVENT_REQUEST_SHORT;
            needTTS = true;
            extend = "";
        }

        protected Event(Parcel in) {
            title = in.readString();
            multselectable = in.readByte() != 0;
            event = in.readString();
            event_type = in.readInt();
            needTTS = in.readByte() != 0;
            extend = in.readString();
        }

        public static final Creator<Event> CREATOR = new Creator<Event>() {
            @Override
            public Event createFromParcel(Parcel in) {
                return new Event(in);
            }

            @Override
            public Event[] newArray(int size) {
                return new Event[size];
            }
        };

        public String getTitle() {
            return title;
        }

        public void setTitle(String title1) {
            this.title = title1;
        }

        public boolean getMultselectable() {
            return multselectable;
        }

        public void setMultselectable(boolean selectable) {
            this.multselectable = selectable;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public int getEventType() {
            return event_type;
        }

        public void setEventType(int type) {
            this.event_type = type;
        }

        public boolean isNeedTTS() {
            return needTTS;
        }

        public void setNeedTTS(boolean needTTS1) {
            this.needTTS = needTTS1;
        }

        public String getExtend() {
            return extend;
        }

        public void setExtend(String extend1) {
            this.extend = extend1;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeByte((byte) (multselectable ? 1 : 0));
            dest.writeString(event);
            dest.writeInt(event_type);
            dest.writeByte((byte) (needTTS ? 1 : 0));
            dest.writeString(extend);
        }

        @Override
        public String toString() {
            return "\n"+"{" +
                    ", \"title\":" + "\"" + title +"\""+"\n" +
                    "\"multselectable\":" + multselectable  +"\n" +
                    ", \"event\":" + "\"" + event +"\""+"\n" +
                    ", \"event_type\":" + event_type +"\n" +
                    ", \"needTTS\":" + needTTS +"\n" +
                    ", \"extend\":" + "\"" + extend +"\""+
                    "}"+ "\n";
        }
    }

    @Override
    public String toString() {
        return "{" +
                "\"title\":" + "\"" + title + "\"" +"\n" +
                ", \"eventList\":" +  eventList +"\n" +
                ", \"extend\":" + "\"" + extend +"\"" +
                "}";
    }
}
