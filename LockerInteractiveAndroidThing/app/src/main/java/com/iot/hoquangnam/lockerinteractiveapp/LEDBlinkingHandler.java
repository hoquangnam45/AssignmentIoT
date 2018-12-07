package com.iot.hoquangnam.lockerinteractiveapp;
import android.util.Log;

import java.io.IOException;

public class LEDBlinkingHandler implements Runnable{
    private int num;
    private String TAG="LEDBLINKING";
    public LEDBlinkingHandler(int _num){
        this.num = _num;
    }

    public void run(){
        if(this.num == 1){
            if (MainActivity.mLed1 == null) {
                return;
            }
            try {
                boolean mLedState = true;
                for (int i = 0; i < 10; i++){
                    MainActivity.mLed1.setValue(mLedState);
                    mLedState = !mLedState;
                    Thread.sleep(500);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            if (MainActivity.mLed2 == null) {
                return;
            }
            try {
                boolean mLedState = true;
                for (int i = 0; i < 10; i++){
                    MainActivity.mLed2.setValue(mLedState);
                    mLedState = !mLedState;
                    Thread.sleep(500);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
