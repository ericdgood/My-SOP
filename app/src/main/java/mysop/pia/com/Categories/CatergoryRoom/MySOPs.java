package mysop.pia.com.Categories.CatergoryRoom;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "mysop")
public class MySOPs {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "categoryTitle")
    private String categoryTitle;

    @ColumnInfo(name = "sharedAuthor")
    private String sharedAuthor;

    @Ignore
    public MySOPs(){}

    public MySOPs(String categoryTitle, String sharedAuthor) {
        this.categoryTitle = categoryTitle;
        this.sharedAuthor = sharedAuthor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public String getSharedAuthor() {
        return sharedAuthor;
    }
}
