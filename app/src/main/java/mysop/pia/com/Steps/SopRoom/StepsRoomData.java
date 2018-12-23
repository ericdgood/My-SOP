package mysop.pia.com.Steps.SopRoom;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "steps")
public class StepsRoomData {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "stepTitle")
    private String stepTitle;

    @ColumnInfo(name = "stepNumber")
    private int stepNumber;

    public StepsRoomData(String stepTitle, int stepNumber) {
        this.stepTitle = stepTitle;
        this.stepNumber = stepNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStepTitle() {
        return stepTitle;
    }

    public int getStepNumber() {
        return stepNumber;
    }
}
