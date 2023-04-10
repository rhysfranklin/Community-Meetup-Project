CREATE TABLE organiser(
  id          int PRIMARY KEY,
  name        varchar(255)
);

CREATE TABLE event(
  eventId            int PRIMARY KEY,
  messageId          varchar(255) NOT NULL,
  organiserId        int,
  creationTimestamp  timestamp DEFAULT current_timestamp,
CONSTRAINT fk_organiserId FOREIGN KEY(organiserId) REFERENCES organiser(id)
);