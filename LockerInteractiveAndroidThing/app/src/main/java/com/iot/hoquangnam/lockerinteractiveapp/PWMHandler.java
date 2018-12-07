package com.iot.hoquangnam.lockerinteractiveapp;
import android.util.Log;

import java.io.IOException;

public class PWMHandler implements Runnable {
    private int num;
    private boolean isOpen;
    private String TAG="PMWHANDLER";
    public PWMHandler(int _num, boolean _isOpen) {
        this.num = _num;this.isOpen = _isOpen;
    }

    public void run() {
        if (this.num == 1){
            if (MainActivity.mPwm == null) {
                Log.w(TAG, "Stopping thread since mPwm is null");
                return;
            }
            if (isOpen){
                try {

                    MainActivity.mPwm.setPwmDutyCycle(11);
                } catch (IOException e) {
                    Log.e(TAG, "Error on PeripheralIO API mPwm", e);
                }
            } else {
                try {
                    MainActivity.mPwm.setPwmDutyCycle(5);
                } catch (IOException e) {
                    Log.e(TAG, "Error on PeripheralIO API mPwm", e);
                }
            }
        }
        else {
            if (this.num == 2){
                if (MainActivity.mPwm2 == null) {
                    Log.w(TAG, "Stopping thread since mPwm2 is null");
                    return;
                }
                if (isOpen){
                    try {
                        MainActivity.mPwm2.setPwmDutyCycle(5);
                    } catch (IOException e) {
                        Log.e(TAG, "Error on PeripheralIO API mPwm2", e);
                    }
                } else {
                    try {
                        MainActivity.mPwm2.setPwmDutyCycle(11);
                    } catch (IOException e) {
                        Log.e(TAG, "Error on PeripheralIO API mPwm2", e);
                    }
                }
            }
        }
    }
}
