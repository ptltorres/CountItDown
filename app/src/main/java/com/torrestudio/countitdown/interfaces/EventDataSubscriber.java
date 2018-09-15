package com.torrestudio.countitdown.interfaces;
import com.torrestudio.countitdown.entities.Event;

import java.io.Serializable;

public interface EventDataSubscriber {
    void onEventCreated(Event e);
}
