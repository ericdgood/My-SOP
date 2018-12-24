package mysop.pia.com.Steps.StepsRoom;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "steps")
public class StepsRoomData {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "sopTitle")
    private String sopTitle;

    @ColumnInfo(name = "stepTitle")
    private String stepTitle;

    @ColumnInfo(name = "stepNumber")
    private int stepNumber;

    @ColumnInfo(name = "stepDescrition")
    private String stepDescription;

    @ColumnInfo(name = "imageURI")
    private String imageURI;

    public StepsRoomData(String sopTitle, String stepTitle, int stepNumber, String stepDescription, String imageURI) {
        this.sopTitle = sopTitle;
        this.stepTitle = stepTitle;
        this.stepNumber = stepNumber;
        this.stepDescription = stepDescription;
        this.imageURI = imageURI;
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

    public String getSopTitle() {
        return sopTitle;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public String getImageURI() {
        return imageURI;
    }
}