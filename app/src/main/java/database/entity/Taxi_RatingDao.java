package database.entity;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface Taxi_RatingDao
{
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTaxi_Rating(Taxi_Rating taxi_rating);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Taxi_Rating> notes);

    @Delete
    void deleteNote(Taxi_Rating taxi_rating);

    @Query("SELECT * FROM taxi_rating WHERE taxi_rating_id = :taxi_rating_id")
    Taxi_Rating getNoteById(int taxi_rating_id);

    @Query("SELECT * FROM taxi_rating ORDER BY taxi_rating_id DESC")
    LiveData<List<Taxi_Rating>> getAll();

    @Query("DELETE FROM taxi_rating")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM taxi_rating")
    int getCount();
}
