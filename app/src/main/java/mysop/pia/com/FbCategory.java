package mysop.pia.com;

public class FbCategory {

    private String categoryName;

    public FbCategory() {
    }

    public FbCategory(String categoryName){
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
