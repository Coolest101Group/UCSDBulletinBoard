package com.apress.gerber.ucsdbulletinboard;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
    private EditText eTitleView;
    private EditText eDescView;
    private Bitmap eImage;
    private ImageView imgEvent;
    public static int hr;
    public static int min;
    public static int yr;
    public static int mnth;
    public static int day;
    String mCurrentPhotoPath;
    File photoFile = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        ImageView img = (ImageView) findViewById(R.id.animation2);
        img.setBackgroundResource(R.drawable.animationconcert);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();

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
        });
        Button mAddCamera = (Button) findViewById(R.id.select_from_camera);
        mAddCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnGetCameraPhotoClicked();

            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //finish();
        return true;
    }

    public boolean addEventDB() {
        // Reset errors
        eTitleView.setError(null);
        eDescView.setError(null);
        boolean success = false;

        String mTitle = eTitleView.getText().toString();
        String mDesc = eDescView.getText().toString();
        int year = yr;
        int month = mnth;
        int day = this.day;




        String mDate = new StringBuilder().append(month)
                .append("-").append(day).append("-").append(year)
                .append(" ").toString();
        if(eImage == null){
            success = MainActivity.mDB.postEvent(mTitle, mDate, mDesc, day, month, year, hr, min);
        }
        else{
            success = MainActivity.mDB.postEvent(mTitle, mDate, mDesc, day, month, year, hr, min, eImage);
        }
        if(success){
            Toast.makeText(getApplicationContext(), "Event added successfully.", Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_LONG).show();
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
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if (requestCode == CAMERA_REQUEST){
                Uri imageUri = Uri.fromFile(photoFile);

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
        return image;
    }

}
