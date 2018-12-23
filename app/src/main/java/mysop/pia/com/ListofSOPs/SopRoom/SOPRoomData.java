package mysop.pia.com.ListofSOPs.SopRoom;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "sopinfo")
public class SOPRoomData {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "categoryName")
    private String categoryName;

    @ColumnInfo(name = "sopTitle")
    private String sopTitle;

    @ColumnInfo(name = "numberOfSteps")
    private int numberOfSteps;

    public SOPRoomData(String categoryName, String sopTitle, int numberOfSteps) {
        this.categoryName = categoryName;
        this.sopTitle = sopTitle;
        this.numberOfSteps = numberOfSteps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getSopTitle() {
        return sopTitle;
    }

    public int getNumberOfSteps() {
        return numberOfSteps;
    }

}
