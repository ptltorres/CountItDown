package com.torrestudio.countitdown.entities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.torrestudio.countitdown.R;
import com.torrestudio.countitdown.constants.Constant;
import com.torrestudio.countitdown.controllers.ImageStorageController;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private static final String TAG = "EventAdapter";

    private Context mContext;
    private ArrayList<Event> mAllEvents;

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public ImageView eventImage;
        public TextView eventName;
        public TextView eventDaysRemaining;
        public TextView eventHoursRemaining;

        public EventViewHolder(View view) {
            super(view);
            eventImage = view.findViewById(R.id.event_image);
            eventName = view.findViewById(R.id.event_name_textView);
            eventDaysRemaining = view.findViewById(R.id.event_days_reaminaing_text);
            eventHoursRemaining = view.findViewById(R.id.event_hours_remaining_text);
        }
    }

    public EventAdapter(Context context, ArrayList<Event> events) {
        mContext = context;
        mAllEvents = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_event_layout, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = mAllEvents.get(position);

        File imageFile = new ImageStorageController(mContext).
                setFileName(event.getPhotoUri()).
                setDirectoryName(Constant.DIRECTORY_NAME).
                getImageAsFile();

        Picasso.get()
                .load(imageFile)
                .fit()
                .centerCrop()
                .into(holder.eventImage);

        holder.eventName.setText(event.getName());
        holder.eventDaysRemaining.setText("12");
        holder.eventHoursRemaining.setText("22");
    }

    @Override
    public int getItemCount() {
        return mAllEvents.size();
    }
}
