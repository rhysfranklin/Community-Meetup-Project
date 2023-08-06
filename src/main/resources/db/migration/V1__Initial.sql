CREATE TABLE organiser(
  id          serial PRIMARY KEY,
  name        varchar(255)
);

CREATE TABLE event(
  event_id            serial PRIMARY KEY,
  message_id          varchar(255) NOT NULL,
  organiser_id        int,
  creation_timestamp  timestamp NOT NULL DEFAULT NOW(),
CONSTRAINT fk_organiserId FOREIGN KEY(organiser_id) REFERENCES organiser(id)
);