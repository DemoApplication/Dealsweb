package com.ngagroupinc.movers;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import projo.GalleryUtil;
import projo.ServerUtilites;
import projo.SessionManager;
import projo.Singleton;

public class ProfileActivity extends Activity {



    public static ImageView profile;




    TextView profileNeedToVerifyTextView;
    ProgressDialog progress;


    TextView displayNameTextView, emailTextView, locationTextView, hobbiesTextView, aboutMeTextView, editProfileTextView, profileNameTextView;
    ImageView backArrowImageView, profilePic;
    Singleton singleton;
    ServerUtilites serverUtilites;
    Button editProfileLinearLayout;
    ArrayList<String> profileArrayList = new ArrayList<String>();
    private final int GALLERY_ACTIVITY_CODE = 200;
    private final int RESULT_CROP = 400;

    TextView changePasswordTextView;
    String TAG = "ProfileActivity";

    public String profileTotalData;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        singleton = Singleton.getInstance();
        //initialize all view objects
        displayNameTextView = (TextView) findViewById(R.id.display_name_text);
        emailTextView = (TextView) findViewById(R.id.email_text);
        locationTextView = (TextView) findViewById(R.id.location_text);
        hobbiesTextView = (TextView) findViewById(R.id.hobbies_text);
        hobbiesTextView = (TextView) findViewById(R.id.hobbies_text);
        profileNameTextView = (TextView) findViewById(R.id.profile_name);
        profileNeedToVerifyTextView = (TextView) findViewById(R.id.profile_need_to_verify);
        editProfileLinearLayout = (Button) findViewById(R.id.edit_profile);
        changePasswordTextView = (TextView) findViewById(R.id.changepassword);
        aboutMeTextView = (TextView) findViewById(R.id.aboutme);
        session = new SessionManager(this);
        profilePic = (ImageView) findViewById(R.id.profilepic1);

        serverUtilites = new ServerUtilites(ProfileActivity.this);

        progress = new ProgressDialog(ProfileActivity.this);

        backArrowImageView = (ImageView) findViewById(R.id.backarrow_profile1);


