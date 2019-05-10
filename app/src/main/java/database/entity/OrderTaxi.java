package database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

@Entity(tableName = "ordertaxi")

public class OrderTaxi implements Parcelable
{
    private int fk_enduser_id;
    private int fk_taxi_id;
    @PrimaryKey(autoGenerate = true)
    private int ordertaxi_id;
    private Date ordertaxitime;

    public OrderTaxi()
    {

    }

    public OrderTaxi(int fk_enduser_id, int fk_taxi_id, int ordertaxi_id, Date ordertaxitime) {
        this.fk_enduser_id = fk_enduser_id;
        this.fk_taxi_id = fk_taxi_id;
        this.ordertaxi_id = ordertaxi_id;
        this.ordertaxitime = ordertaxitime;
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

    public int getOrdertaxi_id() {
        return ordertaxi_id;
    }

    public void setOrdertaxi_id(int ordertaxi_id) {
        this.ordertaxi_id = ordertaxi_id;
    }

    public Date getOrdertaxitime() {
        return ordertaxitime;
    }

    public void setOrdertaxitime(Date ordertaxitime) {
        this.ordertaxitime = ordertaxitime;
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
        dest.writeInt(this.ordertaxi_id);
        dest.writeLong(this.ordertaxitime != null ? this.ordertaxitime.getTime() : -1 );
    }

    protected OrderTaxi(Parcel in)
    {
        this.fk_enduser_id = in.readInt();
        this.fk_taxi_id = in.readInt();
        this.ordertaxi_id = in.readInt();
        this.ordertaxitime = new Date(in.readLong());

    }

    public static final Parcelable.Creator<OrderTaxi> CREATOR = new Parcelable.Creator<OrderTaxi>() {
        @Override
        public OrderTaxi createFromParcel(Parcel source) {
            return new OrderTaxi(source);
        }

        @Override
        public OrderTaxi[] newArray(int size) {
            return new OrderTaxi[size];
        }
    };
}
