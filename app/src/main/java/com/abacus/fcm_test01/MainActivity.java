package com.abacus.fcm_test01;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner sp_timeInterval;
    Button bt_saveSchedule;
    Context context;
    PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        permissionManager = new PermissionManager();

        initView();
        setSpinner();
        setFCM();
    }

    public void initView() {
        sp_timeInterval = findViewById(R.id.sp_timeInterval);
        bt_saveSchedule = findViewById(R.id.bt_saveSchedule);
    }

    public void setSpinner() {
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(context, R.array.timeInterval_array, android.R.layout.simple_spinner_dropdown_item);
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_timeInterval.setAdapter(arrayAdapter);

        sp_timeInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /** FCM **/
    public void setFCM(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }else {
                            Log.e("123213123", task.getResult());
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 권한 요청 결과 처리 메서드
     **/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionManager.PERMISSION_REQUEST_CODE_POST_NOTIFICATIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 허용되었을 때 실행할 코드
                } else {
                    // 권한이 거부되었을 때 실행할 코드
                    Toast.makeText(this, "알림 권한이 거부 되어있으면 서비스 동작 상태를 확인하실 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_saveSchedule:
                if (permissionManager.permissionCheck(context, Manifest.permission.POST_NOTIFICATIONS)){
                    // 권한 허용 되어있으면 FCM 실행
                }else {
                    // 권한 허용 안되어있으면 권한 요청

                }
                break;
        }
    }
}