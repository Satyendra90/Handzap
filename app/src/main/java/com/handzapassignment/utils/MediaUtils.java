package com.handzapassignment.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.handzapassignment.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaUtils {

    public enum MediaType {
        IMAGE, VIDEO, DOCUMENT
    }

    public enum RequestCode {

        CAPTURE_IMAGE(1), CAPTURE_VIDEO(2), SELECT_IMAGE_VIDEO(3), SELECT_DOCUMENT(4);

        private int value;

        RequestCode(int v) {
            value = v;
        }

        public int getValue() {
            return value;
        }
    }

    public static File file;

    public static void captureImage(Activity activity, RequestCode requestCode) {

        Intent cameraInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraInt.resolveActivity(activity.getPackageManager()) != null) {
            file = null;
            try {
                file = MediaPathUtils.createImageFile(activity);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                cameraInt.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(cameraInt, requestCode.getValue());
            }
        }
    }

    public static void captureVideo(Activity activity, RequestCode requestCode) {
        Intent cameraInt = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (cameraInt.resolveActivity(activity.getPackageManager()) != null) {
            file = null;
            try {
                file = MediaPathUtils.createVideoFile(activity);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                cameraInt.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(cameraInt, requestCode.getValue());
            }
        }
    }

    public static void selectDocument(Activity activity, RequestCode requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        activity.startActivityForResult(intent, requestCode.getValue());
    }

    public static void selectImage(Activity activity, RequestCode requestCode) {
        Intent openGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(openGallery, requestCode.getValue());
    }

    public static void selectImageVideo(Activity activity, RequestCode requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
        activity.startActivityForResult(intent, requestCode.getValue());
    }

    public static void onActivityResult(Context context, int requestCode, int resultCode, Intent data, OnMediaPicked onMediaPicked) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestCode.CAPTURE_IMAGE.getValue()) {
                onMediaPicked.onSuccess(file, null, MediaType.IMAGE);
            } else if (requestCode == RequestCode.CAPTURE_VIDEO.getValue()) {
                onMediaPicked.onSuccess(file, null, MediaType.VIDEO);
            } else if (requestCode == RequestCode.SELECT_IMAGE_VIDEO.getValue()) {
                Uri selectedUri = data.getData();
                MediaType mediaType = checkIsImage(context, selectedUri) ?
                        MediaType.IMAGE : MediaType.VIDEO;
                File file = new File(mediaType == MediaType.IMAGE ? MediaPathUtils.getGalleryImagePath(context, selectedUri) :
                        MediaPathUtils.getGalleryVideoPath(context, selectedUri));
                onMediaPicked.onSuccess(file, null, mediaType);
            } else if (requestCode == RequestCode.SELECT_DOCUMENT.getValue()) {
                Uri uri = data.getData();
                onMediaPicked.onSuccess(MediaPathUtils.getRealPathFromURI(context, uri), null, MediaType.DOCUMENT);
            }
        }
    }

    public static boolean checkIsImage(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        String type = contentResolver.getType(uri);
        if (type != null) {
            return type.startsWith("image/");
        }
        return false;
    }

    public static abstract class OnMediaPicked {

        public abstract void onSuccess(File file, Bitmap bm, MediaUtils.MediaType mediaType);

        public void onError() {
        }

    }
}
