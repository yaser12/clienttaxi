package database.entity;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao
public interface EndUserDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEndUser(EndUser endUser);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<EndUser> endUsers);

    @Delete
    void deleteNote(EndUser endUser);

    @Query("SELECT * FROM enduser WHERE enduser_id = :enduser_id")
    EndUser getNoteById(int enduser_id);

    @Query("SELECT * FROM enduser ORDER BY enduser_id DESC")
    LiveData<List<EndUser>> getAll();

    @Query("DELETE FROM enduser")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM enduser")
    int getCount();

}
