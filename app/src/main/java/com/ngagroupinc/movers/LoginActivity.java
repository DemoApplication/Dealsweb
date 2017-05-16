package com.ngagroupinc.movers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import projo.ServerUtilites;
import projo.SessionManager;
import projo.Singleton;
import projo.Utilities;

/**
 * Created by userone on 12/5/2016.
 */

public class LoginActivity extends Activity {

    Button signinbutton;
    TextView fogotpasswordTextview;
    ImageView backArrowImageView;
    EditText emailTextinputlayout, passwordTextinputlayout;
    EditText emailEdittext, passwordEdittext;
    String emailString, passwordString = null;
    ServerUtilites serverUtilites;
    Singleton singleton;
    Utilities utilities;
    boolean showOrHidePasswordStatus = false;
    ImageView showOrHidePasswordImageView;
    TextView JoinNowtextview;
    SessionManager session;
    CallbackManager callbackManager;

    Button faceBookRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        singleton = Singleton.getInstance();
        utilities = new Utilities(this);
        session = new SessionManager(getApplicationContext());
        fogotpasswordTextview = (TextView) findViewById(R.id.forgotpasswordTextview);
        showOrHidePasswordImageView = (ImageView) findViewById(R.id.passw_eye_icon);
        serverUtilites = new ServerUtilites(LoginActivity.this);
        JoinNowtextview = (TextView) findViewById(R.id.jointextview);
       /* String name = utilities.getColoredSpanned("Already have an account?", "#333");
        String surName = utilities.getColoredSpanned("Join Now", "#2f7bab");
        JoinNowtextview.setText(Html.fromHtml(name + " " + surName));*/
        SpannableString content = new SpannableString("Don't Have an Account? Join now");
        content.setSpan(new UnderlineSpan(), 23, content.length(), 0);
        JoinNowtextview.setText(content);
        emailTextinputlayout = (EditText) findViewById(R.id.email_login_edittext);
        passwordTextinputlayout = (EditText) findViewById(R.id.password_login_edittext);
        faceBookRelativeLayout = (Button) findViewById(R.id.login_facebook);
        emailEdittext = (EditText) findViewById(R.id.email_login_edittext);
        passwordEdittext = (EditText) findViewById(R.id.password_login_edittext);
        callbackManager = CallbackManager.Factory.create();
        passwordEdittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
        showOrHidePasswordStatus = false;

        signinbutton = (Button) findViewById(R.id.signinbutton);
        backArrowImageView = (ImageView) findViewById(R.id.backarrow_join);
        showOrHidePasswordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int password_length=passwordEdittext.getText().toString().trim().length();

