package com.community.meetup.controller;

import com.slack.api.bolt.App;

import javax.servlet.annotation.WebServlet;
import com.slack.api.bolt.servlet.SlackAppServlet;

@WebServlet("/slack/organiser")
public class SlackController extends SlackAppServlet {
    public SlackController(App app) {
        super(app);
    }
}