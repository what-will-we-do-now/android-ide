package uk.ac.tees.v8036651.mode;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
class FingerprintHandler extends FingerprintManager.AuthenticationCallback
{
    private Context context;
    public FingerprintHandler(Context context)
    {
        this.context = context;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject)
    {
        CancellationSignal camcellationSignal = new CancellationSignal();

        fingerprintManager.authenticate(cryptoObject, camcellationSignal, 0,this,null);
    }

    @Override
    public void onAuthenticationError (int errorCode, CharSequence errString)
    {
        this.update("There was an Authentication Error." + errString, false);
    }

    @Override
    public void onAuthenticationFailed()
    {
        this.update("Authentication Failed", false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString)
    {
        this.update("Error " + helpString, false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result)
    {
        this.update("You can now access the app.", true);

        System.out.println("Hits here");

        Intent intent = new Intent(null, Screen_Home.class);
        context.startActivity(intent);
    }

    private void update(String s, boolean b)
    {
        System.out.println("Works Time 27:30");
    }
}
