package com.example.loveme;

import android.Manifest;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String MESSAGE = "Love Me!!";
    // this is for handling the event where your permission is granted
    public static final int SEND_SMS_REQ = 0;

    public String phoneNumber = null;
    public SharedPreferences mPrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = getSharedPreferences("label", 0);
        phoneNumber = mPrefs.getString("phoneNumber", "");

        TextView numberField = findViewById(R.id.editText);
        numberField.setText(phoneNumber);
    }

    public void getLove(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // request permission to us sms
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_REQ);
        } else {

            EditText editText = findViewById(R.id.editText);
            phoneNumber = editText.getText().toString();

            if (phoneNumber == null || phoneNumber.trim().equalsIgnoreCase("")) {
                // TODO handle bad number
            } else{
                // store new value of phone number
                mPrefs.edit().putString("phoneNumber", phoneNumber).commit();

                // Set the service center address if needed, otherwise null.
                String scAddress = null;
                // Set pending intents to broadcast
                // when message sent and when delivered, or set to null.
                PendingIntent sentIntent = null, deliveryIntent = null;

                // Use SmsManager.
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage
                        (phoneNumber, scAddress, MESSAGE,
                                sentIntent, deliveryIntent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_REQ: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    getLove(null);
                } else {
                    // permission denied, boo!
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
