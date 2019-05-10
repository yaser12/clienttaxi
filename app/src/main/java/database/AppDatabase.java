package database;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import database.entity.EndUser;
import database.entity.EndUserDao;
import database.entity.Office;
import database.entity.OfficeDao;
import database.entity.OrderTaxi;
import database.entity.OrderTaxiDao;
import database.entity.Taxi;
import database.entity.TaxiDao;
import database.entity.Taxi_Rating;
import database.entity.Taxi_RatingDao;

@Database(entities = {EndUser.class,Office.class,OrderTaxi.class,Taxi_Rating.class,Taxi.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase
{

    public static final String DATABASE_NAME = "AppDatabase.db";
    private static volatile AppDatabase instance;
    private static final Object LOCK = new Object();

    public abstract EndUserDao endUserDao();
    public abstract OfficeDao officeDao();
    public abstract OrderTaxiDao orderTaxiDao();
    public abstract Taxi_RatingDao taxi_RatingDao();
    public abstract TaxiDao taxiDao();
    public static AppDatabase getInstance(Context context)
    {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room . databaseBuilder(context.getApplicationContext() , AppDatabase.class, DATABASE_NAME).build();
                }
            }
        }

        return instance;
    }

}
