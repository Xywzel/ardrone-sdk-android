package com.parrot.freeflight.activities;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.parrot.freeflight.R;

public class FlightPathSelectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_path_select);
    }

    public void flightPathSelectOnClick(View v){
        super.onBackPressed();
        Log.d("asd","hurdur");
    }

}
