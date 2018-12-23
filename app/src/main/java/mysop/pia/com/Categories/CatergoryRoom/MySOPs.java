package mysop.pia.com.Categories.CatergoryRoom;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "mysop")
public class MySOPs {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "categoryTitle")
    private String categoryTitle;

    public MySOPs(String categoryTitle) {
        this.categoryTitle = categoryTitle;
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

}
