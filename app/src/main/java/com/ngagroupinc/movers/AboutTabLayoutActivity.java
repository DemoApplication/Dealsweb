package com.ngagroupinc.movers;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.Calendar;

import projo.ServerUtilites;
import projo.Singleton;

/**
 * Created by user1 on 20-Jan-17.
 */

public class AboutTabLayoutActivity extends TabActivity {
    TabHost tabs;
    public static TabWidget tabWidget;
    int tab_position;
    ScrollView sv_privacy_policy;
    ProgressBar progressBar;
    ImageView back_arrow;
    WebView mWebView;
    ServerUtilites serverUtilities;
    FrameLayout tab_content;
    TextView tv_year_privacy;
    RelativeLayout llayout1, llayout2, llayout3;
    TextView tabs_name;
    RelativeLayout privacyPolicyRelativelayout;
    TextView tv_header;
    View view_tabs;
    //j,k,l for display tab once
    int j = 0, k = 0, l = 0;
    TextView AboutTextView, privacyPolicyTextView, contactUsTextView;
    RelativeLayout tab1 = null, tab2 = null, tab3 = null;
    TabHost.TabSpec tab;
    Singleton singleton;
    TextView tv_privacy_policy;
    LinearLayout aboutLinearlayout;
    private static final String TABS_NAME = "AboutTabLayoutActivity";
    MyTabIndicator myTab;
    Dialog dialog;

    private static final String[] TAB_NAMES = {"About", "Privacy Policy", "Contact Us"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_tabslayout);
/*
        TabHost tabHost = getTabHost();
*/
        tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabWidget = tabs.getTabWidget();
        sv_privacy_policy = (ScrollView) findViewById(R.id.sv_privacy_policy);
        //aboutLinearlayout=(LinearLayout)findViewById(R.id.llv_about);
        //  mWebView = (WebView) findViewById(R.id.webview1);
        serverUtilities = new ServerUtilites(this);
        tab_content = (FrameLayout) findViewById(android.R.id.tabcontent);
        tv_year_privacy = (TextView) findViewById(R.id.tv_year_privacy);
        privacyPolicyRelativelayout=(RelativeLayout)findViewById(R.id.rll_web_view);
        dialog = new Dialog(this);
        singleton = Singleton.getInstance();
        tv_header = (TextView) findViewById(R.id.tab_text);
        tv_privacy_policy = (TextView) findViewById(R.id.tv_privacy_policy);
        // progressBar = (ProgressBar) findViewById(R.id.progressBar);
        back_arrow = (ImageView) findViewById(R.id.backarrow);
        tab_content.setVisibility(View.VISIBLE);
        sv_privacy_policy.setVisibility(View.GONE);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        for (int i = 0; i < TAB_NAMES.length; i++) {
            tab = tabs.newTabSpec(TABS_NAME);

            tab_position = i;
            myTab = new MyTabIndicator(this, TAB_NAMES[i], (i + 1));
            tab.setIndicator(myTab);
            // aboutLinearlayout.setVisibility(View.VISIBLE);
            tab_content.setVisibility(View.VISIBLE);

            Intent intent = new Intent(AboutTabLayoutActivity.this,ContactUsActivity.class);
            intent.putExtra("tab_name", "about");
            tv_header.setText("About");

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            tab.setContent(intent);
            tabs.addTab(tab);
            tabWidget.setCurrentTab(0);
            tabs.setCurrentTab(0);

        }
        tabWidget.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //  if (j != 0) {
/*
                    aboutLinearlayout.setVisibility(View.VISIBLE);
*/
                    sv_privacy_policy.setVisibility(View.GONE);
                    tab_content.setVisibility(View.VISIBLE);
                    Intent about = new Intent(AboutTabLayoutActivity.this, ContactUsActivity.class);
                    about.putExtra("tab_name", "about");
                    tv_header.setText("About");


                    //}
                } catch (Exception e) {

                }

                Log.d("clicked tab1", " " + j);

                AboutTextView.setTextColor(getResources().getColor(R.color.btn_text_color));
                privacyPolicyTextView.setTextColor(getResources().getColor(R.color.lite_green));
                contactUsTextView.setTextColor(getResources().getColor(R.color.lite_green));


                tabWidget.setCurrentTab(0);
                tabs.setCurrentTab(0);
                j++;
                k = 0;
                l = 0;
            }
        });
        tabWidget.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                aboutLinearlayout.setVisibility(View.GONE);
