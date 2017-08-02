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
  code VARCHAR(100),
  city VARCHAR(100),
  airport_name VARCHAR(100),
  latitude DOUBLE NOT NULL,
  longitude DOUBLE NOT NULL
);

CREATE TABLE Flight (
  id                        BIGINT AUTO_INCREMENT,
  PRIMARY KEY (id),
  airplane_id               BIGINT      NOT NULL,
  flight_number             VARCHAR(50) NOT NULL,
  departure_airport_id      BIGINT		NOT NULL,
  arrival_airport_id        BIGINT		NOT NULL,
  base_cost                 DOUBLE      NOT NULL,
  available_places_econom   INT         NOT NULL,
  available_places_business INT         NOT NULL,
  flight_datetime           DATETIME    NOT NULL,
  FOREIGN KEY (airplane_id)             REFERENCES Airplane (id),
  FOREIGN KEY (departure_airport_id)    REFERENCES Airport (id),
  FOREIGN KEY (arrival_airport_id)      REFERENCES Airport (id)
);

CREATE TABLE FlightPlace (
  id              BIGINT AUTO_INCREMENT,
  PRIMARY KEY (id),
  flight_id       BIGINT        NOT NULL,
  places_econom   VARCHAR(1000) NOT NULL,
  places_business VARCHAR(1000) ,
  FOREIGN KEY (flight_id) REFERENCES Flight (id)
);

CREATE TABLE Account (
  id                BIGINT AUTO_INCREMENT,
  PRIMARY KEY (id),
  name              VARCHAR(100),
  email             VARCHAR(100),
  password_hash     VARCHAR(1000),
  registration_date DATETIME
);

CREATE TABLE Invoice (
  id                BIGINT AUTO_INCREMENT,
  PRIMARY KEY (id),
  account_id        BIGINT NOT NULL,
  status            VARCHAR(20),
  invoice_datetime  DATETIME,
  FOREIGN KEY (account_id) REFERENCES Account (id)
);

CREATE TABLE Ticket (
  id             BIGINT  AUTO_INCREMENT,
  PRIMARY KEY (id),
  invoice_id     BIGINT       NOT NULL,
  flight_id      BIGINT       NOT NULL,
  passenger_name VARCHAR(100) NOT NULL,
  passport       VARCHAR(100) NOT NULL,
  place          INT          NOT NULL,
  luggage        BOOLEAN DEFAULT FALSE,
  business_class BOOLEAN DEFAULT FALSE,
  price          DOUBLE       NOT NULL,
  FOREIGN KEY (invoice_id)    REFERENCES Invoice (id),
  FOREIGN KEY (flight_id)     REFERENCES Flight (id)
);

