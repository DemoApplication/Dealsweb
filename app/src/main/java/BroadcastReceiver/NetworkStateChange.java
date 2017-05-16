package BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import projo.Singleton;


/**
 * Created by user1 on 10/26/2016.
 */

public class NetworkStateChange extends BroadcastReceiver {

    static Singleton singleton;

    private static boolean firstConnect = true;
    static final String DISPLAY_MESSAGE_ACTION = "DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

    @Override
    public void onReceive(Context context, Intent intent) {
        singleton = singleton.getInstance();


        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();


        if (isConnected)
        {
            singleton.setOnline(true);
            firstConnect = true;

            /*displayMessage(context,singleton.getPresentTabStatus()+"");*/
        }
        else {
            if (firstConnect)
            {

                singleton.setOnline(false);
                firstConnect = false;

            }
        }


    }
  /*  static void displayMessage(Context context, String message)
    {

        singleton.setRefreshPage(true);
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }*/
}

