package mysop.pia.com.Categories.CatergoryRoom;

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

    @Query("DELETE FROM MYSOP WHERE :categoryName = categoryTitle")
    void deleteCategory(String categoryName);
//
//    @Query("SELECT starStatus FROM favorite_movies WHERE movieId = :movieId")
//    int getStarStatus(String movieId);
}
