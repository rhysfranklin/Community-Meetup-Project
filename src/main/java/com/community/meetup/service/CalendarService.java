package com.community.meetup.service;

import com.community.meetup.config.GoogleConfig;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CalendarService {

    @Autowired
    private GoogleConfig config;

    private static final String APPLICATION_NAME = "Community meetup";

    public void createEvent(){

    }

    public void createCalendarEvent(DateTime startDateTime, DateTime endDateTime, List<String> emails) throws GeneralSecurityException, IOException {

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Calendar service =
                new Calendar.Builder(HTTP_TRANSPORT, config.JSON_FACTORY, config.getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();

        Event event = new Event()
                .setSummary("Community meetup")
                .setDescription("Community social meetup");

        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Europe/London");
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Europe/London");
        event.setEnd(end);

        event.setAttendees(createAttendees(emails));


        String calendarId = "primary";
        event = service.events().insert(calendarId, event).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());

    }

    private List<EventAttendee> createAttendees(List<String> emails){
        List<EventAttendee> attendeesList = new ArrayList<>();
        for (String email : emails){
            EventAttendee ea = new EventAttendee().setEmail(email);
            attendeesList.add(ea);
        }
        return attendeesList;
    }
}
