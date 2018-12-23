package mysop.pia.com.RoomData.CatergoryRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MySopDao {

    @Query("SELECT * FROM MYSOP")
    List<MySOPs> getAllSOPs();

    @Insert
    void insertAll(MySOPs... sops);
//
//    @Query("DELETE FROM favorite_movies WHERE movieId = :movieId")
//    void deleteByUserId(String movieId);
//
//    @Query("SELECT starStatus FROM favorite_movies WHERE movieId = :movieId")
//    int getStarStatus(String movieId);
}
