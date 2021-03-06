/**
 *
 *  Author: P. Torres
 *  Last Modified: 10/20/18
 */

package com.torrestudio.countitdown.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.torrestudio.countitdown.R;
import com.torrestudio.countitdown.constants.Constant;
import com.torrestudio.countitdown.controllers.EventDataController;
import com.torrestudio.countitdown.entities.Category;
import com.torrestudio.countitdown.entities.Event;
import com.torrestudio.countitdown.fragments.DatePickerDialogFragment;
import com.torrestudio.countitdown.fragments.TimePickerDialogFragment;
import com.torrestudio.countitdown.helpers.DateFormatHelper;

import java.io.IOException;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity
        implements View.OnClickListener, DatePickerDialogFragment.OnDateSelectedListener,
                    TimePickerDialogFragment.OnTimeSelectedListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "CreateEventActivity";

    // Member views of this Activity
    private Toolbar mActivityToolbar;
    private ImageView mEventImage;
    private FloatingActionButton mAddImageFab;
    private EditText mEventNameEditText;
    private EditText mEventDateEditText;
    private EditText mEventTimeEditText;
    private Spinner mCategorySpinner;

    // Helper booleans
    private boolean isEventImageSet;
    private boolean isEventDateSet;
    private boolean isEventTimeSet;

    // Event data to store
    private Bitmap mEventPhotoBitmap;
    private Uri mEventPhotoUri;
    private String mEventName;
    private long mEventDateTimeInMillis;
    private String mEventCategory;
    private Calendar mEventDateTimeCalendar = Calendar.getInstance();

    // Request code for image chooser
    static final int REQUEST_IMAGE_GET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        initViews();
    }

    private void initViews() {
        // Toolbar
        mActivityToolbar = findViewById(R.id.create_event_toolbar);
        setSupportActionBar(mActivityToolbar);
        ActionBar bar = getSupportActionBar();
        bar.setTitle("");
        bar.setDisplayHomeAsUpEnabled(true);

        mEventImage = findViewById(R.id.new_event_image);
        mAddImageFab = findViewById(R.id.add_event_image_fab);
        mAddImageFab.setOnClickListener(this);
        mEventNameEditText = findViewById(R.id.add_event_name_edittext);
        mEventDateEditText = findViewById(R.id.add_event_date_edittext);
        mEventDateEditText.setFocusableInTouchMode(false);
        mEventDateEditText.setOnClickListener(this);
        mEventTimeEditText = findViewById(R.id.add_event_time_edittext);
        mEventTimeEditText.setFocusableInTouchMode(false);
        mEventTimeEditText.setOnClickListener(this);

        // Spinner
        mCategorySpinner = findViewById(R.id.add_event_category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(adapter);
        mCategorySpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mEventCategory = Category.values()[position].name();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_event_menu, menu);
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_event_image_fab:
                openImageChooser();
                break;
            case R.id.add_event_date_edittext:
                DialogFragment dateDialogFragment = new DatePickerDialogFragment();
                dateDialogFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.add_event_time_edittext:
                DialogFragment timeDialogFragment = new TimePickerDialogFragment();
                timeDialogFragment.show(getSupportFragmentManager(), "timePicker");
                break;
        }
    }

    private void openImageChooser() {
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");

        if (imageIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(imageIntent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            mEventPhotoUri = data.getData();
            mEventPhotoBitmap = getBitmapFromUri(mEventPhotoUri);
            Picasso.get()
                    .load(mEventPhotoUri)
                    .fit()
                    .centerCrop()
                    .into(mEventImage);
            isEventImageSet = true;
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            Log.d(TAG, "getBitmapFromUri: " + e.getMessage());
            return null;
        }
    }


    @Override
    public void onDateSelected(DatePicker view, int year, int month, int dayOfMonth) {
        mEventDateTimeCalendar.set(year, month, dayOfMonth);
        mEventDateEditText.setText(DateFormatHelper.formatDate(mEventDateTimeCalendar));
        isEventDateSet = true;
    }

    @Override
    public void onTimeSelected(TimePicker view, int hourOfDay, int minute) {
        mEventDateTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mEventDateTimeCalendar.set(Calendar.MINUTE, minute);
        mEventTimeEditText.setText(DateFormatHelper.formatTime(mEventDateTimeCalendar));
        isEventTimeSet = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                createEventRecord();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createEventRecord() {
        if (validateInputViews()) {
            EventDataController eventController = EventDataController.initController(this);
            Event newEvent = getEventInstance();
            eventController.createEvent(newEvent);
            finish();
        } else {
            Toast.makeText(this, R.string.blank_fields, Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateInputViews() {
        setInputViewsValues();

        boolean isValid = !mEventName.isEmpty() && isEventImageSet
                        && isEventDateSet && isEventTimeSet;

        return isValid;
    }

    private void setInputViewsValues() {
        mEventName = mEventNameEditText.getText().toString();
        mEventDateTimeInMillis = mEventDateTimeCalendar.getTimeInMillis();
    }

    private Event getEventInstance() {
        Event event = new Event("event" + System.currentTimeMillis() + Constant.IMG_FILE_EXTENSION,
                mEventName, mEventDateTimeInMillis, mEventCategory);
        event.setPhotoBitmap(mEventPhotoBitmap);
        return event;
    }

}
