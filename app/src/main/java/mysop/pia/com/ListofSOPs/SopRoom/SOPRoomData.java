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

    @ColumnInfo(name = "stepTitle")
    private String stepTitle;

    @ColumnInfo(name = "stepNumber")
    private int stepNumber;

    public SOPRoomData(String categoryName, String sopTitle, String stepTitle, int stepNumber) {
        this.categoryName = categoryName;
        this.sopTitle = sopTitle;
        this.stepTitle = stepTitle;
        this.stepNumber = stepNumber;
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

    public String getStepTitle() {
        return stepTitle;
    }

    public int getStepNumber() {
        return stepNumber;
    }
}
