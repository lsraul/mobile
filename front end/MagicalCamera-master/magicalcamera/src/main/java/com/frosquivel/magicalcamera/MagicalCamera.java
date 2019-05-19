package com.frosquivel.magicalcamera;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;

import com.frosquivel.magicalcamera.Objects.ActionPictureObject;
import com.frosquivel.magicalcamera.Objects.FaceRecognitionObject;
import com.frosquivel.magicalcamera.Objects.MagicalCameraObject;
import com.frosquivel.magicalcamera.Objects.PrivateInformationObject;
import com.frosquivel.magicalcamera.Utilities.PictureUtils;

import static android.graphics.Color.RED;

/**
 * Created by          Fabián Rosales Esquivel
 * Created Date        on 5/15/16
 * This is an android library to take easy picture,
 * This have the objects or anothers classes to toast the best form to use this library
 * I recommended use only this class in your code for take a best experience of this third party library
 * You have the possibility of write me in my personal email fabian7593@gmail.com
 * v2
 */
public class MagicalCamera {

    //compress format public static variables
    public static final Bitmap.CompressFormat JPEG = Bitmap.CompressFormat.JPEG;
    public static final Bitmap.CompressFormat PNG = Bitmap.CompressFormat.PNG;
    public static final Bitmap.CompressFormat WEBP = Bitmap.CompressFormat.WEBP;

    //the orientation rotates
    public static final int ORIENTATION_ROTATE_NORMAL = 0;
    public static final int ORIENTATION_ROTATE_90 = 90;
    public static final int ORIENTATION_ROTATE_180 = 180;
    public static final int ORIENTATION_ROTATE_270 = 270;

    //other constants for realized tasks
    public static final int TAKE_PHOTO = 0;
    public static final int SELECT_PHOTO = 1;
    public static final int LANDSCAPE_CAMERA = 2;
    public static final int NORMAL_CAMERA = 3;

    //Constants for permissions
    public static final String CAMERA = "android.permission.CAMERA";
    public static final String EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String ACCESS_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";

    private MagicalCameraObject magicalCameraObject;
    private MagicalPermissions magicalPermissions;

    //================================================================================
    // Constructs
    //================================================================================
    //region Construct
    public MagicalCamera(Activity activity, int resizePhoto, MagicalPermissions magicalPermissions) {
        resizePhoto = resizePhoto <= 0 ? ActionPictureObject.BEST_QUALITY_PHOTO : resizePhoto;
        magicalCameraObject = new MagicalCameraObject(activity, resizePhoto);
        this.magicalPermissions = magicalPermissions;
    }
    //endregion


