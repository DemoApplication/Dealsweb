package projo;

/**
 * Created by user1 on 19-Dec-16.
 */

import java.io.Serializable;


/**
 * Created by Userone on 11/11/2016.
 */

public class SubCategories implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String emailId;
    private String imgSting;

    public SubCategories() {

    }
    public SubCategories(String category_id, String category_name, String subcategory_name) {
        this.name = name;
        this.emailId = emailId;
        this.imgSting=imgSting;
    }

    public String getName() {
        return name;
    }

    public void setName(String category_name,String subcategory_name) {
        this.name = name;
    }

    public String getSubcategoryName(String name) {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getImgSting() {
        return imgSting;
    }

    public void setImgSting(String imgSting) {
        this.imgSting = imgSting;
    }
}
