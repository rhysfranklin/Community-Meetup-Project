package com.community.meetup.service;

import com.community.meetup.model.Event;
import com.community.meetup.repository.EventRepo;
import com.slack.api.methods.SlackApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ScheduleService {

    @Autowired
    private MessageService messageService;

    @Autowired
    private EventRepo eventRepo;

    @Value("${CHANNEL_ID}")
    private String channelId;

    @Scheduled(cron = "0 0 13 1 * *")
    public void scheduleMessage() throws IOException, SlackApiException {
        log.info("sending scheduled message to slack");
        //TODO get organiser from schedule
        long organiser = 1L;
        String message = "If you are interested in attending an upcoming social event please react with an emoji to express your interest :smiley:";
        String ts = messageService.publishMessage(channelId, message);
        Event event = new Event(ts, organiser);
        eventRepo.save(event);
    }
}
