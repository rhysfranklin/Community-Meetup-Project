package com.community.meetup.service;

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
    MessageService messageService;

    @Value("${CHANNEL_ID}")
    private String channelId;

    @Scheduled(cron = "001**")
    public void scheduleMessage() throws IOException, SlackApiException {
        log.info("sending scheduled message to slack");
        String message = "If you are interested in attending an upcoming social event please react with an emoji to express your interest :smiley:";
        messageService.publishMessage(channelId, message);
    }
}
