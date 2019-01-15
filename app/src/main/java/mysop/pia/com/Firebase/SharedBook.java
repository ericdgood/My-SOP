package mysop.pia.com.Firebase;

public class SharedBook {

    private String bookTitle;
    private int pageNum;
    private String pageImg;
    private String pageDescription;

    SharedBook(String bookTitle, int pageNum, String pageImg, String pageDescription){
        this.bookTitle = bookTitle;
        this.pageNum = pageNum;
        this.pageImg = pageImg;
        this.pageDescription = pageDescription;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public int getPageNum() {
        return pageNum;
    }

    public String getPageImg() {
        return pageImg;
    }

    public String getPageDescription() {
        return pageDescription;
    }

}
