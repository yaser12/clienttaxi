package database.entity;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TaxiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTaxi(Taxi taxi);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Taxi> taxis);

    @Delete
    void deleteNote(Taxi taxi);

    @Query("SELECT * FROM taxi WHERE taxi_id = :taxi_id")
    Taxi getNoteById(int taxi_id);

    @Query("SELECT * FROM taxi ORDER BY taxi_id DESC")
    LiveData<List<Taxi>> getAll();

    @Query("DELETE FROM taxi")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM taxi")
    int getCount();
}