                if (showOrHidePasswordStatus) {
                    if(password_length!=0) {
                        Log.d("password_length", " " + password_length);

                        Log.d("signinpasstrue", " " + showOrHidePasswordStatus);
                        showOrHidePasswordImageView.setImageResource(R.drawable.eye_pass);
                        // passwordEdittext.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        passwordEdittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        showOrHidePasswordStatus = false;
                    }
                } else {
                    if(password_length!=0) {
                        Log.d("signinpassfal", " " + showOrHidePasswordStatus);

                        showOrHidePasswordImageView.setImageResource(R.drawable.hide_pass);
                        //passwordEdittext.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        passwordEdittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                        showOrHidePasswordStatus = true;
                    }

                }
            }
        });
        faceBookRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signinwithFb();
            }
        });
        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailString = emailEdittext.getText().toString();
                passwordString = passwordEdittext.getText().toString();
                Log.d("entered email", "" + emailString);
                Log.d("entered password", "" + passwordString);
                if (!emailString.isEmpty()) {
                    if (!passwordString.isEmpty()) {
                        boolean valid_email = utilities.isValidEmail(emailString);
                        if (valid_email) {
                            if (singleton.isOnline()) {
                                Map<String, String> apiHeader = new HashMap<String, String>();
                                apiHeader.put("ApiKey ", serverUtilites.apiHeader);

                                String registrationApi = serverUtilites.serverUrl + "" + serverUtilites.checkLogin;
                                getCheckLoginResponse(registrationApi);
                            } else {
                                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            utilities.ShowAlert("your email is not in valid format");
                        }

                    } else {
                        utilities.ShowAlert("Password Empty");
                    }
                } else {
                    utilities.ShowAlert("Email Empty");
                }

            }
        });
        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();


            }
        });
        JoinNowtextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, JoinDealswebActivity.class);
                startActivity(i);
                finish();

            }
        });


        fogotpasswordTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailString = emailEdittext.getText().toString();
                passwordString = passwordEdittext.getText().toString();
                Log.d("email(forgot)", "" + emailString);

                //  if(emailString!=null)
                //  {
                startActivity(new Intent(LoginActivity.this, FotgotPasswordActivity.class));
                Log.d("email(forgot)", emailString);
                Map<String, String> apiHeader = new HashMap<String, String>();
                apiHeader.put("ApiKey ", serverUtilites.apiHeader);

            }
            // else{

            // DisplayToast("Email should not be Empty!");
            // }
               /* Intent i = new Intent(LoginActivity.this, FotgotPasswordActivity.class);
                startActivity(i);
                finish();*/
            // }
        });
    }


    public void getCheckLoginResponse(String url) {

        Log.d("login url", "--->" + url);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("Email", emailString);
            jsonBody.put("Password", passwordString);
            Log.d("Email L", "--->" + emailString);
            Log.d("Password L", "--->" + passwordString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = jsonBody.toString();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("join response", "--->" + response);
                        String DisplayName = null, LoginId = null, LoginEmail = null, Active = null, Status = null, ImagePath = null;
                        try {
                            JSONArray jArray = new JSONArray(response);


                            if (jArray.length() == 0) {
                                Log.d("join response12", "--->" + jArray.length());
                                serverUtilites.ShowAlert("Please enter valid E-mail or Password!");
                            } else {
                                JSONObject jsonobject = jArray.getJSONObject(0);
                                Log.d("else join response12", "--->" + jArray.length());
                                if (jArray.getJSONObject(0).has("DisplayName")) {

                                    DisplayName = jsonobject.getString("DisplayName");

                                }
                                if (jArray.getJSONObject(0).has("LoginId")) {

                                    LoginId = jsonobject.getString("LoginId");

                                }
                                if (jArray.getJSONObject(0).has("LoginEmail")) {

                                    LoginEmail = jsonobject.getString("LoginEmail");

                                }
                                if (jArray.getJSONObject(0).has("Active")) {

                                    Active = jsonobject.getString("Active");

                                }
                                if (jArray.getJSONObject(0).has("Status")) {

                                    Status = jsonobject.getString("Status");

                                }
                                if (jArray.getJSONObject(0).has("ImagePath")) {

                                    ImagePath = jsonobject.getString("ImagePath");

                                }
                                if (Active.equals("0")) {
                                    serverUtilites.ShowAlert("Email Validation Required");
                                } else if (Status.equals("0")) {
                                    serverUtilites.ShowAlert("Your account blocked by Admin");
                                } else {
                                    singleton.setLoginUserDisplayName(DisplayName);
                                    singleton.setLoginUserEmailId(LoginEmail);
                                    singleton.setLoginUserId(LoginId + "");


                                    singleton.setLoginImage("http://www.dealsweb.com" + ImagePath);
                                    session.createLoginSession(LoginId, DisplayName, LoginEmail, "http://www.dealsweb.com" + ImagePath);
                                    utilities.checkForUserLogin(getApplicationContext());

                                    singleton.setReloadPage(true);
                                    onBackPressed();
                                }

                            }

                        } catch (JSONException e) {

                            Log.d("exception", "--->" + e);
                        }


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

                return mRequestBody.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<String, String>();


                params1.put("ApiKey ", "3AF8YG58-7DAC-4E2F-B4E2-E1E9A87SZ2FF");


                return params1;
            }


        };


        Volley.newRequestQueue(getApplicationContext()).add(postRequest);


    }

    private void signinwithFb() {
        if (singleton.isOnline()) {
            Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_SHORT).show();
            utilities.showProgressDialog();

            facebook();
        } else {
            utilities.ShowAlert("No Internet Connection");
            Toast.makeText(getApplicationContext(), "no internet available", Toast.LENGTH_SHORT).show();
        }

    }

    private void facebook() {

        LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email", "user_about_me"));
        final LoginBehavior WEB_VIEW_ONLY;

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("call fromloginmanager", "Loginmanager");
                if (AccessToken.getCurrentAccessToken() != null) {
                    Utilities.getInstance().RequestData(getApplicationContext(), new MainActivity.SomeFBListener<String>() {
                        @Override
                        public void getFBResult(String response) {
                            Log.d("after_calling_fb", response);
                            utilities.cancelProgressDialog();
                            String result = response;
                            if (result != null) {

                                MainActivity.relativeLayoutPopup.setVisibility(View.GONE);
                                MainActivity.linearLayoutViewFooter.setVisibility(View.GONE);
                                //MainActivity.profile.setImageResource(R.drawable.profile_icon_login);
                                utilities.checkForUserLogin(getApplicationContext());

                                Toast.makeText(getApplicationContext(), "you have your account with dealsweb", Toast.LENGTH_SHORT).show();

                                finish();

                            }


                        }

                        @Override
                        public void getFBErrorResult(String String) {
                            utilities.ShowAlert(String);

                        }
                    });
                }
            }

            @Override
            public void onCancel() {

                utilities.DisplayToast("Facebook got canceled");
            }

            @Override
            public void onError(FacebookException error) {
                utilities.DisplayToast("Facebook login error");
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

}
