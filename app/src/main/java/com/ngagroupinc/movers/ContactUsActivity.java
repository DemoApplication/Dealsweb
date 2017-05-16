package com.ngagroupinc.movers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projo.ServerUtilites;
import projo.Singleton;
import projo.Utilities;

public class ContactUsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String selected_spinner_item;
    List<String> user_queries;
    EditText nameEditText, emailEditText;
    Spinner sp_spinner;
    Utilities utilities;
    String userNameString, userEmailString, messageString;
    LinearLayout aboutLinearlayout;
    Bundle bundle;
    ServerUtilites serverUtilites;
    LinearLayout contactUsLinearLayout;
    Singleton singleton;
    EditText messageEditText;
    Button contactSubmitButton;
    String come_from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Log.d("am in ", "ContactUsActivity");

        sp_spinner = (Spinner) findViewById(R.id.sp_select);
        user_queries = new ArrayList<>();
        aboutLinearlayout = (LinearLayout) findViewById(R.id.llv_about);
        singleton = Singleton.getInstance();
        bundle = getIntent().getExtras();
        contactUsLinearLayout = (LinearLayout) findViewById(R.id.llv_contact_us);
        come_from = bundle.getString("tab_name");
        serverUtilites = new ServerUtilites(this);
        nameEditText = (EditText) findViewById(R.id.ed_name);
        emailEditText = (EditText) findViewById(R.id.ed_email);
        messageEditText = (EditText) findViewById(R.id.edittext_message);
        utilities = new Utilities(this);
        contactSubmitButton = (Button) findViewById(R.id.contact_btn_submit);
        if (come_from.equals("about")) {
            contactUsLinearLayout.setVisibility(View.GONE);
            aboutLinearlayout.setVisibility(View.VISIBLE);
        } else if (come_from.equals("contact_us")) {
            insertQueryItemsToList();
            DisplayUserQueriesListToSpinner();
            sp_spinner.setOnItemSelectedListener(this);
            contactSubmitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getInputValues();
                    if (userNameString.length() != 0) {
                        if(userNameString.length()>=3) {
                            if (userEmailString.length() != 0) {
                                if (utilities.isValidEmail(userEmailString)) {
                                    if (messageString.length() != 0) {
                                        if(messageString.length()>=15) {
                                            Log.d("selected_spinner_item", " " + selected_spinner_item);

                                            if (singleton.isOnline()) {
                                                String contactsUsUrl = serverUtilites.serverUrl + serverUtilites.contactUs;
                                                sendContactDetailsToServer(contactsUsUrl);
                                            } else
                                                utilities.ShowAlert("no internet connection");

                                        }else utilities.ShowAlert("please enter message between 50 to 100 characters");
                                    }else utilities.ShowAlert("please enter message between 50 to 100 characters");
                                } else
                                    utilities.ShowAlert("email is not in valid format");
                            } else
                                utilities.ShowAlert("email should not be empty");
                        }else utilities.ShowAlert("name length should be greater than 3 digits");
                    } else
                        utilities.ShowAlert("name should not be empty");

                }
            });
        }

    }


    private void DisplayUserQueriesListToSpinner() {
        ArrayAdapter<String> rooms_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, user_queries);
        rooms_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_spinner.setAdapter(rooms_adapter);

    }

    private void insertQueryItemsToList() {
        user_queries.add("Please Select");
        user_queries.add("Make a suggestion");
        user_queries.add("Report a Post");
        user_queries.add("File a Complaint");
        user_queries.add("Advertise on your site");
        user_queries.add("Ask a Question");
        user_queries.add("Leave a Compliment");
    }

    private void getInputValues() {
        userNameString = nameEditText.getText().toString().trim();
        userEmailString = emailEditText.getText().toString().trim();
        messageString = messageEditText.getText().toString().trim();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_spinner_item = parent.getItemAtPosition(position).toString();
        Log.d("selected_spinner_item",selected_spinner_item);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selected_spinner_item="Please Select";

    }

    private void sendContactDetailsToServer(String url) {

        Log.d("contactus url", "--->" + url);
        String loginUserId = singleton.getLoginUserId();
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("Name", userNameString);
            jsonBody.put("Email", userEmailString);
            jsonBody.put("Message", messageString);
        }catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = jsonBody.toString();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ContactUs response", "--->" + response);
                      //utilities.ShowAlert(response);
                        if (response.trim().equals("\"True\"")) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(
                                    ContactUsActivity.this).create();


                            // Setting Dialog Message
                            alertDialog.setMessage(getResources().getString(R.string.contactus_thank_you));


                            // Setting OK Button
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();

                                    Intent i = new Intent(ContactUsActivity.this, MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    finish();

                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                        } else if (response.trim().equals("\"null\"")) {
                            utilities.ShowAlert("please enter the details properly");
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
}