*/
                privacyPolicyRelativelayout.setVisibility(View.VISIBLE);
                sv_privacy_policy.setVisibility(View.VISIBLE);
                tab_content.setVisibility(View.GONE);
                tv_header.setText("Privacy Policy");

                displayWebView();

              /*  try {
                    //  if (j != 0) {
                    Intent privacy_policy = new Intent(AboutTabLayoutActivity.this, PrivacyPolicyActivity.class);
                    privacy_policy.putExtra("tab_name", "Privacy_Policy");
                    Log.d("call tab2", " " + j);

                    privacy_policy.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    tab.setContent(privacy_policy);
                    tabs.addTab(tab);
                    //}
                } catch (Exception e) {

                }

                Log.d("clicked tab2"," " +j);

                AboutTextView.setTextColor(getResources().getColor(R.color.lite_green));
                privacyPolicyTextView.setTextColor(getResources().getColor(R.color.btn_text_color));
                contactUsTextView.setTextColor(getResources().getColor(R.color.lite_green));


                tabWidget.setCurrentTab(1);
                tabs.setCurrentTab(1);
                j++;
                k = 0;
             */
                l = 0;
                Log.d("clicked tab2", " " + j);

                AboutTextView.setTextColor(getResources().getColor(R.color.lite_green));
                privacyPolicyTextView.setTextColor(getResources().getColor(R.color.btn_text_color));
                contactUsTextView.setTextColor(getResources().getColor(R.color.lite_green));


                tabWidget.setCurrentTab(1);
                tabs.setCurrentTab(1);
            }
        });
        tabWidget.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    //if (l != 0) {
