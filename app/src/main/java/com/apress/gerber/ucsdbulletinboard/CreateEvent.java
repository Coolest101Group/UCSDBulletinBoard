package com.apress.gerber.ucsdbulletinboard;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.TextView;

import com.apress.gerber.ucsdbulletinboard.*;
import com.apress.gerber.ucsdbulletinboard.fragments.FeaturedEvents;
import com.apress.gerber.ucsdbulletinboard.models.manageEvents;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CreateEvent extends AppCompatActivity {

    public static final int CAMERA_REQUEST = 10;
    public static final int GALLERY_PICK = 20;

    public static final int ART = 1;
    public static final int FITNESS = 2;
    public static final int INFO = 4;
    public static final int COMINV = 8;
    public static final int WEEKEND = 16;
    public static boolean ANIMATION = false;

    private EditText eTitleView;
    private EditText eDescView;
    private Bitmap eImage;
    private ImageView imgEvent;
    public static int hr;
    public static int min;
    public static int yr;
    public static int mnth;
    public static int day;
    public static int category;
    String mCurrentPhotoPath;
    File photoFile = null;
    Uri myURI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        category = 0;

        if(ANIMATION){
            ImageView img = (ImageView) findViewById(R.id.animation2);
            img.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.animationconcert));
            AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
            frameAnimation.start();
        }

        // Get event information from create_event fragment
        eTitleView = (EditText)findViewById(R.id.event_title);
        eDescView = (EditText)findViewById(R.id.event_description);
        imgEvent = (ImageView) findViewById(R.id.iViewPhoto);

        // When user clicks "create" button invoke 'addEvent()' method
        Button mCreateEventButton = (Button) findViewById(R.id.event_sign_up_button);
        mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEventDB();
                }
            }
        );
        Button mAddCamera = (Button) findViewById(R.id.select_from_camera);
        mAddCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnGetCameraPhotoClicked();

            }
        });

    }

    //Handle radio buttons here!
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {

            case R.id.cat_art:
                if(checked) // set art bit
                    category = category | ART;
                else // clear art bit
                    category &= ~(1 << 0);
                break;
            case R.id.cat_fitness:
                if(checked) //set fitness bit
                    category = category | FITNESS;
                else // clear fitness bit
                    category &= ~(1 << 1);
                break;
            case R.id.cat_info:
                if(checked) // set info bit
                    category = category | INFO;
                else // clear info bit
                    category &= ~(1 << 2);
                break;
            case R.id.cat_cominv:
                if(checked) // set cominv bit
                    category = category | COMINV;
                else // clear cominv bit
                    category &= ~(1 << 3);
                break;
            case R.id.cat_weekend:
                if(checked) // set week bit
                    category = category | WEEKEND;
                else // clear week bit
                    category &= ~(1 << 4);
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    public boolean addEventDB() {
        // Reset errors
        eTitleView.setError(null);
        eDescView.setError(null);
        View focusView = null;
        boolean success = false;
        boolean abort = false;

        String mTitle = eTitleView.getText().toString();
        String mDesc = eDescView.getText().toString();
        int year = yr;
        int month = mnth;
        int day = this.day;




        String mDate = new StringBuilder().append(month)
                .append("-").append(day).append("-").append(year)
                .append(" ").toString();
        if(TextUtils.isEmpty(mTitle)){
            eTitleView.setError("Required field. Enter a title.");
            focusView = eTitleView;
            abort = true;
        }
        else if(TextUtils.isEmpty(mDesc)){
            eDescView.setError("Required field. Enter a description.");
            focusView = eDescView;
            abort = true;
        }

        if(eImage == null && !abort){
            success = MainActivity.mDB.postEvent(category, mTitle, mDate, mDesc, day, month, year, hr, min);
        }
        else if(!abort){
            success = MainActivity.mDB.postEvent(category, mTitle, mDate, mDesc, day, month, year, hr, min, eImage);
        }
        if(success){
            Toast.makeText(getApplicationContext(), "Event added successfully.", Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            if(!abort)Toast.makeText(getApplicationContext(), "Error in DB.", Toast.LENGTH_LONG).show();
            else focusView.requestFocus();

        }
        return true;
    }

    public void btnGetCameraPhotoClicked(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile = createImageFile();
                if(photoFile != null){
                    Log.i("CreateEvent", "File not null");

                }
                else{
                    Log.i("CreateEvent", "File is null");
                }
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                myURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        myURI);
                Log.i("CreateEvent", "Added extra intent to photofile");
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if (requestCode == CAMERA_REQUEST){
                Log.i("CreateEvent", "Getting image Uri");
                Uri imageUri = myURI;
                Log.i("CreateEvent", "Success, now getting inputStream");

                // declare stream to read img data from SD
                InputStream inputStream;
                try{
                    inputStream = getContentResolver().openInputStream(imageUri);

                    Bitmap data2 = BitmapFactory.decodeStream(inputStream);
                    int nh = (int) ( data2.getHeight() * (512.0 / data2.getWidth()) );
                    Bitmap scaled = Bitmap.createScaledBitmap(data2, 512, nh, true);
                    eImage = scaled;
                    imgEvent.setImageBitmap(eImage);
                }
                catch(FileNotFoundException e){
                    Toast.makeText(this, "Failed to open.", Toast.LENGTH_LONG).show();
                }
            }
        }

        if(resultCode == RESULT_OK){
            if(requestCode == GALLERY_PICK){
                // the address of image in SD Card
                Uri imageUri = data.getData();

                // declare stream to read img data from SD
                InputStream inputStream;
                try{
                    inputStream = getContentResolver().openInputStream(imageUri);

                    Bitmap data2 = BitmapFactory.decodeStream(inputStream);
                    int nh = (int) ( data2.getHeight() * (512.0 / data2.getWidth()) );
                    Bitmap scaled = Bitmap.createScaledBitmap(data2, 512, nh, true);
                    eImage = scaled;
                    imgEvent.setImageBitmap(eImage);
                }
                catch(FileNotFoundException e){
                    Toast.makeText(this, "Failed to open.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void btnGetGalleryClicked(View v){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picDirPath = pictureDirectory.getPath();
        Uri data = Uri.parse(picDirPath);

        // Set data and type
        galleryIntent.setDataAndType(data, "image/*");

        startActivityForResult(galleryIntent, GALLERY_PICK);


    }

    public void showTimePickerDialog(View v) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.i("CreateEvent", "CreateEvent successful file output");

        return image;
    }
    public void backtohome(){
        setContentView(R.layout.fragment_featuredevents);
    }

    /*
     *  Method that takes an event and retrieves its unmasked
     *  category
     */
    public static boolean checkIndividualEventCategory(myEvent event, int cat){
        int category = event.getCat();

        // TODO: Get unmasked category
        if((category & cat) > 0)
            return true;
        else
            return false;
    }

}