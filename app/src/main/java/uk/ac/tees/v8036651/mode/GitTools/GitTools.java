package uk.ac.tees.v8036651.mode.GitTools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import uk.ac.tees.v8036651.mode.R;

public class GitTools {

    public static void requestAuthentication(Context context, Runnable postCredentialsSet){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View dialogue = LayoutInflater.from(context).inflate(R.layout.dialog_git_authentication, null);
        builder.setView(dialogue);

        final EditText username = dialogue.findViewById(R.id.git_authentication_username);
        final EditText password = dialogue.findViewById(R.id.git_authentication_password);

        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                CredentialsProvider.setDefault(new UsernamePasswordCredentialsProvider(username.getText().toString(), password.getText().toString()));
                postCredentialsSet.run();
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    public static boolean hasInternet(Context context){
        return isWiFiConnected(context) || isMobileDataConnected(context);
    }

    public static boolean isWiFiConnected(Context context){
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        for(Network network : conn.getAllNetworks()){
            NetworkInfo networkInfo = conn.getNetworkInfo(network);
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                if(networkInfo.isConnected()){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isMobileDataConnected(Context context){
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        for(Network network : conn.getAllNetworks()){
            NetworkInfo networkInfo = conn.getNetworkInfo(network);
            if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                if(networkInfo.isConnected()){
                    return true;
                }
            }
        }
        return false;
    }

}