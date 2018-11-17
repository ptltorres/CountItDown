package com.torrestudio.countitdown.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.torrestudio.countitdown.R;
import com.torrestudio.countitdown.constants.Constant;
import com.torrestudio.countitdown.controllers.ImageStorageController;
import com.torrestudio.countitdown.database.EventContract;
import com.torrestudio.countitdown.entities.Event;

import java.io.File;

public class EventDetailActivity extends AppCompatActivity {

    public static final String EVENT_INTENT_CODE = "event_intent_code";

    private Event mEvent;

    private Toolbar mActivityToolbar;
    private ImageView mEventImageView;
    private TextView mEventNameTextView;
    private TextView mEventDaysTextView;
    private TextView mEventHoursTextView;
    private TextView mEventMinutesTextView;

    public static Intent newIntent(Context packageContext, Event event) {
        Intent intent = new Intent(packageContext, EventDetailActivity.class);
        intent.putExtra(EVENT_INTENT_CODE, event);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        mEvent = (Event) getIntent().getSerializableExtra(EVENT_INTENT_CODE);
        mEvent.getDateDifference().update();

        initViews();
    }

    private void initViews() {
        initToolbar();

        mEventImageView = findViewById(R.id.event_image);
        Picasso.get().load(getEventImageAsFile(mEvent))
                .fit()
                .centerCrop()
                .into(mEventImageView);

        mEventNameTextView = findViewById(R.id.event_name_text);
        mEventNameTextView.setText(mEvent.getName());

        mEventDaysTextView = findViewById(R.id.event_days_remaining_text);
        mEventDaysTextView.setText(Long.toString(mEvent.getDateDifference().getElapsedDays()));
        mEventHoursTextView = findViewById(R.id.event_hours_remaining_text);
        mEventHoursTextView.setText(Long.toString(mEvent.getDateDifference().getElapsedHours()));
        mEventMinutesTextView = findViewById(R.id.event_minutes_remaining_text);
        mEventMinutesTextView.setText(Long.toString(mEvent.getDateDifference().getElapsedMinutes()));
    }

    private void initToolbar() {
        mActivityToolbar = findViewById(R.id.event_detail_toolbar);
        setSupportActionBar(mActivityToolbar);
        ActionBar bar = getSupportActionBar();
        bar.setTitle("");
        bar.setDisplayHomeAsUpEnabled(true);
    }

    private File getEventImageAsFile(Event event) {
        return new ImageStorageController(this)
                .setFileName(event.getPhotoUri())
                .setDirectoryName(Constant.DIRECTORY_NAME)
                .getImageAsFile();
    }
}
