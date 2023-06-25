package com.community.meetup.service;

import com.community.meetup.model.Event;
import com.community.meetup.model.Organiser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class ScheduleService {

    @Autowired
    private MessageService messageService;

    @Autowired
    private EventService eventService;

    @Autowired
    private SlackService slackService;


    @Value("${CHANNEL_ID}")
    private String channelId;

    @Scheduled(cron = "0 0 13 1 * *")
    public void scheduleMessage() throws Exception {
        log.info("sending scheduled message to slack");
        Organiser organiser = eventService.getNextOrganiser();
        String message = "If you are interested in attending an upcoming social event please react with an emoji to express your interest, this month the person/persons responsible for organising the event is: " + organiser.getName() + " :smiley:";
        String ts = messageService.publishMessage(channelId, message);
        Event event = new Event(ts, organiser.getId());
        eventService.save(event);
    }

    @Scheduled(cron = "0 0 13 7 * *")
    public void scheduleFollowUpMessage() throws Exception {
        log.info("sending follow up message to slack");
        Optional<String> optMessageId = eventService.getMostRecentMessageId();
        if (optMessageId.isPresent()) {
            String messageId = optMessageId.get();
            String token = System.getenv("SLACK_BOT_TOKEN");
            int numberOfInteractions = slackService.getNumberOfInteractions(messageId, channelId, token);
            String message = numberOfInteractions + " people are interested in attending this months event. Please would the organiser for the next event propose a date for the event using the slack shortcut command /schedule-event ";
            messageService.publishMessage(channelId, message);
        } else {
            throw new Exception("no current events in database so could not produce follow up message");
        }
    }
}
