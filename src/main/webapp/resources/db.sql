CREATE TABLE Airplane (
  id       BIGINT,
  PRIMARY KEY (id),
  name     VARCHAR(50) NOT NULL,
  capacity INT         NOT NULL
);

CREATE TABLE Flight (
  id                BIGINT,
  PRIMARY KEY (id),
  airplane_id       BIGINT      NOT NULL,
  flight_nunmber    VARCHAR(50) NOT NULL,
  departure_airport VARCHAR(50) NOT NULL,
  arrival_airport   VARCHAR(50) NOT NULL,
  base_cost         DOUBLE      NOT NULL,
  used_places       INT DEFAULT 0, #available_places INT?
  date              DATE        NOT NULL,
  FOREIGN KEY (airplane_id) REFERENCES Airplane (id)
);

CREATE TABLE FlightPlace (
  id        BIGINT,
  PRIMARY KEY (id),
  flight_id BIGINT        NOT NULL,
  places    VARCHAR(1000) NOT NULL, #bitset
  FOREIGN KEY (flight_id) REFERENCES Flight (id)
);

CREATE TABLE User (
  id                BIGINT,
  PRIMARY KEY (id),
  name              VARCHAR(100),
  password_hash     VARCHAR(1000),
  registration_date DATE
);

CREATE TABLE Invoice (
  id             BIGINT,
  PRIMARY KEY (id),
  user           BIGINT NOT NULL,
  status         VARCHAR(20),
  num_of_tickets INT    NOT NULL,
  timestamp      DATE,
  FOREIGN KEY (user) REFERENCES User (id)
);
CREATE TABLE Ticket (
  id             BIGINT,
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

