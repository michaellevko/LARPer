package com.example.user.larper;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TAG", "MainActivity");
        String userName = this.getIntent().getExtras().getString(getApplicationContext()
                .getResources().getString(R.string.account_display_name));

        ((TextView)this.findViewById(R.id.loginSuccessful)).setText(userName);

        /* Check Firebase for user profile
         * If profile doesnt exist, call InitProfileFragment
         * Else, pull user profile and call HomeTabFragment*/
    }
}
