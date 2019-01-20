package mysop.pia.com.Steps.StepsRoom;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "steps")
public class StepsRoomData {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "category")
    private String category;

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

    @ColumnInfo(name = "savedBook")
    private int savedBook;

    @ColumnInfo(name = "bookColor")
    private String bookColor;

    @ColumnInfo(name = "sharedAuthor")
    private String sharedAuthor;

    public StepsRoomData(String category, String sopTitle, String stepTitle, int stepNumber,
                         String stepDescription, String imageURI, int savedBook, String bookColor, String sharedAuthor) {
        this.category = category;
        this.sopTitle = sopTitle;
        this.stepTitle = stepTitle;
        this.stepNumber = stepNumber;
        this.stepDescription = stepDescription;
        this.imageURI = imageURI;
        this.savedBook = savedBook;
        this.bookColor = bookColor;
        this.sharedAuthor = sharedAuthor;
    }

    @Ignore
    public StepsRoomData(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public String getStepTitle() {
        return stepTitle;
    }

    public String getSopTitle() {
        return sopTitle;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public String getImageURI() {
        return imageURI;
    }

    public int getSavedBook() {
        return savedBook;
    }

    public String getBookColor() {
        return bookColor;
    }

    public String getSharedAuthor() {
        return sharedAuthor;
    }
}
