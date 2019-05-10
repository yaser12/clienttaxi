package database.entity;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao
public interface OrderTaxiDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrderTaxi(OrderTaxi orderTaxi);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<OrderTaxi> orderTaxis);

    @Delete
    void deleteNote(OrderTaxi orderTaxi);

    @Query("SELECT * FROM ordertaxi WHERE ordertaxi_id = :ordertaxi_id")
    OrderTaxi getNoteById(int ordertaxi_id);

    @Query("SELECT * FROM ordertaxi ORDER BY ordertaxi_id DESC")
    LiveData<List<OrderTaxi>> getAll();

    @Query("DELETE FROM ordertaxi")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM ordertaxi")
    int getCount();

}