    //Principal Methods
    public void takePhoto(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                magicalCameraObject.getActionPicture().takePhoto();
            }
        };
        askPermissions(runnable, CAMERA);
    }

    public void selectedPicture(final String headerPopUpName){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                magicalCameraObject.getActionPicture().selectedPicture(headerPopUpName);
            }
        };
        askPermissions(runnable, EXTERNAL_STORAGE);
    }

    public void takeFragmentPhoto(final Fragment fragment) {
        if (magicalCameraObject.getActionPicture().takeFragmentPhoto()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    fragment.startActivityForResult(getIntentFragment(), MagicalCamera.TAKE_PHOTO);
                }
            };
            askPermissions(runnable, CAMERA);
        }
    }

    public void selectFragmentPicture(final Fragment fragment, final String header){
        if (magicalCameraObject.getActionPicture().selectedFragmentPicture()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    fragment.startActivityForResult(
                            Intent.createChooser(getIntentFragment(), header),
                            MagicalCamera.SELECT_PHOTO);
                }
            };
            askPermissions(runnable, EXTERNAL_STORAGE);
        }
    }

    public void takeFragmentPhoto(final android.support.v4.app.Fragment fragment) {
        if (magicalCameraObject.getActionPicture().takeFragmentPhoto()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    fragment.startActivityForResult(getIntentFragment(), MagicalCamera.TAKE_PHOTO);
                }
            };
            askPermissions(runnable, CAMERA);
        }
    }

    public void selectFragmentPicture(final android.support.v4.app.Fragment fragment, final String header) {
        if (magicalCameraObject.getActionPicture().selectedFragmentPicture()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    fragment.startActivityForResult(
                            Intent.createChooser(getIntentFragment(), header),
                            MagicalCamera.SELECT_PHOTO);
                }
            };
            askPermissions(runnable, EXTERNAL_STORAGE);
        }
    }

    private void askPermissions(final Runnable runnable){
        magicalPermissions.askPermissions(runnable);
    }

    private void askPermissions(final Runnable runnable, final String operationType){
        magicalPermissions.askPermissions(runnable, operationType);
    }

    //Face detector methods
    public Bitmap faceDetector(int stroke, int color){
        return  magicalCameraObject.getFaceRecognition().faceDetector(stroke, color,
                magicalCameraObject.getActivity(), magicalCameraObject.getActionPicture().getActionPictureObject().getMyPhoto());
    }

    public Bitmap faceDetector(){
        return magicalCameraObject.getFaceRecognition().faceDetector(5, RED,
                magicalCameraObject.getActivity(), magicalCameraObject.getActionPicture().getActionPictureObject().getMyPhoto());
    }

    public FaceRecognitionObject getFaceRecognitionInformation(){
        return magicalCameraObject.getFaceRecognition().getFaceRecognitionInformation();
    }

    //Image information methods
    public boolean initImageInformation() {
        return magicalCameraObject.getPrivateInformation().getImageInformation(magicalCameraObject.getUriPaths().getUriPathsObject().getRealPath());
    }

    public PrivateInformationObject getPrivateInformation() {
        return magicalCameraObject.getPrivateInformation().getPrivateInformationObject();
    }

    /**
     * ***********************************************
     * This methods save the photo in memory device
     * with diferents params
     * **********************************************
     */
    public String savePhotoInMemoryDevice(Bitmap bitmap, String photoName, boolean autoIncrementNameByDate) {
        return magicalCameraObject.getSaveEasyPhoto().writePhotoFile(bitmap,
                photoName, "MAGICAL CAMERA", PNG, autoIncrementNameByDate, magicalCameraObject.getActivity());
    }

    public String savePhotoInMemoryDevice(Bitmap bitmap, String photoName, Bitmap.CompressFormat format, boolean autoIncrementNameByDate) {
        return magicalCameraObject.getSaveEasyPhoto().writePhotoFile(bitmap,
                photoName, "MAGICAL CAMERA", format, autoIncrementNameByDate, magicalCameraObject.getActivity());
    }

    public String savePhotoInMemoryDevice(Bitmap bitmap, String photoName, String directoryName, boolean autoIncrementNameByDate) {
        return magicalCameraObject.getSaveEasyPhoto().writePhotoFile(bitmap,
                photoName, directoryName, PNG, autoIncrementNameByDate, magicalCameraObject.getActivity());
    }

    public String savePhotoInMemoryDevice(Bitmap bitmap, String photoName, String directoryName,
                                           Bitmap.CompressFormat format, boolean autoIncrementNameByDate) {
        return magicalCameraObject.getSaveEasyPhoto().writePhotoFile(bitmap,
                photoName, directoryName, format, autoIncrementNameByDate, magicalCameraObject.getActivity());
    }

    //get variables
    public String getRealPath(){
        return magicalCameraObject.getUriPaths().getUriPathsObject().getRealPath();
    }

    public Bitmap getPhoto(){
        return magicalCameraObject.getActionPicture().getActionPictureObject().getMyPhoto();
    }

    public void setPhoto(Bitmap bitmap){
        magicalCameraObject.getActionPicture().getActionPictureObject().setMyPhoto(bitmap);
    }

    public void resultPhoto(int requestCode, int resultCode, Intent data){
        magicalCameraObject.getActionPicture().resultPhoto(requestCode, resultCode, data);
    }

    public void resultPhoto(int requestCode, int resultCode, Intent data, int rotateType){
        magicalCameraObject.getActionPicture().resultPhoto(requestCode, resultCode, data, rotateType);
    }

    public Intent getIntentFragment(){
        return magicalCameraObject.getActionPicture().getActionPictureObject().getIntentFragment();
    }
    public void setResizePhoto(int resize){
         magicalCameraObject.getActionPicture().getActionPictureObject().setResizePhoto(resize);
    }


    //methods to rotate picture
    public Bitmap rotatePicture(int rotateType){
        if(getPhoto() != null)
            return PictureUtils.rotateImage(getPhoto(), rotateType);
        else
            return null;
    }

    public Bitmap rotatePicture(Bitmap picture,int rotateType){
        if(picture != null)
            return PictureUtils.rotateImage(picture, rotateType);
        else
            return null;
    }

    //endregion
}
