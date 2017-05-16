package com.ngagroupinc.movers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import projo.Utilities;

/**
 * Created by userone on 12/5/2016.
 */

public class FotgotPasswordActivity extends Activity {


    ServerUtilites serverUtilites;
    Utilities utilities;
    EditText emailToRecoverPasswordEditText;
    String emailToRecoverPasswordString = null;
    Button resetPasswordButton;
    ImageView backArrowImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);
        serverUtilites = new ServerUtilites(this);
        utilities = new Utilities(this);

        backArrowImageView=(ImageView)findViewById(R.id.backarrow_reset_one) ;
        emailToRecoverPasswordEditText = (EditText) findViewById(R.id.emailToRecoverPassword);
        resetPasswordButton = (Button) findViewById(R.id.btn_forgot_password);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailToRecoverPasswordString = emailToRecoverPasswordEditText.getText().toString().trim();
                String SubScriptionsString = ServerUtilites.serverUrl + serverUtilites.ForgotPassword;
                getForgotpasswordeData(SubScriptionsString);

            }
        });

      /*  backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();


            }
        });
*/
        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();


            }
        });
    }

    public void getForgotpasswordeData(String url) {
        Log.d("forgot passworf request url", "--->" + url);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("Email", emailToRecoverPasswordString);
            Log.d("Email F", "--->" + emailToRecoverPasswordString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = jsonBody.toString();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(" forgot password update response", "--->" + response);
                        if (response.equals("\"success\""))
                        {
                            serverUtilites.ShowAlert(getResources().getString(R.string.success_forgot_password));
                        }

                        else {
                            serverUtilites.ShowAlert(getResources().getString(R.string.fail_forgot_password));
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

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

}
