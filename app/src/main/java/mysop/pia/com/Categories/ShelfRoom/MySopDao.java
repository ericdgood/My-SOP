package mysop.pia.com.Categories.ShelfRoom;

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

    @Query("UPDATE MYSOP SET categoryTitle = :shelfTitle WHERE id = :id")
    void updateShelf(String shelfTitle, int id);

    @Query("UPDATE MYSOP SET sharedAuthor = 'shared' WHERE categoryTitle = :shelfTitle")
    void updateSharedShelf(String shelfTitle);
}
