package com.community.meetup.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name="event")
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long eventId;

    private String messageId;

    private long organiserId;

    @CreationTimestamp
    private Timestamp creationTimestamp;

    public Event(String messageId, long organiserId) {
        this.messageId = messageId;
        this.organiserId = organiserId;
    }
}
