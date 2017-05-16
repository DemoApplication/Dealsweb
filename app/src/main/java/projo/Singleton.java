package projo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 10/12/2016.
 */

public class Singleton
{
    private static Singleton _singleton = null;
    private boolean online;
    public static String dealsType;
    public static String selectedSubCategory;
    public String singleActivityData;
    public String descriptionData;
    public String loginUserId;
    public int screenSize;
    public String LoginUserDisplayName="";
    public String LoginUserEmailId;
    public String LoginStatus;
    public String LoginImage;
    public static String insertLikeStaus;
    public static String active_login;
public static boolean ReloadPage;
    public String profileData;
    public static boolean editProfileSaveStatus=false,profileActivityBackPressedStatus=false;

   public static List<String> categoriesData= new ArrayList<String>();
    public static List<String> subCategoriesData= new ArrayList<String>();
    public List<String> hottestDeals= new ArrayList<String>();
    public List<String> latestDeals= new ArrayList<String>();
    public ArrayList<String> user_likes_data = new ArrayList<String>();
    public static List<String> userProfileData= new ArrayList<String>();

    boolean RefreshPage=false;
    public static Singleton getInstance()
    {
        if (_singleton == null) {
            _singleton = new Singleton();
        }
        return _singleton;
    }

    public static boolean isReloadPage() {
        return ReloadPage;
    }

    public static void setReloadPage(boolean reloadPage) {
        ReloadPage = reloadPage;
    }

    public String getProfileData() {
        return profileData;
    }

    public void setProfileData(String profileData) {
        this.profileData = profileData;
    }

    public String getLoginImage() {
        return LoginImage;
    }

    public void setLoginImage(String loginImage) {
        LoginImage = loginImage;
    }

    public String getLoginStatus() {
        return LoginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        LoginStatus = loginStatus;
    }

    public String getLoginUserDisplayName() {
        return LoginUserDisplayName;
    }

    public void setLoginUserDisplayName(String loginUserDisplayName) {
        LoginUserDisplayName = loginUserDisplayName;
    }

    public String getLoginUserEmailId() {
        return LoginUserEmailId;
    }

    public void setLoginUserEmailId(String loginUserEmailId) {
        LoginUserEmailId = loginUserEmailId;
    }

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getDescriptionData() {
        return descriptionData;
    }

    public void setDescriptionData(String descriptionData) {
        this.descriptionData = descriptionData;
    }

    public static String getSelectedCategoryId() {
        return selectedSubCategory;
    }

    public static void setSelectedCategoryId(String selectedSubCategory) {
        Singleton.selectedSubCategory = selectedSubCategory;
    }

    public boolean isOnline() {
        return online;
    }

    public static String getDealsType() {
        return dealsType;
    }

    public static void setDealsType(String dealsType) {
        Singleton.dealsType = dealsType;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getSingleActivityData() {
        return singleActivityData;
    }

    public void setSingleActivityData(String singleActivityData) {
        this.singleActivityData = singleActivityData;
    }

    public boolean isRefreshPage() {
        return RefreshPage;
    }

    public void setRefreshPage(boolean refreshPage) {
        RefreshPage = refreshPage;
    }





    public void setLoginActiveStatus(String loginActiveStatus) {
        active_login = loginActiveStatus;
    }
    public String getLoginActiveStatus() {
        return active_login;
    }

    public int getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(int screenSize) {
        this.screenSize = screenSize;
    }

    public  void setInsertLikeStatus(String insertLikeStaus) {
        Singleton.insertLikeStaus = insertLikeStaus;
    }

    public String getInsertLikeStatus() {
        return insertLikeStaus;
    }




}
