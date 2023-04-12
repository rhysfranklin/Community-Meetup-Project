package com.community.meetup.config;

import com.community.meetup.service.OrganiserService;
import com.slack.api.bolt.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackApp {

    @Autowired
    private OrganiserService organiserService;

    @Bean
    public App initSlackApp() {
        App app = new App();
        app.command("/add-organiser", (req, ctx) -> {
            String organiser = req.getPayload().getText();
            //add organiser to database
            organiserService.addOrganiser(organiser);
            return ctx.ack("Organiser added: " + organiser);
        });
        return app;
    }
}
