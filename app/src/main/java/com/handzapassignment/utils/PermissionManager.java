package com.handzapassignment.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.handzapassignment.R;

/**
 * Created by satyendra on 8/30/17.
 */

public class PermissionManager {

    public class Constant{
        public static final int LOCATION_ACCESS_RQ = 73;
        public static final int READ_STORAGE_ACCESS_RQ = 75;
        public static final int WRITE_STORAGE_ACCESS_RQ = 76;
        public static final int MAKE_CALL_RQ = 77;
        public static final int READ_PHONE_STATE_RQ = 78;
    }

    public interface CustomPermissionDialogCallback{
        void onCancelClick();
        void onOpenSettingClick();
    }

    /**
     * check location access permission
     */
    public static boolean  checkAccessFineLocationPermission(final Activity activity, int reqcode){

        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, reqcode);
            return false;
        }
        return true;
    }

    /**
     * check external phone call permission
     */
    public static boolean checkPhoneCallPermission(Activity activity, int reqcode){

        if(ContextCompat.checkSelfPermission(activity,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, reqcode);
            return false;
        }
        return true;
    }

    /**
     * check write external storage permission
     */
    public static boolean checkWriteStoragePermission(Activity activity, int reqcode){

        if(ContextCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, reqcode);
            return false;
        }
        return true;
    }

    /**
     * check phone state permission
     */
    public static boolean checkReadPhoneStatePermission(Activity activity, int reqcode){

        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, reqcode);
            return false;
        }
        return true;
    }

    /**
     * check phone state permission and provide callback in fragment
     */
    public static boolean checkReadPhoneStatePermissionInFragment(Activity activity, Fragment fragment, int reqcode){

        if(ContextCompat.checkSelfPermission(activity,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            fragment.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, reqcode);
            return false;
        }
        return true;
    }

    /**
     * check read external storage permission
     */
    public static boolean checkReadStoragePermission(Activity activity, int reqcode){

        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, reqcode);
            return false;
        }
        return true;
    }

    /**
     * check read storage permission and provide callback in fragment
     */
    public static boolean checkReadStoragePermissionInFragment(Activity activity, Fragment fragment, int reqcode){

        if(ContextCompat.checkSelfPermission(activity,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, reqcode);
            return false;
        }
        return true;
    }

    /**
     * check write storage permission and provide callback in fragment
     */
    public static boolean checkWriteStoragePermissionInFragment(Activity activity, Fragment fragment, int reqcode){

        if(ContextCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, reqcode);
            return false;
        }
        return true;
    }

    /**
     * check location access permission
     */
    public static boolean checkAccessFineLocationPermissionInFragment(final Activity activity, Fragment fragment, int reqcode){

        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            fragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, reqcode);
            return false;
        }
        return true;
    }
}
