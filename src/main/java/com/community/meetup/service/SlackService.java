package com.community.meetup.service;

import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.reactions.ReactionsGetRequest;
import com.slack.api.methods.request.reactions.ReactionsListRequest;
import com.slack.api.methods.response.reactions.ReactionsGetResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.ServiceUnavailableException;
import java.io.IOException;

@Service
public class SlackService {

    private App app = new App();

    public int getNumberOfInteractions(String messageId, String channelId, String token) throws IOException, SlackApiException {
        ReactionsGetResponse response = app.client().reactionsGet(ReactionsGetRequest.builder().token(token).channel(channelId).timestamp(messageId).build());
        if(response.isOk()){
            return response.getMessage().getReactions().size();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, response.getError());
        }
    }
}
