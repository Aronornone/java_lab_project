CREATE TABLE Airplane (
  id       BIGINT AUTO_INCREMENT,
  name     VARCHAR(50) NOT NULL,
  capacity INT         NOT NULL
);

CREATE TABLE Flight (
  id                BIGINT AUTO_INCREMENT,
  airplane_id       BIGINT       NOT NULL,
  flight_number     VARCHAR(100) NOT NULL,
  departure_airport VARCHAR(50)  NOT NULL,
  arrival_airport   VARCHAR(50)  NOT NULL,
  base_cost         DOUBLE       NOT NULL,
  used_places       INT    DEFAULT 0, --available_places INT?
  date              DATE         NOT NULL,
  FOREIGN KEY (airplane_id) REFERENCES Airplane (id),
);

CREATE TABLE FlightPlace (
  id        BIGINT AUTO_INCREMENT,
  flight_id BIGINT NOT NULL,
  places    VARCHAR(MAX), --bitset

  FOREIGN KEY (flight_id) REFERENCES Flight (id)
);

CREATE TABLE User (
  id                BIGINT AUTO_INCREMENT,
  name              VARCHAR(100) NOT NULL,
  password_hash     VARCHAR(MAX) NOT NULL,
  registration_date DATE
);

CREATE TABLE Ticket (
  id             BIGINT  AUTO_INCREMENT,
  invoice_id     BIGINT       NOT NULL,
  flight_id      BIGINT       NOT NULL,
  passenger_name VARCHAR(100) NOT NULL,
  passport_info  VARCHAR(200) NOT NULL,
  place          INT          NOT NULL,
  luggage        BOOLEAN DEFAULT FALSE,
  business_class BOOLEAN DEFAULT FALSE,
  price          DOUBLE       NOT NULL,
  FOREIGN KEY (invoice_id) REFERENCES Invoice (id),
  FOREIGN KEY (flight_id) REFERENCES Flight (id)
);

CREATE TABLE Invoice (
  id             BIGINT AUTO_INCREMENT,
  user_id        BIGINT NOT NULL,
  status         VARCHAR(20),
  num_of_tickets INT    NOT NULL,
  timestamp      DATE,
  FOREIGN KEY (user_id) REFERENCES User (id)
);
