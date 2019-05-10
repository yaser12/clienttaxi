package database.entity;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface OfficeDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertOffice(Office office);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAll(List<Office> offices);

        @Delete
        void deleteNote(Office office);

        @Query("SELECT * FROM office WHERE office_id = :office_id")
        Office getNoteById(int office_id);

        @Query("SELECT * FROM office ORDER BY office_id DESC")
        LiveData<List<Office>> getAll();

        @Query("DELETE FROM office")
        int deleteAll();

        @Query("SELECT COUNT(*) FROM Office")
        int getCount();
}
