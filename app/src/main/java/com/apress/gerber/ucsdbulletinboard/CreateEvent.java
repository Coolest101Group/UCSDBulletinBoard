package com.apress.gerber.ucsdbulletinboard;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.apress.gerber.ucsdbulletinboard.*;
import com.apress.gerber.ucsdbulletinboard.fragments.FeaturedEvents;
import com.apress.gerber.ucsdbulletinboard.models.manageEvents;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class CreateEvent extends AppCompatActivity {

    public static final int CAMERA_REQUEST = 10;
    public static final int GALLERY_PICK = 20;
    private EditText eTitleView;
    private EditText eDescView;
    private DatePicker ePicker;
    private Bitmap eImage;
    private ImageView imgEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.create_event_toolbar);
        //setSupportActionBar(toolbar);
        //actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        // Get event information from create_event fragment
        eTitleView = (EditText)findViewById(R.id.event_title);
        eDescView = (EditText)findViewById(R.id.event_description);
        ePicker = (DatePicker)findViewById(R.id.event_date);
        imgEvent = (ImageView) findViewById(R.id.iViewPhoto);

        // When user clicks "create" button invoke 'addEvent()' method
        Button mCreateEventButton = (Button) findViewById(R.id.event_sign_up_button);
        mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEventDB();
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
        int year = ePicker.getYear();
        int month = ePicker.getMonth();
        int day = ePicker.getDayOfMonth();

        String mDate = new StringBuilder().append(month +1)
                .append("-").append(day).append("-").append(year)
                .append(" ").toString();
        if(eImage == null){
            success = MainActivity.mDB.postEvent(mTitle, mDate, mDesc, day, month, year);
        }
        else{
            success = MainActivity.mDB.postEvent(mTitle, mDate, mDesc, day, month, year, eImage);
        }
        if(success){
            //Intent intent = new Intent(this, FeaturedEvents.class);
            //this.startActivity(intent);
            Toast.makeText(getApplicationContext(), "Event added successfully.", Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public void btnGetCameraPhotoClicked(View v){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, false);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if (requestCode == CAMERA_REQUEST){
                Bitmap data1 = (Bitmap) data.getExtras().get("data");
                eImage = data1;
                imgEvent.setImageBitmap(eImage);
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





}
