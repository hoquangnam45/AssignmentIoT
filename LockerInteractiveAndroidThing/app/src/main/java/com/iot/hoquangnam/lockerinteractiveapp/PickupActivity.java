package com.iot.hoquangnam.lockerinteractiveapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class PickupActivity extends AppCompatActivity {
    private EditText OTPInput;
    private EditText ReceiverIDInput;
    private StringBuilder buffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        getSupportActionBar().setTitle("Nhận gói hàng");
        OTPInput = (EditText) findViewById(R.id.OTPCodeInput);
        ReceiverIDInput = (EditText) findViewById(R.id.IDInput);
        ReceiverIDInput.setHint("Xin vui lòng nhập vào ID của bạn");
        Button BtnActivityParcel = (Button) findViewById(R.id.send_OTP_request);
        BtnActivityParcel.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                final String sOTPInput = OTPInput.getText().toString();
                final String sShipperIDInput = ReceiverIDInput.getText().toString();
                try {
                    String reponse = new PostJSONRequest(PickupActivity.this).execute(
                            "http://assignmentiot.atwebpages.com/myworkspace/IOT/web/open.php",
                            "OTP",
                            sOTPInput,
                            "shipperId",
                            sShipperIDInput,
                            "dresserId",
                            "A5558"
                    ).get();
                    JSONObject response = new JSONObject(reponse);
                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    final AlertDialog.Builder builder = new AlertDialog.Builder(PickupActivity.this);
                    if (response.get("code").equals("1")) {
                        builder.setTitle("Thành công");
                        final JSONArray drawers = response.getJSONArray("drawers");
                        buffer = new StringBuilder();
                        for (int i = 0; i < drawers.length(); i++) {
                            buffer.append(drawers.get(i).toString());
                            if (i != drawers.length() - 1) buffer.append(", ");
                            //Open dresser
                            int drawer_id = drawers.getInt(i);
                            Runnable PWM = new PWMHandler(drawer_id, true);
                            new Thread(PWM).start();
                            Runnable LED = new LEDBlinkingHandler(drawer_id);
                            new Thread(LED).start();
                        }

                        builder.setMessage(
                                "Tủ khóa " + buffer + " của bạn sẵn sàng!\n" +
                                        "Xin hãy xác nhận đã đặt hàng vào tủ khóa?"
                        );

                        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    //Toast.makeText(SendParcelActivity.this, sOTPInput.toString(), Toast.LENGTH_SHORT).show();
                                    String reponse = new PostJSONRequest(PickupActivity.this).execute(
                                            "http://assignmentiot.atwebpages.com/myworkspace/IOT/web/user-confirm.php",
                                            "OTP",
                                            sOTPInput,
                                            "shipperId",
                                            sShipperIDInput,
                                            "dresserId",
                                            "A5558"
                                    ).get();
                                    JSONObject response = new JSONObject(reponse);
                                    if (response.get("code").equals("1")) {
                                        //AlertDialog alert = builder.create();
                                        //alert.setMessage("Xác nhận thành công!!\n Chúc bạn một ngày vui vẻ");
                                        for (int i = 0; i < drawers.length(); i++) {
                                            int drawer_id = drawers.getInt(i);
                                            Runnable PWM = new PWMHandler(drawer_id, false);
                                            new Thread(PWM).start();
                                            Runnable LED = new LEDBlinkingHandler(drawer_id);
                                            new Thread(LED).start();
                                        }
                                    }                                    //Log.d(this.getClass().toString(), reponse);
                                } catch (ExecutionException | InterruptedException | JSONException e) {
                                    e.printStackTrace();
                                }
                                // Get back to main activity
                                Intent myIntent = new Intent(PickupActivity.this, MainActivity.class);
                                startActivity(myIntent);
                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                // Get back to main activity
                                Intent myIntent = new Intent(PickupActivity.this, MainActivity.class);
                                startActivity(myIntent);
                                dialog.dismiss();
                            }
                        });
                    } else {
                        builder.setTitle("Thất bại");
                        builder.setMessage("Xin vui lòng kiểm tra lại thông tin nhập vào");
                        builder.setPositiveButton("Thử lại", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Get back to main activity
                                Intent myIntent = new Intent(PickupActivity.this, MainActivity.class);
                                startActivity(myIntent);
                                dialog.dismiss();
                            }
                        });
                    }

                    // 2. Chain together various setter methods to set the dialog characteristics

                    AlertDialog alert = builder.create();
                    alert.show();

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                } catch (InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
