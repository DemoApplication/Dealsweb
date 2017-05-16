package com.ngagroupinc.movers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import projo.ServerUtilites;
import projo.SessionManager;
import projo.Singleton;
import projo.Utilities;

/**
 * Created by Userone on 1/11/2017.
 */
public class EditProfileActivity extends AppCompatActivity {
    EditText firstNameEditText, lastNameEditText, displayNameEdittext, subscriptionEmailEditText, genderEditText, locationEditText, aboutmeEditText, hobbiesEditText;
    String ProfileData[] = null;
    Singleton singleton;
    ServerUtilites serverUtilites;
    ImageView backArrowImageView;
    CheckBox weeklyCheckBox, dealCheckBox;
    String gender_string;
    boolean weekly_status, deal_alerts_status;
    Button saveButton;
    Utilities utilities;
    String loginUserIdString, firstNameString, lastNameString, displayNameString, subscriptionEmailString, genderString, locationString, aboutMeString, interestsString, weeklyNewsLettersString, subscribeDealAlertsString;
SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        singleton = Singleton.getInstance();
        firstNameEditText = (EditText) findViewById(R.id.first_name_edittext);
        lastNameEditText = (EditText) findViewById(R.id.last_name_edittext);
        displayNameEdittext = (EditText) findViewById(R.id.display_name_edittext);
        subscriptionEmailEditText = (EditText) findViewById(R.id.email_edittext);
        genderEditText = (EditText) findViewById(R.id.gender_edittext);
        locationEditText = (EditText) findViewById(R.id.location_edittext);
        aboutmeEditText = (EditText) findViewById(R.id.aboutme_edittext);
        hobbiesEditText = (EditText) findViewById(R.id.interests_edittext);
        backArrowImageView = (ImageView) findViewById(R.id.backarrow_profile1);
        saveButton = (Button) findViewById(R.id.saveprefrences_button_edit_profile);
        weeklyCheckBox = (CheckBox) findViewById(R.id.checkbox_weekly);
        dealCheckBox = (CheckBox) findViewById(R.id.checkbox_deal);
        ProfileActivity profile = new ProfileActivity();
        firstNameEditText.setText("");
        lastNameEditText.setText("");
        displayNameEdittext.setText("");
        subscriptionEmailEditText.setText("");
        genderEditText.setText("");
        locationEditText.setText("");
        aboutmeEditText.setText("");
        hobbiesEditText.setText("");
        weeklyCheckBox.setChecked(false);
        dealCheckBox.setChecked(false);
        utilities = new Utilities(this);
        serverUtilites = new ServerUtilites(this);
        int length = singleton.userProfileData.size();
        session=new SessionManager(this);
        Log.d("checkprofdateditprofile", " " + length);
        for (int i = 0; i < singleton.userProfileData.size(); i++) {
            ProfileData = singleton.userProfileData.get(i).split("-~-");

        }
        Log.d("firstNameEditText", ProfileData[0]);
        Log.d("lastNameEditText", ProfileData[1]);
        Log.d("displayNameEdittext", ProfileData[2]);
        Log.d("subscriptionEmail", ProfileData[4]);
        Log.d("genderEditText", ProfileData[5]);
        Log.d("locationEditText", ProfileData[6]);
        Log.d("aboutmeEditText", ProfileData[8]);
        Log.d("hobbiesEditText", ProfileData[9]);
        Log.d("weeklyCheckBox", ProfileData[11]);
        Log.d("dealCheckBox", ProfileData[12]);
        if (ProfileData[0].equals("null")) {
            firstNameEditText.setText("");
        } else {
            firstNameEditText.setText(ProfileData[0]);

        }
        if (ProfileData[1].equals("null")) {
            lastNameEditText.setText("");

        } else {
            lastNameEditText.setText(ProfileData[1]);

        }
        if (ProfileData[2].equals("null")) {
            displayNameEdittext.setText("");

        } else {
            displayNameEdittext.setText(ProfileData[2]);

        }
        if (ProfileData[4].equals("null")) {
            subscriptionEmailEditText.setText("");

        } else {
            subscriptionEmailEditText.setText(ProfileData[4]);
        }
        if (ProfileData[5].equals("2")) {
            genderEditText.setText("Female");
        } else {
            genderEditText.setText("Male");
        }
        if (ProfileData[6].equals("null")) {
            locationEditText.setText("");


        } else {
            locationEditText.setText(ProfileData[6]);

        }
        if (ProfileData[8].equals("null")) {
            aboutmeEditText.setText("");

        } else {
            aboutmeEditText.setText(ProfileData[8]);

        }
        if (ProfileData[9].equals("null")) {
            hobbiesEditText.setText("");

        } else {
            hobbiesEditText.setText(ProfileData[9]);

        }
        if (ProfileData[11].equals("1")) {
            weeklyCheckBox.setChecked(true);

        } else if (ProfileData[11].equals("0")) {
            weeklyCheckBox.setChecked(false);

        }
        if (ProfileData[12].equals("1")) {
            dealCheckBox.setChecked(true);
        } else if (ProfileData[12].equals("0")) {
            dealCheckBox.setChecked(false);

        }
        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();


            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserIdString = singleton.getLoginUserId();
                firstNameString = firstNameEditText.getText().toString().trim();
                lastNameString = lastNameEditText.getText().toString().trim();
                displayNameString = displayNameEdittext.getText().toString().trim();
                subscriptionEmailString = subscriptionEmailEditText.getText().toString().trim();
                gender_string = genderEditText.getText().toString().trim();
                locationString = locationEditText.getText().toString().trim();
                aboutMeString = aboutmeEditText.getText().toString().trim();
                interestsString = hobbiesEditText.getText().toString().trim();
                weekly_status = weeklyCheckBox.isChecked();
                deal_alerts_status = dealCheckBox.isChecked();
                if (weekly_status) {
                    weeklyNewsLettersString = "1";

                } else {
                    weeklyNewsLettersString = "0";
                }
                if (deal_alerts_status) {
                    subscribeDealAlertsString = "1";
                } else {
                    subscribeDealAlertsString = "0";
                }
                if (gender_string.equals("Male")) {
                    genderString = "1";

                } else if (gender_string.equals("Female")) {
                    genderString = "2";

                }
                String profileString = serverUtilites.serverUrl + serverUtilites.editProfile;

