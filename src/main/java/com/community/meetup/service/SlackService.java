package com.community.meetup.service;

import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.reactions.ReactionsGetRequest;
import com.slack.api.methods.request.reactions.ReactionsListRequest;
import com.slack.api.methods.request.users.UsersInfoRequest;
import com.slack.api.methods.response.reactions.ReactionsGetResponse;
import com.slack.api.methods.response.users.UsersInfoResponse;
import com.slack.api.model.Reaction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.ServiceUnavailableException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SlackService {

    private App app = new App();

    private List<Reaction> getInteractions(String messageId, String channelId, String token) throws IOException, SlackApiException {
        ReactionsGetResponse response = app.client().reactionsGet(ReactionsGetRequest.builder().token(token).channel(channelId).timestamp(messageId).build());
        if(response.isOk()){
            return response.getMessage().getReactions();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, response.getError());
        }
    }

    public int getNumberOfInteractions(String messageId, String channelId, String token) throws IOException, SlackApiException {
       return getInteractions(messageId, channelId, token).size();
    }


    private String getEmail(String token, String user) throws IOException, SlackApiException {
        UsersInfoResponse response = app.client().usersInfo(UsersInfoRequest.builder().token(token).user(user).build());
        if(response.isOk()){
            return response.getUser().getProfile().getEmail();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, response.getError());
        }
    }

    public Set<String> getEmailsFromPost(String messageId, String channelId, String token) throws IOException, SlackApiException {
        List<Reaction> interactions = getInteractions(messageId, channelId, token);
        Set<String> users = new HashSet<>();
        for(Reaction reaction : interactions){
            users.addAll(reaction.getUsers());
        }
        Set<String> emails = new HashSet<>();
        for(String user : users) {
            emails.add(getEmail(token, user));
        }
        return emails;
    }
}
