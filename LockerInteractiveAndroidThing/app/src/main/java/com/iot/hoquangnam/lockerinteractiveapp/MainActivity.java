package com.iot.hoquangnam.lockerinteractiveapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.Pwm;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final double PULSE_PERIOD_MS = 20;  // Frequency of 50Hz (1000/20)
    public static Gpio mLed1, mLed2;
    public static Pwm mPwm,mPwm2;
    public static boolean isClockWise = true;
    public static boolean isClockWise2 = true;
    public String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up PWM and LED
        try {
            mPwm = PeripheralManager.getInstance().openPwm("PWM0");
            mPwm2 = PeripheralManager.getInstance().openPwm("PWM1");
            if (mPwm2 == null) mPwm2.close();
            if (mPwm == null) mPwm.close();
            if (mLed1 == null) mLed1.close();
            if (mLed2 == null) mLed2.close();
            // Always set frequency and initial duty cycle before enabling PWM
            mPwm.setPwmFrequencyHz(1000 / PULSE_PERIOD_MS);
            mPwm.setPwmDutyCycle(5);
            mPwm.setEnabled(true);
            mPwm2.setPwmFrequencyHz(1000 / PULSE_PERIOD_MS);
            mPwm2.setPwmDutyCycle(5);
            mPwm2.setEnabled(true);

            mLed1 = PeripheralManager.getInstance().openGpio("BCM23");
            mLed1.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLed2 = PeripheralManager.getInstance().openGpio("BCM6");
            mLed2.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
        }
        catch (IOException e) {
        Log.e(TAG, "Error on PeripheralIO API", e);
        }

        getSupportActionBar().setTitle("Tủ khóa A558");
        Button BtnActivityParcel = (Button) findViewById(R.id.send_package_btn);
        BtnActivityParcel.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this, SendParcelActivity.class);
                startActivity(myIntent);
            }
        });
        Button btnPickup = (Button) findViewById(R.id.get_package_btn);
        btnPickup.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this, PickupActivity.class);
                startActivity(myIntent);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            mPwm.close();
            mPwm2.close();
            mLed1.close();
            mLed2.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            mPwm = null;
            mPwm2 = null;
            mLed1 = null;
            mLed2 = null;
        }
    }
}
