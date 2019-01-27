package mysop.pia.com.Pages.PagesRoom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface Stepsinterface {

//    GET ALL STEPS FOR SOP IN ORDER OF STEP NUMBER
    @Query("SELECT * FROM steps WHERE :sopTitle = sopTitle ORDER BY stepNumber ASC")
    List<StepsRoomData> getAllSteps(String sopTitle);

//    GET ALL SOPS FOR CATEGORY
    @Query("SELECT * FROM steps WHERE :category = category AND :pageNum = stepNumber")
    List<StepsRoomData> getAllSOPs(String category, int pageNum);

    //    GET ALL SOPS FOR CATEGORY
    @Query("SELECT * FROM steps WHERE :category = category")
    List<StepsRoomData> getAllBooks(String category);

    //    GET ALL SAVED BOOK
    @Query("SELECT * FROM steps WHERE :save = savedBook")
    List<StepsRoomData> getAllSavedBooks(int save);

    //  INSERT NEW SOP
    @Insert
    void insertSteps(StepsRoomData... steps);

    @Update (onConflict = REPLACE)
    void updateStep(StepsRoomData... steps);

    //    UPDATE SOP TITLE
    @Query("UPDATE steps SET sopTitle = :newTitle WHERE :oldTitle = sopTitle")
    void updateSop(String newTitle ,String oldTitle);

    //    UPDATE SOP TITLE
    @Query("UPDATE steps SET bookColor = :bookColor WHERE id = :id")
    void updateBookColor(String bookColor, int id);

    //    DELETE SOP
    @Query("DELETE FROM steps WHERE :sopTitle = sopTitle")
    void DeleteSOP(String sopTitle);

    //    DELETE PAGE FROM BOOK
    @Query("DELETE FROM steps WHERE :pageTitle = stepTitle")
    void DeletePAGE(String pageTitle);

    //    UPDATE PAGES WHEN SHELF IS EDITED
    @Query("UPDATE steps SET stepNumber = (stepNumber - 1) WHERE stepNumber > :pageNumber AND :bookTitle = sopTitle ")
    void updatePageNumber(int pageNumber, String bookTitle);

    //    DELETE ALL SHELF BOOKS
    @Query("DELETE FROM steps WHERE :shelf = category")
    void DeleteShelfBooks(String shelf);

    //    UPDATE PAGES WHEN SHELF IS EDITED
    @Query("UPDATE steps SET category = :newShelf WHERE :oldShelf = category")
    void updatePagesShelf(String newShelf ,String oldShelf);

    //    UPDATE PAGES WHEN SHELF IS EDITED
    @Query("UPDATE steps SET savedBook = :save WHERE id = :id")
    void updateBookSaved(int save, int id);

    //    UPDATE SHELF FOR SHARING
    @Query("UPDATE steps SET sharedAuthor = :sharedAuthor, sharedStatus = 1 WHERE category = :shelf")
    void updateShelfSharing(String sharedAuthor, String shelf);

    //    UPDATE BOOK FOR SHARING
    @Query("UPDATE steps SET sharedAuthor = :sharedAuthor, sharedStatus = 4 WHERE sopTitle = :bookTitle")
    void updateBookSharing(String sharedAuthor, String bookTitle);
}

//SHARED STATUS KEY
//  0 = NOT SHARED
//  1 = SENT SHELF
//  2 = SHELF REQUEST ACCEPTED
//  3 = REQUEST DENIED
//  4 = SENT BOOK
//  5 = BOOK REQUEST ACCEPTED
