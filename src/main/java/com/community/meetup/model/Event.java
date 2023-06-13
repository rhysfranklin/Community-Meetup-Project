package com.community.meetup.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="event")
public class Event {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long eventId;

    private String messageId;

    private long organiserId;

    private String creationTimestamp;

    public Event(String messageId, long organiserId) {
        this.messageId = messageId;
        this.organiserId = organiserId;
    }
}
