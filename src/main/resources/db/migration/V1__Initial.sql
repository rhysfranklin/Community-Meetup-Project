CREATE TABLE organiser(
  id          serial PRIMARY KEY,
  name        varchar(255)
);

CREATE TABLE event(
  eventId            serial PRIMARY KEY,
  messageId          varchar(255) NOT NULL,
  organiserId        int,
  creationTimestamp  timestamp DEFAULT current_timestamp,
CONSTRAINT fk_organiserId FOREIGN KEY(organiserId) REFERENCES organiser(id)
);