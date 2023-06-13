package com.community.meetup.service;

import com.community.meetup.repository.EventRepo;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class MessageService {

    /**
     * Post a message to a channel your app is in using ID and message text
     */
    static String publishMessage(String channelId, String text) throws IOException, SlackApiException {

        Slack slack = Slack.getInstance();
        String token = System.getenv("SLACK_BOT_TOKEN");
        log.info("token: {}", token);

        ChatPostMessageResponse response = slack.methods(token).chatPostMessage(req -> req
                .channel(channelId)
                .text(text)
        );
        if (response.isOk()) {
            log.info("message: {}, sent to channel: {}", text, channelId);
            return response.getTs();
        } else {
            log.warn("message: {}, was not sent to channel: {} because message had errors {}", text, channelId, response.getError());
            return null;
        }
    }
}
