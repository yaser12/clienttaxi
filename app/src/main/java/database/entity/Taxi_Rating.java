package database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "taxi_rating")
public class Taxi_Rating   implements Parcelable {
    private int fk_enduser_id;
    private int fk_taxi_id;
    private int rating_desc;
    private int rating_value;
    @PrimaryKey(autoGenerate = true)
    private int taxi_rating_id;

    public Taxi_Rating(int fk_enduser_id, int fk_taxi_id, int rating_desc, int rating_value, int taxi_rating_id) {
        this.fk_enduser_id = fk_enduser_id;
        this.fk_taxi_id = fk_taxi_id;
        this.rating_desc = rating_desc;
        this.rating_value = rating_value;
        this.taxi_rating_id = taxi_rating_id;
    }

    public Taxi_Rating() {
    }

    public int getFk_enduser_id() {
        return fk_enduser_id;
    }

    public void setFk_enduser_id(int fk_enduser_id) {
        this.fk_enduser_id = fk_enduser_id;
    }

    public int getFk_taxi_id() {
        return fk_taxi_id;
    }

    public void setFk_taxi_id(int fk_taxi_id) {
        this.fk_taxi_id = fk_taxi_id;
    }

    public int getRating_desc() {
        return rating_desc;
    }

    public void setRating_desc(int rating_desc) {
        this.rating_desc = rating_desc;
    }

    public int getRating_value() {
        return rating_value;
    }

    public void setRating_value(int rating_value) {
        this.rating_value = rating_value;
    }

    public int getTaxi_rating_id() {
        return taxi_rating_id;
    }

    public void setTaxi_rating_id(int taxi_rating_id) {
        this.taxi_rating_id = taxi_rating_id;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.fk_enduser_id);
        dest.writeInt(this.fk_taxi_id);
        dest.writeInt(this.rating_desc);
        dest.writeInt(this.rating_value);
        dest.writeInt(this.taxi_rating_id);

    }

    protected Taxi_Rating(Parcel in)
    {
        this.fk_enduser_id = in.readInt();
        this.fk_taxi_id = in.readInt();
        this.rating_desc = in.readInt();
        this.rating_value = in.readInt();
        this.taxi_rating_id = in.readInt();



    }

    public static final Parcelable.Creator<Taxi_Rating> CREATOR = new Parcelable.Creator<Taxi_Rating>() {
        @Override
        public Taxi_Rating createFromParcel(Parcel source) {
            return new Taxi_Rating(source);
        }

        @Override
        public Taxi_Rating[] newArray(int size) {
            return new Taxi_Rating[size];
        }
    };
}