/*
                    aboutLinearlayout.setVisibility(View.GONE);
*/
                    sv_privacy_policy.setVisibility(View.GONE);
                    tab_content.setVisibility(View.VISIBLE);
                    Intent contact_us = new Intent(AboutTabLayoutActivity.this,ContactUsActivity.class);
                    contact_us.putExtra("tab_name", "contact_us");
                    tv_header.setText("Contact Us");

                    Log.d("call tab3", " " + l);

                    contact_us.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    tab.setContent(contact_us);
                    tabs.addTab(tab);
                    //}
                } catch (Exception e) {

                }

                Log.d("clicked tab3", " " + l);

                AboutTextView.setTextColor(getResources().getColor(R.color.lite_green));
                privacyPolicyTextView.setTextColor(getResources().getColor(R.color.lite_green));
                contactUsTextView.setTextColor(getResources().getColor(R.color.btn_text_color));
                tabWidget.setCurrentTab(2);
                tabs.setCurrentTab(2);

            }
        });

    }

    public class MyTabIndicator extends LinearLayout {
        public MyTabIndicator(Context context, String label, int tabId) {
            super(context);


            this.setGravity(Gravity.CENTER);

            if (tabId == 1) {
                tab1 = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.custom_tabs_layout, null);
                Log.d("calling from", "tab1");
                if (tab1.getParent() != null) {
                    ((LinearLayout) tab1.getParent()).removeView(tab1);
                }
                llayout1 = (RelativeLayout)tab1.findViewById(R.id.tabs_layout);
                llayout1.setPadding(0, 6, 0, 10);

                AboutTextView = (TextView) tab1.findViewById(R.id.tabs_name);
                AboutTextView.setText(label);
                AboutTextView.setTextColor(getResources().getColor(R.color.btn_text_color));
                this.addView(tab1);
            } else if (tabId == 2) {
                tab2 = (RelativeLayout) LayoutInflater.from(context).inflate(
                        R.layout.custom_tabs_layout, null);
                Log.d("calling from", "tab2");

                if (tab2.getParent() != null) {
                    ((LinearLayout) tab2.getParent()).removeView(tab2);
                }
                llayout2 = (RelativeLayout) tab2.findViewById(R.id.tabs_layout);
                llayout2.setPadding(0, 6, 0, 10);
                privacyPolicyTextView = (TextView) tab2.findViewById(R.id.tabs_name);
                privacyPolicyTextView.setText(label);
                privacyPolicyTextView.setTextColor(getResources().getColor(R.color.lite_green));

                this.addView(tab2);
            } else if (tabId == 3) {
                tab3 = (RelativeLayout) LayoutInflater.from(context).inflate(
                        R.layout.custom_tabs_layout, null);
                Log.d("calling from", "tab3");

                if (tab3.getParent() != null) {
                    ((LinearLayout) tab3.getParent()).removeView(tab3);
                }
                llayout3 = (RelativeLayout) tab3.findViewById(R.id.tabs_layout);
                llayout3.setPadding(0, 8, 0, 10);
                view_tabs = (View) tab3.findViewById(R.id.view_tabs);

                contactUsTextView = (TextView) tab3.findViewById(R.id.tabs_name);
                contactUsTextView.setText(label);
                contactUsTextView.setTextColor(getResources().getColor(R.color.lite_green));
                view_tabs.setVisibility(GONE);
                this.addView(tab3);
            }
        }
    }


    /*  // Tab for About
      TabHost.TabSpec about_spec = tabHost.newTabSpec("About");
      // setting Title and Icon for the Tab
      about_spec.setIndicator("About");
      Intent photosIntent = new Intent(this, AboutActivity.class);
      about_spec.setContent(photosIntent);

      // Tab for PrivacyPolicy
      TabHost.TabSpec privacy_policy_spec = tabHost.newTabSpec("Privacy Policy");
      privacy_policy_spec.setIndicator("Privacy Policy");
      Intent songsIntent = new Intent(this, PrivacyPolicyActivity.class);
      privacy_policy_spec.setContent(songsIntent);

      // Tab for Contact Us
      TabHost.TabSpec contact_us_spec = tabHost.newTabSpec("Contact Us");
      contact_us_spec.setIndicator("Contact Us");
      Intent videosIntent = new Intent(this, ContactUsActivity.class);
      contact_us_spec.setContent(videosIntent);

      // Adding all TabSpec to TabHost
      tabHost.addTab(about_spec); // Adding About tab
      tabHost.addTab(privacy_policy_spec); // Adding Privacy Policy tab
      tabHost.addTab(contact_us_spec); // Adding Contact Us tab
  }*/
    private void displayWebView() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        Log.d("displaying web view ", "privacy_policy");
        String privacy_policy = "<p>A hearty welcome to Dealsweb.com. Our site, which is operated by the NGA Group, Inc. dba Dealsweb.com, provides you with the latest online deals and coupons from the biggest online retailers. We value our users and the services we provide on this site. We have implemented a strict privacy policy to ensure that your personal information is secure with us.</p>\n" +
                "<p>By using Dealsweb.com, the mobile site, or app, you are agreeing to the practices outlined in this privacy policy. Once you share your personal information with us, you will be covered by our privacy policy and must adhere to the terms mentioned here.</p>\n" +
                "<p>If you have any questions, please email us at&nbsp;<a href=\"http://www.dealsweb.com/privacy-policy.html\">contactus@dealsweb.com</a>.</p>\n" +
                "<p><strong>Information Collected by Dealsweb.com</strong><br /><br /><strong>Personally Identifiable Information:&nbsp;</strong>Dealsweb.com will collect personal information from you when you register, email us, or sign up for notifications. The information we collect may include your name, email address, or other identifying information. This information is collected only from users who interact with our site, register or sign up with an email address. You can use many features of the site without providing any information. The Dealsweb site automatically collects the user&rsquo;s domain name and IP addresses which keeps personal identity undisclosed.<br /><br /><strong>Non-Personal Non-Identifiable Information::</strong> When you use Deals Web the server may collect non-personal information including your browser type, ISP, operating system, and IP address. If you access the site from a mobile device, we may collect a device identifier or transactional information from that device. We may also receive location information from your device. If you receive an email, we will track whether the email was opened and whether or not you click on a link or offer in the email.<br /><br /><strong>How Dealsweb.com Uses Your Information:&nbsp;</strong>The information collected by Dealsweb.com is used to communicate with our customers, keep them updated with new content and for marketing purposes. This information may be shared with a third party to improve the content of the site.<br /><br /><strong>Third Party Disclosure of Your Information:&nbsp;</strong>Dealsweb.com respects your personal information and works to protect it in most situations. However, there are certain instances where, for the development of the site and the business procedure, we will disclose your information to the third parties. Below mentioned are such instances:<br /><br /><strong>Disclosure to Successors:</strong>&nbsp;If there is any successor-in-interest of Dealsweb.com, like the company which acquires it, your personal information may be disclosed to them. In the course of any business transition also, Dealsweb.com may disclose your personal information as a part of the total asset transfer process. But in all cases, you will be informed through email or a notice on Dealsweb.com, that the ownership of your personal information has been changed.<br /><br /><strong>Disclosure to Unaffiliated Third Parties:</strong>&nbsp;Dealsweb.com may decide to disclose your personal information under the following circumstances. To prevent an emergency, to protect or enforce our rights, or any third party&rsquo;s rights, or as required or permitted by law (including, without limitation, to comply with a subpoena or court order). In cases of transgressing Intellectual Property Rights, on your part or on the part of any third party Dealsweb.com may consider disclosing your personal information.<br /><br /><strong>Disclosure to Third Party Service Providers:</strong>&nbsp;In order to maintain the site and its services, Dealsweb.com may have outside contracts with several companies. Dealsweb.com cannot guarantee that those third parties will not use or disclose your personal information. Dealsweb.com strives to keep your personal information secure. Please note that whenever you are disclosing your personal information online, it can be collected by an unknown party. In these cases, Dealsweb.com cannot guarantee the security of your personal information. However, if you are in violation of any rules or terms, Dealsweb.com has the right to disclose your personal information to concerning authorities. Please also be aware that ad server companies may collect information including IP address and domain type.<br /><br />Co-Branding:&nbsp;Dealsweb.com may be co-branded with another company. Before you disclose any personal information to Dealsweb.com, we advise that you read the co-brand&rsquo;s privacy policy as well.<br /><br /><strong>Children Privacy Protection:</strong>&nbsp;You must be 18 years of age or older to use Dealsweb.com. If you think that any of your personally identifiable information is disclosed to us by your child who is less than 18 years old, please contact us so that we can delete that information immediately to secure your privacy.<br /><br /><strong>Cookies:&nbsp;</strong>Dealsweb.com does not leave cookies on your computer. However, we do provide links to third party websites that may leave cookies after you&rsquo;ve clicked on their links. These cookies are governed by the third-parties and Dealsweb.com does not take responsibility for them.<br /><br /><strong>Change Notification:</strong>&nbsp;All users will be notified of any changes made to this Privacy Policy by a notice posted on Dealsweb.com. This notice will be posted within 30 business days of the changes to take effect. Changes regarding how Personal Information is collected or used by Dealsweb.com will also be posted on the site. Registered Newsletter readers will receive email regarding all these changes. They will also be given an option to unsubscribe.<br /><br />Last Updated: July 13, 2016.</p>";
        tv_privacy_policy.setText(Html.fromHtml(privacy_policy));

        tv_year_privacy.setText("©" + Integer.toString(year) + " " + "Dealsweb.com");
       /* if (singleton.isOnline()) {
            if (Build.VERSION.SDK_INT >= 19) {
                mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            callWebview(serverUtilities.privacy_policy);

        } else {

            showDialog();

        }*/

    }

    private void callWebview(String url) {

        mWebView.getSettings().setJavaScriptEnabled(true); // enable javascript
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);


        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                Log.d("onReceivedError", "--->");
                showDialog();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("onPageStarted", "--->");
                /*progressBar.getIndeterminateDrawable().setColorFilter(0x0e843e, android.graphics.PorterDuff.Mode.MULTIPLY);*/
                progressBar.setVisibility(View.VISIBLE);

            }


            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("onPageFinished", "--->" + singleton.isOnline());
                progressBar.setVisibility(View.GONE);

            }

        });


        Log.d("dislog", "--->" + dialog.isShowing());

        dialog.cancel();
        mWebView.loadUrl(url);

    }

    private void showDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(300, 700);


        dialog.setContentView(R.layout.custom_dialog);
        Button okButton = (Button) dialog.findViewById(R.id.ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

