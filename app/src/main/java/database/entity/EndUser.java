package database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "enduser")
public class EndUser implements Parcelable
{
    @PrimaryKey(autoGenerate = true)
    private int enduser_id;
    private String enduser_name;
    private float lang;
    private float lat;
    private String pass;
    private String phone;
    private String user_name;

    public EndUser(int enduser_id, String enduser_name, float lang, float lat, String pass, String phone, String user_name) {
        this.enduser_id = enduser_id;
        this.enduser_name = enduser_name;
        this.lang = lang;
        this.lat = lat;
        this.pass = pass;
        this.phone = phone;
        this.user_name = user_name;
    }

    @Ignore
    public EndUser() {
    }

    public int getEnduser_id() {
        return enduser_id;
    }

    public void setEnduser_id(int enduser_id) {
        this.enduser_id = enduser_id;
    }

    public String getEnduser_name() {
        return enduser_name;
    }

    public void setEnduser_name(String enduser_name) {
        this.enduser_name = enduser_name;
    }

    public float getLang() {
        return lang;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.enduser_id);
        dest.writeString(this.enduser_name);
        dest.writeFloat(this.lang);
        dest.writeFloat(this.lat);
        dest.writeString(this.pass);
        dest.writeString(this.phone);
        dest.writeString(this.user_name);
    }



    protected EndUser(Parcel in)
    {
        this.enduser_id = in.readInt();
        this.enduser_name = in.readString();
        this.lang = in.readFloat();
        this.lat = in.readInt();
        this.pass = in.readString();
        this.phone = in.readString();
        this.user_name = in.readString();
    }

    public static final Parcelable.Creator<EndUser> CREATOR = new Parcelable.Creator<EndUser>() {
        @Override
        public EndUser createFromParcel(Parcel source) {
            return new EndUser(source);
        }

        @Override
        public EndUser[] newArray(int size) {
            return new EndUser[size];
        }
    };
}
