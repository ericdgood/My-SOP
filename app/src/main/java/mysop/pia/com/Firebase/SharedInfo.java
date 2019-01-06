package mysop.pia.com.Firebase;

public class SharedInfo {

    private String authorName;
    private String sharedUser;
    private String sopSharedTitle;

    public SharedInfo(){

    }

    public SharedInfo(String AuthorName, String SharedUser, String SopTitle){
        this.authorName = AuthorName;
        this.sharedUser = SharedUser;
        this.sopSharedTitle = SopTitle;
    }


    public String getAuthorName() {
        return authorName;
    }

    public String getSharedUser() {
        return sharedUser;
    }

    public String getSopSharedTitle() {
        return sopSharedTitle;
    }

}
