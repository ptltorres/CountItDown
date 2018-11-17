/**
 *
 *  Author: P. Torres
 *  Last Modified: 10/30/18
 */

package com.torrestudio.countitdown.entities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.torrestudio.countitdown.R;
import com.torrestudio.countitdown.activities.EventDetailActivity;
import com.torrestudio.countitdown.constants.Constant;
import com.torrestudio.countitdown.controllers.ImageStorageController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
                            implements Filterable {
    private static final String TAG = "EventAdapter";

    private Context mContext;
    private List<Event> mAllEvents;
    private List<Event> mFilteredEvents;

    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Event mEvent;
        private ImageView eventImage;
        private TextView eventName;
        private TextView eventDaysRemaining;
        private TextView eventHoursRemaining;
        private TextView eventHoursRemainingText;

        public EventViewHolder(View view) {
            super(view);
            eventImage = view.findViewById(R.id.event_image);
            eventName = view.findViewById(R.id.event_name_textView);
            eventDaysRemaining = view.findViewById(R.id.event_days_remaining_text);
            eventHoursRemaining = view.findViewById(R.id.event_hours_remaining_text);
            eventHoursRemainingText = view.findViewById(R.id.hours_remaining_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = EventDetailActivity.newIntent(mContext, mEvent);
            mContext.startActivity(intent);
        }

        public void bind(Event event) {
            mEvent = event;
            Picasso.get()
                    .load(getEventImageAsFile(event))
                    .fit()
                    .centerCrop()
                    .into(eventImage);

            eventName.setText(event.getName());
            eventDaysRemaining.setText(Long.toString(Math.abs(event.getDateDifference().getElapsedDays())));
            eventHoursRemaining.setText(Long.toString(Math.abs(event.getDateDifference().getElapsedHours())));

            if (event.isPastEvent())
                eventHoursRemainingText.setText(R.string.event_hours_ago_text);
            else
                eventHoursRemainingText.setText(R.string.event_hours_remaining_text);
        }

        private File getEventImageAsFile(Event event) {
             return new ImageStorageController(mContext)
                     .setFileName(event.getPhotoUri())
                     .setDirectoryName(Constant.DIRECTORY_NAME)
                     .getImageAsFile();
         }
    }

    public EventAdapter(Context context, List<Event> events) {
        mContext = context;
        mAllEvents = events;
        mFilteredEvents = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_event_layout, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = mFilteredEvents.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return mFilteredEvents.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();

                if (charString.isEmpty()) {
                    mFilteredEvents = mAllEvents;
                } else {
                    List<Event> filteredList = new ArrayList<>();
                    for (Event e : mAllEvents) {
                        if (e.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(e);
                        }
                    }

                    mFilteredEvents = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredEvents;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredEvents = (ArrayList<Event>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
