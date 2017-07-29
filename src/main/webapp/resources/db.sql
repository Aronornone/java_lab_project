CREATE TABLE Airplane (
  id                BIGINT AUTO_INCREMENT,
  PRIMARY KEY (id),
  name              VARCHAR(50) NOT NULL,
  capacity_econom   INT         NOT NULL,
  capacity_business INT         NOT NULL

);

CREATE TABLE Airport (
  id   BIGINT AUTO_INCREMENT,
  PRIMARY KEY (id),
  name VARCHAR(100),
  city VARCHAR(100)

);
CREATE TABLE Flight (
  id                        BIGINT AUTO_INCREMENT,
  PRIMARY KEY (id),
  airplane_id               BIGINT      NOT NULL,
  flight_nunmber            VARCHAR(50) NOT NULL,
  departure_airport         VARCHAR(50) NOT NULL,
  arrival_airport           VARCHAR(50) NOT NULL,
  base_cost                 DOUBLE      NOT NULL,
  available_places_econom   INT         NOT NULL,
  available_places_business INT         NOT NULL,
  date                      DATETIME    NOT NULL,
  FOREIGN KEY (airplane_id) REFERENCES Airplane (id)
);

CREATE TABLE FlightPlace (
  id              BIGINT AUTO_INCREMENT,
  PRIMARY KEY (id),
  flight_id       BIGINT        NOT NULL,
  places_econom   VARCHAR(1000) NOT NULL,
  places_business VARCHAR(1000) NOT NULL,
  FOREIGN KEY (flight_id) REFERENCES Flight (id)
);

CREATE TABLE User (
  id                BIGINT AUTO_INCREMENT,
  PRIMARY KEY (id),
  name              VARCHAR(100),
  email             VARCHAR(100),
  password_hash     VARCHAR(1000),
  registration_date DATETIME
);

CREATE TABLE Invoice (
  id             BIGINT AUTO_INCREMENT,
  PRIMARY KEY (id),
  user           BIGINT NOT NULL,
  status         VARCHAR(20),
  num_of_tickets INT    NOT NULL,
  timestamp      DATETIME,
  FOREIGN KEY (user) REFERENCES User (id)
);
CREATE TABLE Ticket (
  id             BIGINT  AUTO_INCREMENT,
  PRIMARY KEY (id),
  invoice        BIGINT       NOT NULL,
  flight         BIGINT       NOT NULL,
  passenger_name VARCHAR(100) NOT NULL,
  passport       VARCHAR(100) NOT NULL,
  place          INT          NOT NULL,
  luggage        BOOLEAN DEFAULT FALSE,
  business_class BOOLEAN DEFAULT FALSE,
  price          DOUBLE       NOT NULL,
  FOREIGN KEY (invoice) REFERENCES Invoice (id),
  FOREIGN KEY (flight) REFERENCES Flight (id)
);

