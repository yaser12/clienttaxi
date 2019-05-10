package database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "office")
public class Office implements Parcelable
{

    private float lang;
    private float lat;
    @PrimaryKey(autoGenerate = true)
    private int office_id;
    private String office_name;
    private String access_token;
    public float getLang() {
        return lang;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setLang(float lang) {
        this.lang = lang;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public int getOffice_id() {
        return office_id;
    }

    public void setOffice_id(int office_id) {
        this.office_id = office_id;
    }

    public String getOffice_name() {
        return office_name;
    }

    public void setOffice_name(String office_name) {
        this.office_name = office_name;
    }
    @Ignore
    public Office() {
    }

    public Office(float lang, float lat, int office_id, String office_name,String access_token) {
        this.lang = lang;
        this.lat = lat;
        this.office_id = office_id;
        this.office_name = office_name;
        this.access_token=access_token;
    }

    @Override
    public String toString() {
        return "Office{" +
                "lang=" + lang +
                ", lat=" + lat +
                ", office_id=" + office_id +
                ", office_name='" + office_name + '\'' +
                ", access_token='" + access_token + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.lang);
        dest.writeFloat(this.lat);
        dest.writeInt(this.office_id);
        dest.writeString(this.office_name);
        dest.writeString(this.access_token);


    }

    protected Office(Parcel in)
    {
        this.lang = in.readFloat();
        this.lat = in.readFloat();
        this.office_id = in.readInt();
        this.office_name = in.readString();
        this.access_token=in.readString();

    }

    public static final Parcelable.Creator<Office> CREATOR = new Parcelable.Creator<Office>() {
        @Override
        public Office createFromParcel(Parcel source) {
            return new Office(source);
        }

        @Override
        public Office[] newArray(int size) {
            return new Office[size];
        }
    };
}
