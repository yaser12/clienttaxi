package database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

@Entity(tableName = "taxi")
public class Taxi implements Parcelable
{

    private String descrip;
    private String driver;
    private int fk_office_id;
    private int is_busy;
    private float lang;
    private float lat;
    @PrimaryKey(autoGenerate = true)
    private int taxi_id;

    public Taxi(String descrip, String driver, int fk_office_id, int is_busy, float lang, float lat, int taxi_id) {
        this.descrip = descrip;
        this.driver = driver;
        this.fk_office_id = fk_office_id;
        this.is_busy = is_busy;
        this.lang = lang;
        this.lat = lat;
        this.taxi_id = taxi_id;
    }

    @Override
    public String toString() {
        return "Taxi{" +
                "descrip='" + descrip + '\'' +
                ", driver='" + driver + '\'' +
                ", fk_office_id=" + fk_office_id +
                ", is_busy=" + is_busy +
                ", lang=" + lang +
                ", lat=" + lat +
                ", taxi_id=" + taxi_id +
                '}';
    }

    public Taxi() {
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public int getFk_office_id() {
        return fk_office_id;
    }

    public void setFk_office_id(int fk_office_id) {
        this.fk_office_id = fk_office_id;
    }

    public int getIs_busy() {
        return is_busy;
    }

    public void setIs_busy(int is_busy) {
        this.is_busy = is_busy;
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

    public int getTaxi_id() {
        return taxi_id;
    }

    public void setTaxi_id(int taxi_id) {
        this.taxi_id = taxi_id;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.descrip);
        dest.writeString(this.driver);
        dest.writeInt(this.fk_office_id);
        dest.writeInt(this.is_busy);
        dest.writeFloat(this.lang);
        dest.writeFloat(this.lat);
        dest.writeInt(this.taxi_id);
    }

    protected Taxi(Parcel in)
    {
        this.descrip = in.readString();
        this.driver = in.readString();
        this.fk_office_id = in.readInt();
        this.is_busy = in.readInt();
        this.lang = in.readFloat();
        this.lat = in.readFloat();

        this.taxi_id = in.readInt();
    }

    public static final Parcelable.Creator<Taxi> CREATOR = new Parcelable.Creator<Taxi>() {
        @Override
        public Taxi createFromParcel(Parcel source) {
            return new Taxi(source);
        }

        @Override
        public Taxi[] newArray(int size) {
            return new Taxi[size];
        }
    };
}

