package com.community.meetup.config;

import com.community.meetup.service.CalendarService;
import com.community.meetup.service.OrganiserService;
import com.slack.api.bolt.App;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.response.views.ViewsOpenResponse;
import com.slack.api.model.view.View;
import com.slack.api.model.view.ViewState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.slack.api.model.block.Blocks.asBlocks;
import static com.slack.api.model.block.Blocks.input;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.datetimePicker;
import static com.slack.api.model.view.Views.*;

@Configuration
public class SlackApp {

    @Autowired
    private OrganiserService organiserService;

    @Autowired
    private CalendarService calendar;

    @Bean
    public App initSlackApp() {
        App app = new App();
        app.command("/add-organiser", (req, ctx) -> {
            String organiser = req.getPayload().getText();
            //add organiser to database
            organiserService.addOrganiser(organiser);
            return ctx.ack("Organiser added: " + organiser);
        });

        app.command("/create-event", (req, ctx) -> {
            String triggerId = req.getPayload().getTriggerId();
            View modalView = view(v -> v.callbackId("calendar-event")
                    .type("modal")
                    .title(viewTitle(vt -> vt.type("plain_text").text("Create calendar event")))
                    .blocks(asBlocks(
                            input(input -> input
                                    .blockId("date-block")
                                    .element(datetimePicker(pti -> pti.actionId("datetime_input").initialDateTime(Long.valueOf(Instant.now().getEpochSecond()).intValue())))
                                    .label(plainText(pt -> pt.text("Pick a date & time for the deadline"))))
                    ))
                    .close(viewClose(vc -> vc.type("plain_text").text("Cancel")))
                    .submit(viewSubmit(vs -> vs.type("plain_text").text("Submit"))));

            ViewsOpenResponse viewsOpenRes = ctx.client().viewsOpen(r -> r
                    // The token you used to initialize your app
                    .token(System.getenv("SLACK_BOT_TOKEN"))
                    .triggerId(triggerId)
                    .view(modalView)
            );
            if (viewsOpenRes.isOk()) return ctx.ack();
            else return Response.builder().statusCode(500).body(viewsOpenRes.getError()).build();
        });

        app.blockAction("calendar-event", (req, ctx) -> {
            System.out.println("calendar clicked");
            return ctx.ack();
        });

        app.viewSubmission("calendar-event", (req, ctx) -> {
            System.out.println("submission received");
            Map<String, Map<String, ViewState.Value>> stateValues = req.getPayload().getView().getState().getValues();
            System.out.println(stateValues.get("date-block").containsKey("datetime_input"));
            Instant datetime = Instant.ofEpochSecond(stateValues.get("date-block").get("datetime_input").getSelectedDateTime());
            System.out.println(datetime);
            Map<String, String> errors = new HashMap<>();
            if (datetime.isBefore(Instant.now())) {
                errors.put("date-block", "An event can't be created for a date in the past");
            }
            if (!errors.isEmpty()) {
                return ctx.ack(r -> r.responseAction("errors").errors(errors));
            } else {
                System.out.println("google event created");
                return ctx.ack();
            }
        });

        return app;
    }
}