        if (singleton.isOnline()) {
            Picasso.with(ProfileActivity.this).load(singleton.getLoginImage()).into(profilePic);
            String profileString = serverUtilites.serverUrl + "" + serverUtilites.Profile + "" + singleton.getLoginUserId();


            Map<String, String> apiHeader = new HashMap<String, String>();
            apiHeader.put("ApiKey ", serverUtilites.apiHeader);


            getProfileData(apiHeader, profileString);



        } else {

        }
        editProfileLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);


            }
        });
        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();


            }
        });
        SpannableString content = new SpannableString(changePasswordTextView.getText());
        content.setSpan(new UnderlineSpan(), 0, changePasswordTextView.getText().length(), 0);
        changePasswordTextView.setText(content);
        changePasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);


            }
        });
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery_Intent = new Intent(getApplicationContext(),
                        GalleryUtil.class);
                startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
               /* Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);*/


            }
        });


    }

    String picturePath, imageData1;
    Bitmap selectedBitmap;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_ACTIVITY_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                picturePath = data.getStringExtra("picturePath");

                Log.d("ok", "--->" + picturePath);
                // perform Crop on the Image Selected from Gallery
                performCrop(picturePath);
            }
        }
  /* if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            picturePath = data.getStringExtra("picturePath");
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            profilePic.setImageBitmap(photo);
            *//*performCrop(picturePath);*//*
        }*/
        if (requestCode == RESULT_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("add image", "--->");
                Bundle extras = data.getExtras();
                selectedBitmap = extras.getParcelable("data");
                profilePic.setImageBitmap(selectedBitmap);
                MainActivity.navigationProfileImageView.setImageBitmap(selectedBitmap);
               /* Bitmap.createScaledBitmap(selectedBitmap, 140, 140, true);*/
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                byte[] byte_arr = stream.toByteArray();
                String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);

                try {
                    getImageUpload("http://www.dealsweb.com/deals/ws/ProfilePic/", image_str);
                } catch (AuthFailureError authFailureError) {
                    authFailureError.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }
    }

    private void performCrop(String picUri) {
        try {
            // Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast
                    .makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void getImageUpload(String url, String baseFormate) throws AuthFailureError, JSONException {

        Log.d("profile request url", "--->" + url);
        profileArrayList.clear();
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("LgnId", singleton.getLoginUserId());
        jsonBody.put("ImageBase64", baseFormate);


        final String mRequestBody = jsonBody.toString();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.d(" profileimage  response", "--->" + response);
                        if(response!=null)
                        {

                            singleton.setLoginImage("http://www.dealsweb.com" + response.replace("\"", ""));

                            session.createLoginSession(singleton.getLoginUserId(), singleton.getLoginUserDisplayName(), singleton.getLoginUserEmailId(), singleton.getLoginImage());

                        }
                        else{
                            Picasso.with(ProfileActivity.this).load(singleton.getLoginImage()).into(profilePic);
                        }



                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Sorry! Server could not be reached.", Toast.LENGTH_LONG).show();
                Log.d("sub categoryerror", "--->" + error);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {


                return mRequestBody.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<String, String>();


                params1.put("ApiKey ", serverUtilites.apiHeader);


                return params1;
            }


        };


        Volley.newRequestQueue(ProfileActivity.this).add(postRequest);


    }

    public void getProfileData(final Map<String, String> mHeaders,
                               String url) {
        singleton.userProfileData.clear();
        Log.d("profile url", "--->" + url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("profile response", "--->" + response);
                        String DisplayName = null, LoginEmail = null, Gender = null, PercentDiscount = null, ImagePath = null, Location = null, FirstName = null;
                        String LastName = null, AboutMe = null, Interests = null, LoginId = null, subscriptionEmail = null, weeklyNewsLetter = null, subscribeAlerts = null, approveImage = null, active = null, status = null;
                        try {
                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i <= jArray.length(); i++) {

                                JSONObject jsonobject = jArray.getJSONObject(i);

                                //check the condition key exists in jsonResponse
                                if (jArray.getJSONObject(i).has("FirstName")) {

                                    FirstName = jsonobject.getString("FirstName");
                                }
                                if (jArray.getJSONObject(i).has("LoginId")) {

                                    LoginId = jsonobject.getString("LoginId");
                                }
                                if (jArray.getJSONObject(i).has("LastName")) {
                                    LastName = jsonobject.getString("LastName");
                                }
                                if (jArray.getJSONObject(i).has("DisplayName")) {

                                    DisplayName = jsonobject.getString("DisplayName");
                                }
                                if (jArray.getJSONObject(i).has("LoginEmail")) {
                                    LoginEmail = jsonobject.getString("LoginEmail");
                                }
                                if (jArray.getJSONObject(i).has("Gender")) {

                                    Gender = jsonobject.getString("Gender");
                                }
                                if (jArray.getJSONObject(i).has("SubscribeEmail")) {

                                    subscriptionEmail = jsonobject.getString("SubscribeEmail");
                                }

                                if (jArray.getJSONObject(i).has("ImagePath")) {
                                    ImagePath = jsonobject.getString("ImagePath");
                                }
                                if (jArray.getJSONObject(i).has("Location")) {
                                    Location = jsonobject.getString("Location");
                                }

                                if (jArray.getJSONObject(i).has("AboutMe")) {
                                    AboutMe = jsonobject.getString("AboutMe");
                                }
                                if (jArray.getJSONObject(i).has("Interests")) {
                                    Interests = jsonobject.getString("Interests");
                                }
                                if (jArray.getJSONObject(i).has("WeeklyNSLetters")) {
                                    weeklyNewsLetter = jsonobject.getString("WeeklyNSLetters");
                                }
                                if (jArray.getJSONObject(i).has("SubscribeAlerts")) {
                                    subscribeAlerts = jsonobject.getString("SubscribeAlerts");
                                }
                                if (jArray.getJSONObject(i).has("ApproveImage")) {
                                    approveImage = jsonobject.getString("ApproveImage");
                                }
                                if (jArray.getJSONObject(i).has("Active")) {
                                    active = jsonobject.getString("Active");
                                }
                                if (jArray.getJSONObject(i).has("Status")) {
                                    status = jsonobject.getString("Status");
                                }
                                profileTotalData = FirstName + "-~-" + LastName + "-~-" + DisplayName + "-~-" + LoginEmail + "-~-" + subscriptionEmail + "-~-" + Gender + "-~-" + Location + "-~-"
                                        + ImagePath + "-~-" + AboutMe + "-~-" + Interests + "-~-" + LoginId + "-~-" + weeklyNewsLetter + "-~-" + subscribeAlerts + "-~-" + approveImage + "-~-" + active + "-~-" + status;
                                Log.d("profiledataresponse", profileTotalData);
                                singleton.userProfileData.add(profileTotalData);
                                singleton.setProfileData(profileTotalData);
                                Picasso.with(ProfileActivity.this).load("http://www.dealsweb.com" + ImagePath).into(profilePic);
                                Log.d("checking...", " " + Singleton.editProfileSaveStatus);
                                displayNameTextView.setText(DisplayName);
                                emailTextView.setText(LoginEmail);
                                locationTextView.setText(Location);
                                profileNameTextView.setText(DisplayName);
                                Log.d("checking..interests.", " " + Interests);

                                if (Interests.equals("null")) {
                                    hobbiesTextView.setText("");

                                } else
                                    hobbiesTextView.setText(Interests);


                                if (AboutMe != null) {
                                    aboutMeTextView.setText(AboutMe);
                                } else {
                                    aboutMeTextView.setText(AboutMe);
                                    aboutMeTextView.setVisibility(View.GONE);
                                }
                               /* if (approveImage.equals("0")) {
                                    profileNeedToVerifyTextView.setVisibility(View.VISIBLE);
                                } else if (approveImage.equals("1")) {
                                    profileNeedToVerifyTextView.setVisibility(View.GONE);

                                }*/

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Sorry! Server could not be reached.", Toast.LENGTH_LONG).show();
                Log.d("category error", "--->" + error);
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(ProfileActivity.this).add(postRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
singleton.setReloadPage(true);
    }

}
























