                Map<String, String> apiHeader = new HashMap<String, String>();
                apiHeader.put("ApiKey ", serverUtilites.apiHeader);


                getSaveProfileData(profileString);

            }
        });

    }

    public void getSaveProfileData(String url) {

        Log.d("Edit profile url", "--->" + url);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("LgnId", loginUserIdString);
            jsonBody.put("FirstName", firstNameString);
            jsonBody.put("LastName", lastNameString);
            jsonBody.put("DisplayName", displayNameString);
            jsonBody.put("SbscrbEmail", subscriptionEmailString);
            jsonBody.put("Gender", genderString);
            jsonBody.put("Location", locationString);
            jsonBody.put("AboutMe", aboutMeString);
            jsonBody.put("Intrests", interestsString);
            jsonBody.put("WklyNSLetters", weeklyNewsLettersString);
            jsonBody.put("SubscribeAlrts", subscribeDealAlertsString);
            Log.d("loginUserIdString ep", "--->" + loginUserIdString);
            Log.d("firstNameString ep", "--->" + firstNameString);
            Log.d("lastNameString ep", "--->" + lastNameString);
            Log.d("displayNameString ep", "--->" + displayNameString);
            Log.d("subscriptionEmail ep", "--->" + subscriptionEmailString);
            Log.d("genderString ep", "--->" + genderString);
            Log.d("locationString ep", "--->" + locationString);
            Log.d("aboutMeString ep", "--->" + aboutMeString);
            Log.d("interestsString ep", "--->" + interestsString);
            Log.d("weeklyNewsLetters ep", "--->" + weeklyNewsLettersString);
            Log.d("SubscribeAlrts ep", "--->" + subscribeDealAlertsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = jsonBody.toString();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Edit profile response", "--->" + response);
                        if (response.trim().equals("\"Y\"")) {
                            Toast.makeText(EditProfileActivity.this, " Your Profile Details updated Successfully!", Toast.LENGTH_SHORT).show();
                            Singleton.editProfileSaveStatus = true;
                            singleton.setLoginUserDisplayName(displayNameEdittext.getText().toString());
                            session.createLoginSession(singleton.getLoginUserId(), singleton.getLoginUserDisplayName(), singleton.getLoginUserEmailId(), singleton.getLoginImage());

                            Intent intent= new Intent(EditProfileActivity.this,ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else if (response.trim().equals("\"N\"")) {
                            utilities.ShowAlert("Current Password is wrong,please enter correct password");
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
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Log.d("body", "--->" + mRequestBody);
                return mRequestBody.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<String, String>();


                params1.put("ApiKey ", "3AF8YG58-7DAC-4E2F-B4E2-E1E9A87SZ2FF");
                        /*params1.put("Content-Type ","application/json");
                params1.put("Accept","application/json");*/

                return params1;
            }

        };


        Volley.newRequestQueue(getApplicationContext()).add(postRequest);


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
