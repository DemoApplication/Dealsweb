package adapters;

/**
 * Created by Userone on 2/1/2017.
 */

public class Views {

    //private variables
    int _id;
    String viewId ;
    int viewCount;
    // Empty constructor
    public Views(String viewId, int viewCount)
    {
        this.viewId=viewId;
        this.viewCount=viewCount;

    }
    public Views()
    {

    }


    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }
}

