package mysop.pia.com.RoomData.SopRoom;

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

    @ColumnInfo(name = "sopNumberOfSteps")
    private String sopNumberOfSteps;

    public SOPRoomData(String categoryName, String sopTitle, String sopNumberOfSteps) {
        this.categoryName = categoryName;
        this.sopTitle = sopTitle;
        this.sopNumberOfSteps = sopNumberOfSteps;
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

    public void setSopTitle(String sopTitle) {
        this.sopTitle = sopTitle;
    }

    public String getSopNumberOfSteps() {
        return sopNumberOfSteps;
    }

    public void setSopNumberOfSteps(String sopNumberOfSteps) {
        this.sopNumberOfSteps = sopNumberOfSteps;
    }

}
