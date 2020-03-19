package dev.htm.ncovid.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper extends BroadcastReceiver {
    private static AgiNetworkHelperReceiverListener connectivityReceiverListener;
    private static Context context;

    public static void setContext(Context context) {
        NetworkHelper.context = context;
    }

    public static void setReceiver(AgiNetworkHelperReceiverListener receiver) {
        NetworkHelper.connectivityReceiverListener = receiver;
    }

    public static boolean CheckNetwork() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public interface AgiNetworkHelperReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
