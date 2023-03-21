package com.abacus.fcm_test01;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class PermissionManager {

    public static final int PERMISSION_REQUEST_CODE_POST_NOTIFICATIONS = 102;

    /**
     * 권한 확인
     **/
    // 기기 SDK 확인을 여기서 안하기 때문에 사용하기전에 SDK 확인 필요하다.
    public boolean permissionCheck(Context context, String strPermission) {
        // 권한이 허용되어있다면 true를 반환, 거부되어있으면 false 반환
        try {
            return ActivityCompat.checkSelfPermission(context, strPermission) == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 권한 요청
     **/
    // 기기 SDK 확인을 여기서 안하기 때문에 사용하기전에 SDK 확인 필요하다.
    public void requestPermission(Context context, String[] strPermissions) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Log.e("checkBlePermission", "requestPermission_TIRAMISU");
                ActivityCompat.requestPermissions((Activity) context, strPermissions, PERMISSION_REQUEST_CODE_POST_NOTIFICATIONS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public boolean checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.e("checkNotificationPermission", "TIRAMISU");
            if (!(permissionManager.permissionCheck(this, android.Manifest.permission.POST_NOTIFICATIONS))) {

                Log.e("checkNotificationPermission", "TIRAMISU_false");
                permissionManager.requestPermission(context, new String[]{
                        android.Manifest.permission.POST_NOTIFICATIONS
                });
                return false;
            } else {
                Log.e("checkNotificationPermission", "TIRAMISU_true");
                return true;
            }
        } else {
            return true;
        }
    }*/
}
