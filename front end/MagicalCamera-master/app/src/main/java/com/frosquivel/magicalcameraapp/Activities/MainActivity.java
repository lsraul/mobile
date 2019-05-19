package com.frosquivel.magicalcameraapp.Activities;

/*
    Copyright 2015 Dmytro Tarianyk

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

    /*Copyright 2015 Rey Pham.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
     */

    /*
    Copyright 2015 jack wang

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
     */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.frosquivel.magicalcameraapp.Activities.HireUs.HireUsActivity;
import com.frosquivel.magicalcameraapp.Activities.Menu.AboutUsActivity;
import com.frosquivel.magicalcameraapp.Activities.Menu.HelpUsActivity;
import com.frosquivel.magicalcameraapp.Fragments.ActivityForFragment;
import com.frosquivel.magicalcameraapp.R;
import com.frosquivel.magicalcameraapp.Utils.Utils;
import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.frosquivel.magicalcamera.Utilities.ConvertSimpleImage;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.vision.face.Landmark;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

import java.util.List;
import java.util.Map;

/**
 * Created by          Fabián Rosales Esquivel
 * Created Date        on 5/19/16
 * Modified Date       on 16/03/2017
 * This is an android library to take easy picture
 * Search the Utils class in the same package for view the code of this and your meaning
 * https://github.com/Cleveroad/SlidingTutorial-Android/blob/master/sample/src/main/java/com/cleveroad/slidingtutorial/sample/MainActivity.java
 */
public class MainActivity extends AppCompatActivity {
    //this is the image view for show your picture taken,
    // if you click in this show the picture in all screen
    private ImageView imageView;
    //button to take picture
    private ImageButton btntakephoto;
    //button to select picture of your device
    private ImageButton btnselectedphoto;
    //button for save the photo in device
    private ImageButton saveImage;
    //button for go to fragment
    private Button btnGoTo;
    private TextView texttitle;

    private FloatingActionMenu floatingBtnMenu;

    private FrameLayout frame;
    private View principalLayout;

    //button for realized the facial recognition of your picture
    private FloatingActionButton floatingBtnFacialRecognition;
    //see the information data of the picture
    private FloatingActionButton floatingBtnPhotoInformation;
    //button for rotate the image in bitmap and in image view
    private FloatingActionButton floatingBtnRotate;
    //button for view the string base 64 trasnform the bitmap
    private FloatingActionButton floatingBtnSeeString64;

    //Ever you need to call magical camera and permissionGranted
    private MagicalCamera magicalCamera;
    private MagicalPermissions magicalPermissions;

    //the variable of progress dialog loading
    private LinearLayout progressLoadingIndicator;
    Map<String, Boolean> mapPermissions = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        setUIComponents();

        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        //realized the instance of magical camera, this need the context, this need the context,
        //the percentage of quality photo and the permission granted
        magicalPermissions = new MagicalPermissions(this, permissions);
        magicalCamera = new MagicalCamera(this, Integer.parseInt(Utils.getSharedPreference(this, Utils.C_PREFERENCE_MC_QUALITY_PICTURE)), magicalPermissions);

        btntakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isActivePermission(MagicalCamera.CAMERA)) {
                    //call the method of take the picture
                    magicalCamera.takePhoto();
                } else {
                    Utils.viewSnackBar(getString(R.string.error_string_base_64), principalLayout);
                }
            }
        });

        btnselectedphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActivePermission(MagicalCamera.EXTERNAL_STORAGE)) {
                    //call the method of selected picture
                    magicalCamera.selectedPicture(Utils.getSharedPreference(MainActivity.this, Utils.C_PREFERENCE_MC_SELECTED_PICTURE));
                } else {
                    Utils.viewSnackBar(getString(R.string.error_not_have_permissions), principalLayout);
                }
            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.validateMagicalCameraNull(MainActivity.this, principalLayout, magicalCamera)) {
                    //save the photo in your memory external or internal of your device
                    //save the picture inmemory device, and return the physical path of this photo

                    new AsyncTask<Void, Void, String>() {
                        protected void onPreExecute() {
                            progressLoadingIndicator.setVisibility(View.VISIBLE);
                        }

                        protected String doInBackground(Void... params) {

                            String path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(),
                                    Utils.getSharedPreference(MainActivity.this, Utils.C_PREFERENCE_MC_PHOTO_NAME),
                                    Utils.getSharedPreference(MainActivity.this, Utils.C_PREFERENCE_MC_DIRECTORY_NAME),
                                    Utils.getFormat(MainActivity.this),
                                    Boolean.parseBoolean(Utils.getSharedPreference(MainActivity.this, Utils.C_PREFERENCE_MC_AUTO_IC_NAME)));

                            if (path != null) {
                                Utils.viewSnackBar(getString(R.string.message_save_manual) + path, principalLayout);
                            } else {
                                Utils.viewSnackBar(getString(R.string.error_dont_save_photo), principalLayout);
                            }

                            return null;
                        }

                        protected void onPostExecute(String msg) {
                            progressLoadingIndicator.setVisibility(View.GONE);
                        }
                    }.execute();

                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.validateMagicalCameraNull(MainActivity.this, principalLayout, magicalCamera)) {
                    //The static method of bitmap is usage because if you pass string base 64 or
                    //array bytes like intent parameter bundle, the app have an error :(
                    //This error is E/JavaBinder﹕ !!! FAILED BINDER TRANSACTION !!!
                    //please check this link
                    //http://stackoverflow.com/questions/31708092/setting-a-bitmap-as-intent-extra-causes-error

                    new AsyncTask<Void, Void, String>() {
                        protected void onPreExecute() {
                            progressLoadingIndicator.setVisibility(View.VISIBLE);
                        }

                        protected String doInBackground(Void... params) {
                            Utils.magicalCameraBitmap = magicalCamera.getPhoto();
                            Intent intent = new Intent(MainActivity.this, ImageViewActivity.class);
                            startActivity(intent);
                            return null;
                        }

                        protected void onPostExecute(String msg) {
                            progressLoadingIndicator.setVisibility(View.GONE);
                        }
                    }.execute();
                }
            }
        });

        floatingBtnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.validateMagicalCameraNull(MainActivity.this, principalLayout, magicalCamera)) {
                    new AsyncTask<Void, Void, String>() {
                        protected void onPreExecute() {
                            progressLoadingIndicator.setVisibility(View.VISIBLE);
                        }

                        protected String doInBackground(Void... params) {
                            //for once click rotate the picture in 90ª, and set in the image view.
                            magicalCamera.setPhoto(magicalCamera.rotatePicture(magicalCamera.getPhoto(), MagicalCamera.ORIENTATION_ROTATE_90));
                            //its need the ui thread, but in this case we use in on Post Execute
                            //imageView.setImageBitmap(magicalCamera.getPhoto());
                            return null;
                        }

                        protected void onPostExecute(String msg) {
                            imageView.setImageBitmap(magicalCamera.getPhoto());
                            progressLoadingIndicator.setVisibility(View.GONE);
                        }
                    }.execute();
                }
            }
        });

        floatingBtnFacialRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                if (Utils.validateMagicalCameraNull(MainActivity.this, principalLayout, magicalCamera)) {

                    new AsyncTask<Void, Void, String>() {
                        Bitmap faceDetectorBitmap = null;

                        protected void onPreExecute() {
                            progressLoadingIndicator.setVisibility(View.VISIBLE);
                        }

                        protected String doInBackground(Void... params) {
                            int hexToIntColor =
                                    Color.parseColor(Utils.getSharedPreference(MainActivity.this, Utils.C_PREFERENCE_MC_FACIAL_RECOGNITION_COLOR));

                            faceDetectorBitmap = magicalCamera.faceDetector(
                                    Integer.parseInt(Utils.getSharedPreference(MainActivity.this, Utils.C_PREFERENCE_MC_FACIAL_RECOGNITION_THICK)),
                                    hexToIntColor);

                            if (faceDetectorBitmap != null) {

                                magicalCamera.setPhoto(faceDetectorBitmap);
                                List<Landmark> listMark = magicalCamera.getFaceRecognitionInformation().getListLandMarkPhoto();
                            } else {
                                Utils.viewSnackBar(getString(R.string.error_not_detect_face), principalLayout);
                            }

                            return null;
                        }

                        protected void onPostExecute(String msg) {
                            imageView.setImageBitmap(faceDetectorBitmap);
                            progressLoadingIndicator.setVisibility(View.GONE);
                        }
                    }.execute();
                }
            }
        });

        floatingBtnSeeString64.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.validateMagicalCameraNull(MainActivity.this, principalLayout, magicalCamera)) {

                    new AsyncTask<Void, Void, String>() {
                        protected void onPreExecute() {
                            progressLoadingIndicator.setVisibility(View.VISIBLE);
                        }

                        protected String doInBackground(Void... params) {
                            String base64 = "";
                            try {
                                //convert the bitmap to bytes array
                                byte[] bytesArray = ConvertSimpleImage.bitmapToBytes(magicalCamera.getPhoto(), Utils.getFormat(MainActivity.this));
                                //convert the bytes to string 64, with this form is easly to send by web service or store data in DB
                                String imageBase64 = ConvertSimpleImage.bytesToStringBase64(bytesArray);

                                /*****************************************************************
                                 *                    Revert the process
                                 *****************************************************************/
                                //if you need to revert the process
                                //byte[] anotherArrayBytes = ConvertSimpleImage.stringBase64ToBytes(imageBase64);
                                //again deserialize the image
                                //Bitmap myImageAgain = ConvertSimpleImage.bytesToBitmap(anotherArrayBytes);

                                base64 = imageBase64.substring(0, 300);


                            } catch (Exception e) {
                                Utils.viewSnackBar(getString(R.string.error_string_base_64), principalLayout);
                            }

                            return base64;
                        }

                        protected void onPostExecute(String msg) {
                            new MaterialDialog.Builder(MainActivity.this)
                                    .title(getString(R.string.convert_to_string_base_64_title))
                                    .content(msg)
                                    .positiveText(getString(R.string.message_ok))
                                    .show();
                            progressLoadingIndicator.setVisibility(View.GONE);
                        }
                    }.execute();
                }
            }
        });

        floatingBtnPhotoInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.validateMagicalCameraNull(MainActivity.this, principalLayout, magicalCamera)) {

                    new AsyncTask<Void, Void, String>() {
                        protected void onPreExecute() {
                            progressLoadingIndicator.setVisibility(View.VISIBLE);
                        }

                        protected String doInBackground(Void... params) {
                            //for see the data you need to call this method ever
                            //if the function return true you have the posibility of see the dat
                            StringBuilder builderInformation = new StringBuilder();
                            if (magicalCamera.initImageInformation()) {

                                //question in have data
                                if (Utils.notNullNotFill(magicalCamera.getPrivateInformation().getLatitude() + ""))
                                    builderInformation.append(getString(R.string.info_data_latitude) + magicalCamera.getPrivateInformation().getLatitude() + "\n");

                                if (Utils.notNullNotFill(magicalCamera.getPrivateInformation().getLatitudeReference()))
                                    builderInformation.append(getString(R.string.info_data_latitude_referene) + magicalCamera.getPrivateInformation().getLatitudeReference() + "\n");

                                if (Utils.notNullNotFill(magicalCamera.getPrivateInformation().getLongitude() + ""))
                                    builderInformation.append(getString(R.string.info_data_longitude) + magicalCamera.getPrivateInformation().getLongitude() + "\n");

                                if (Utils.notNullNotFill(magicalCamera.getPrivateInformation().getLongitudeReference()))
                                    builderInformation.append(getString(R.string.info_data_longitude_reference) + magicalCamera.getPrivateInformation().getLongitudeReference() + "\n");

                                if (Utils.notNullNotFill(magicalCamera.getPrivateInformation().getDateTimeTakePhoto()))
                                    builderInformation.append(getString(R.string.info_data_date_time_photo) + magicalCamera.getPrivateInformation().getDateTimeTakePhoto() + "\n");

                                if (Utils.notNullNotFill(magicalCamera.getPrivateInformation().getDateStamp()))
                                    builderInformation.append(getString(R.string.info_data_date_stamp_photo) + magicalCamera.getPrivateInformation().getDateStamp() + "\n");

                                if (Utils.notNullNotFill(magicalCamera.getPrivateInformation().getIso()))
                                    builderInformation.append(getString(R.string.info_data_ISO) + magicalCamera.getPrivateInformation().getIso() + "\n");

                                if (Utils.notNullNotFill(magicalCamera.getPrivateInformation().getOrientation()))
                                    builderInformation.append(getString(R.string.info_data_orientation_photo) + magicalCamera.getPrivateInformation().getOrientation() + "\n");

                                if (Utils.notNullNotFill(magicalCamera.getPrivateInformation().getImageLength()))
                                    builderInformation.append(getString(R.string.info_data_image_lenght) + magicalCamera.getPrivateInformation().getImageLength() + "\n");

                                if (Utils.notNullNotFill(magicalCamera.getPrivateInformation().getImageWidth()))
                                    builderInformation.append(getString(R.string.info_data_image_width) + magicalCamera.getPrivateInformation().getImageWidth() + "\n");

                                if (Utils.notNullNotFill(magicalCamera.getPrivateInformation().getModelDevice()))
                                    builderInformation.append(getString(R.string.info_data_model_device) + magicalCamera.getPrivateInformation().getModelDevice() + "\n");

                                if (Utils.notNullNotFill(magicalCamera.getPrivateInformation().getMakeCompany()))
                                    builderInformation.append(getString(R.string.info_data_make_company) + magicalCamera.getPrivateInformation().getMakeCompany() + "\n");
                            } else {
                                Utils.viewSnackBar(getString(R.string.error_not_have_data), principalLayout);
                            }

                            return builderInformation.toString();
                        }

                        protected void onPostExecute(String msg) {
                            new MaterialDialog.Builder(MainActivity.this)
                                    .title(getString(R.string.message_see_photo_information))
                                    .content(msg)
                                    .positiveText(getString(R.string.message_ok))
                                    .show();
                            progressLoadingIndicator.setVisibility(View.GONE);
                        }
                    }.execute();
                }
            }
        });

        btnGoTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityForFragment.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //THIS IS FOR RESUME WHEN THE USER GO BACK TO SHARED PREFERENCE ACTIVITY
        magicalCamera.setResizePhoto(Integer.parseInt(Utils.getSharedPreference(this, Utils.C_PREFERENCE_MC_QUALITY_PICTURE)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //this is for rotate picture in this method
        //magicalCamera.resultPhoto(requestCode, resultCode, data, MagicalCamera.ORIENTATION_ROTATE_180);

        if (resultCode == Activity.RESULT_OK) {
            //you should to call the method ever, for obtain the bitmap photo (= magicalCamera.getPhoto())
            magicalCamera.resultPhoto(requestCode, resultCode, data);

            if (Utils.validateMagicalCameraNull(MainActivity.this, principalLayout, magicalCamera)) {
                floatingBtnMenu.setVisibility(View.VISIBLE);
                saveImage.setVisibility(View.VISIBLE);
                //set the photo in image view
                imageView.setImageBitmap(magicalCamera.getPhoto());
                final Activity activity = this;

                new AsyncTask<Void, Void, String>() {
                    protected void onPreExecute() {
                        progressLoadingIndicator.setVisibility(View.VISIBLE);
                    }

                    protected String doInBackground(Void... params) {

                        //save the picture inmemory device, and return the physical path of this photo
                        String path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(),
                                Utils.getSharedPreference(activity, Utils.C_PREFERENCE_MC_PHOTO_NAME),
                                Utils.getSharedPreference(activity, Utils.C_PREFERENCE_MC_DIRECTORY_NAME),
                                Utils.getFormat(activity),
                                Boolean.parseBoolean(Utils.getSharedPreference(activity, Utils.C_PREFERENCE_MC_AUTO_IC_NAME)));
                        if (path != null) {
                            Utils.viewSnackBar(getString(R.string.message_save_manual) + path, principalLayout);
                        } else {
                            Utils.viewSnackBar(getString(R.string.error_dont_save_photo), principalLayout);
                        }

                        return null;
                    }

                    protected void onPostExecute(String msg) {
                        progressLoadingIndicator.setVisibility(View.GONE);
                    }
                }.execute();
            }
        }
    }

    //verify if the permission is active
    private boolean isActivePermission(String permission) {
        //this map is return in method onRequestPermissionsResult for view what permissions are actives
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (mapPermissions != null) {
                if (mapPermissions.size() > 0) {
                    //obtain the code of camera permissions
                    return mapPermissions.get(permission);
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mapPermissions = magicalPermissions.permissionResult(requestCode, permissions, grantResults);

        for (String permission : mapPermissions.keySet()) {
            Log.d("PERMISSIONS", permission + " was: " + mapPermissions.get(permission));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_shared:
                Utils.sharedApp(this);
                break;

            case R.id.menu_setting:
                startActivity(new Intent(MainActivity.this, SharedPreferenceActivity.class));
                break;

            case R.id.menu_about:
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                break;

            case R.id.menu_git:
                Utils.goToWebView(getString(R.string.link_git), MainActivity.this);
                break;

            case R.id.menu_donate:
                //Utils.goToWebView(getString(R.string.link_donate), MainActivity.this);
                startActivity(new Intent(MainActivity.this, HelpUsActivity.class));
                break;

            case R.id.menu_todo:
                Utils.goToWebView(getString(R.string.link_todo), MainActivity.this);
                break;

            case R.id.menu_documentation:
                Utils.goToWebView(getString(R.string.link_documentation), MainActivity.this);
                //startActivity(new Intent(MainActivity.this, MarkDownReadMeActivity.class));
                break;

            case R.id.menu_hire_us:
                startActivity(new Intent(MainActivity.this, HireUsActivity.class));
                break;

            default:
                break;
        }
        return true;
    }

    private void setUIComponents() {
        imageView = (ImageView) findViewById(R.id.imageView);
        btntakephoto = (ImageButton) findViewById(R.id.btntakephoto);
        btnselectedphoto = (ImageButton) findViewById(R.id.btnselectedphoto);
        btnGoTo = (Button) findViewById(R.id.btnGoTo);
        texttitle = (TextView) findViewById(R.id.texttitle);
        saveImage = (ImageButton) findViewById(R.id.saveImage);
        floatingBtnRotate = (FloatingActionButton) findViewById(R.id.floatingBtnRotate);
        floatingBtnFacialRecognition = (FloatingActionButton) findViewById(R.id.floatingBtnFacialRecognition);
        floatingBtnPhotoInformation = (FloatingActionButton) findViewById(R.id.floatingBtnPhotoInformation);
        floatingBtnSeeString64 = (FloatingActionButton) findViewById(R.id.floatingBtnSeeString64);
        frame = (FrameLayout) findViewById(R.id.frame);
        principalLayout = findViewById(R.id.principalLayout);
        floatingBtnMenu = (FloatingActionMenu) findViewById(R.id.floatingBtnMenu);
        progressLoadingIndicator = (LinearLayout) findViewById(R.id.progressLoadingIndicator);

        floatingBtnMenu.setVisibility(View.GONE);
        saveImage.setVisibility(View.GONE);
        btnGoTo.setText(getString(R.string.go_to_fragment));
        texttitle.setText(getString(R.string.title_activity));
    }
}
