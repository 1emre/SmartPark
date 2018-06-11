package com.emredmrbs99.smartpark;

import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,1);
        }else if(mBluetoothAdapter.isEnabled()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
            },350);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Handler h=new Handler();
        if(requestCode == 1){

            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
            },350);

        }else{finish();}
    }
}
