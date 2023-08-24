package com.community.meetup.service;

import com.community.meetup.config.GoogleConfig;
import com.community.meetup.repository.EventRepo;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.slack.api.methods.SlackApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.*;


@Service
public class CalendarService {

    @Autowired
    private GoogleConfig config;

    @Autowired
    private SlackService slackService;

    @Autowired
    private EventRepo eventRepo;

    private static final String APPLICATION_NAME = "Community meetup";

    @Value("${CHANNEL_ID}")
    private String channelId;

    public void createEvent(Instant startDateTime, Instant endDateTime) throws IOException, SlackApiException, GeneralSecurityException {
        Optional<com.community.meetup.model.Event> event = eventRepo.findFirstByOrderByCreationTimestampDesc();
        String token = System.getenv("SLACK_BOT_TOKEN");
        if(event.isPresent()){
            Set<String> emails = slackService.getEmailsFromPost(event.get().getMessageId(), channelId, token);
            new DateTime(startDateTime.toEpochMilli());
            createCalendarEvent(new DateTime(startDateTime.toEpochMilli()), new DateTime(endDateTime.toEpochMilli()), new ArrayList<>(emails));
        }
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
