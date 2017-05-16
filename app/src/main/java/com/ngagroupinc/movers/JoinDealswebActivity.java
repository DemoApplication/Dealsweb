package com.ngagroupinc.movers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import com.facebook.FacebookSdk;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import projo.ServerUtilites;
import projo.Singleton;
import projo.Utilities;

/**
 * Created by userone on 12/5/2016.
 */

public class JoinDealswebActivity extends Activity {
    TextView textView;

    EditText emailTextinputlayout, passwordTextinputlayout, confrimpassowrdTextinputlayout;
    EditText emailEdittext, passwordEdittext, confirmpasswordEdittext;
    Button signupButton;
    ServerUtilites serverUtilites;
    Singleton singleton;
    Utilities utilities;
    MainActivity main;
    boolean showOrHidePasswordStatus = false, showOrHideConfirmPasswordStatus = false;
    ImageView showOrHidePasswordImageView, showOrHideConfirmPasswordImageView,backArrowImageView;
    String emailString, passwordString, conformPasswordString;
    /*fb*/
    CallbackManager callbackManager;
    Button joinFacebookRelativeLayout;
    // String regexp = "/^\S*$/"; // a string consisting only of non-whitespaces

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         /* facebook intialization */
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_joindealweb_page);
        serverUtilites = new ServerUtilites(JoinDealswebActivity.this);
        textView = (TextView) findViewById(R.id.textview);
        callbackManager = CallbackManager.Factory.create();
        utilities = new Utilities(this);
        main = new MainActivity();
        joinFacebookRelativeLayout = (Button) findViewById(R.id.rll_join_fb);
     /*   String name = utilities.getColoredSpanned("Already have an account?", "#333");
        String surName = utilities.getColoredSpanned("Sign In", "#2f7bab");
        textView.setText(Html.fromHtml(name + " " + surName));*/

        SpannableString content = new SpannableString("Already Have an Account? Sign in");
        content.setSpan(new UnderlineSpan(), 25, content.length(), 0);
        textView.setText(content);

        signupButton = (Button) findViewById(R.id.signupbutton);
        backArrowImageView = (ImageView) findViewById(R.id.backarrow_join_one);

        singleton = Singleton.getInstance();
        showOrHidePasswordImageView = (ImageView) findViewById(R.id.password_eye_icon);
        showOrHideConfirmPasswordImageView = (ImageView) findViewById(R.id.confirm_password_eye_icon_join);
        emailTextinputlayout = (EditText) findViewById(R.id.email_join_edittext);
        passwordTextinputlayout = (EditText) findViewById(R.id.passoword_join_edittext);
        confrimpassowrdTextinputlayout = (EditText)findViewById(R.id.confirm_password_join_edittext);
        emailEdittext = (EditText) findViewById(R.id.email_join_edittext);
        passwordEdittext = (EditText) findViewById(R.id.passoword_join_edittext);
        passwordEdittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
        showOrHidePasswordStatus = false;
        confirmpasswordEdittext = (EditText) findViewById(R.id.confirm_password_join_edittext);
        confirmpasswordEdittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
        showOrHideConfirmPasswordStatus = false;
        showOrHidePasswordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int password_length=passwordEdittext.getText().toString().trim().length();
                Log.d("password_length", "" + password_length);

                if(password_length!=0) {
                    if (showOrHidePasswordStatus) {
                        showOrHidePasswordImageView.setImageResource(R.drawable.eye_pass);
                        // passwordEdittext.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        passwordEdittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        showOrHidePasswordStatus = false;
                    } else {
                        showOrHidePasswordImageView.setImageResource(R.drawable.hide_pass);
                        //passwordEdittext.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        passwordEdittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                        showOrHidePasswordStatus = true;

                    }
                }
            }
        });
        showOrHideConfirmPasswordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int confirm_password_length = confirmpasswordEdittext.getText().toString().length();
                if (confirm_password_length != 0) {

                if (showOrHideConfirmPasswordStatus) {

                    Log.d("confirm_password_length", "" + confirm_password_length);
                        showOrHideConfirmPasswordImageView.setImageResource(R.drawable.eye_pass);
                        // passwordEdittext.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        confirmpasswordEdittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        showOrHideConfirmPasswordStatus = false;
                    } else {
                        showOrHideConfirmPasswordImageView.setImageResource(R.drawable.hide_pass);
                        //passwordEdittext.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        confirmpasswordEdittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                        showOrHideConfirmPasswordStatus = true;

                    }
                }
            }
        });
       /* passwordEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordEdittext.setTransformationMethod();
            }
        });*/
       /* showOrHidePasswordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrHidePasswordStatus=true;
                passwordEdittext.setTransformationMethod(null);
                showOrHidePasswordImageView.setImageResource(R.drawable.hide_pasd_icon);

            }
        });*/
        joinFacebookRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signinwithFb();
            }
        });
        emailEdittext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                Log.d("tracking check", "--->"
                        + (actionId == EditorInfo.IME_ACTION_DONE));
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    passwordEdittext.requestFocus();

                    return true;
                }
                return false;
            }
        });
        passwordEdittext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                Log.d("tracking check1", "--->"
                        + (actionId == EditorInfo.IME_ACTION_DONE));
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    confirmpasswordEdittext.requestFocus();

                    return true;
                }
                return false;
            }
        });

        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();


            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(JoinDealswebActivity.this, LoginActivity.class);
                startActivity(i);
                finish();

            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailString = emailEdittext.getText().toString();
                passwordString = passwordEdittext.getText().toString();
                conformPasswordString = confirmpasswordEdittext.getText().toString();
                if (!emailString.isEmpty()) {
                    if (!passwordString.isEmpty()) {
                        if (!conformPasswordString.isEmpty()) {
                            boolean valid_email=   utilities.isValidEmail(emailString);

                            if(valid_email) {
                                if (passwordString.equals(conformPasswordString)) {
                                    Log.d("passwordString_length", " " + passwordString.length());
                                    Log.d("conformPassStringlen", " " + conformPasswordString.length());

                                    if ((passwordString.length() >= 6) && (conformPasswordString.length() >= 6)) {
                                        Pattern pattern = Pattern.compile("\\s");
                                        Matcher matcher = pattern.matcher(passwordString);
                                        Matcher matcher1 = pattern.matcher(conformPasswordString);
                                        boolean found = matcher.find();
                                        boolean found1 = matcher1.find();
                                        if (found || found1) {
                                            utilities.ShowAlert("password should not contain  white spaces");
                                        } else {
                                            if (singleton.isOnline()) {
                                                Map<String, String> apiHeader = new HashMap<String, String>();
                                                apiHeader.put("ApiKey ", serverUtilites.apiHeader);

                                                String registrationApi = serverUtilites.serverUrl + "" + serverUtilites.Register;
                                                getRegistrationResponse(registrationApi);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    if ((passwordString.length() < 6) || conformPasswordString.length() < 6)
                                        utilities.ShowAlert("Password length should not be less than 6 characters");

                                } else {
                                    utilities.ShowAlert("Password and ConfirmPassword not equal");
                                }
                            }else{
                                utilities.ShowAlert("your email is not in valid format");

                            }
                        } else {
                            utilities.ShowAlert("ConfirmPassword Empty");
                        }
                    } else {
                        utilities.ShowAlert("Password Empty");
                    }
                } else {
                    utilities.ShowAlert("Email Empty");
                }

            }


        });


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

        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email", "user_about_me"));
        final LoginBehavior WEB_VIEW_ONLY;

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("call fromloginmanager", "Loginmanager");
                if (AccessToken.getCurrentAccessToken() != null) {
                    Utilities.getInstance().RequestData(getApplicationContext(),new MainActivity.SomeFBListener<String>() {
                        @Override
                        public void getFBResult(String response) {
                            Log.d("after_calling_fb", response);
                            utilities.cancelProgressDialog();
                            String result = response;
                            if (result != null) {

                                //MainActivity.profile.setImageResource(R.drawable.profile_icon_login);
                                MainActivity.relativeLayoutPopup.setVisibility(View.GONE);
                                MainActivity.linearLayoutViewFooter.setVisibility(View.GONE);


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

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // tv_username.setText(Utilities.first_name);
      /*  tv_username.setText(Utilities.first_name);
        Log.d("first_name", Utilities.first_name);*/
// where to set the firstname to the text

    }

    public void getRegistrationResponse(String url) {
        Log.d("join url", "--->" + url);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("Email", emailString);
            jsonBody.put("Password", passwordString);
            Log.d("Email", "--->" + emailString);
            Log.d("Password", "--->" + passwordString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = jsonBody.toString();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("join response", "--->" + response);
                        displayAlearDialog(response.replace("\"", ""));

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

            /*@Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
Log.d("response","--->"+response);
                    responseString = String.valueOf(response.statusCode);
Log.d("response string","--->"+responseString);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }*/
        };


        Volley.newRequestQueue(getApplicationContext()).add(postRequest);


    }

    private void displayAlearDialog(String dialogString) {

        final AlertDialog alertDialog = new AlertDialog.Builder(
                JoinDealswebActivity.this).create();


        // Setting Dialog Message
        alertDialog.setMessage(dialogString);


        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();

                Intent i = new Intent(JoinDealswebActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
