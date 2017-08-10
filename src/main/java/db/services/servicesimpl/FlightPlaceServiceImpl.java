package db.services.servicesimpl;

import db.dao.daoimpl.FlightPlaceDAOImpl;
import db.dao.interfaces.FlightPlaceDAO;
import db.services.interfaces.FlightPlaceService;
import db.services.interfaces.FlightService;
import db.services.interfaces.TicketService;
import org.apache.log4j.Logger;
import pojo.Flight;
import pojo.FlightPlace;
import pojo.OurBitSet;
import pojo.Ticket;
import utils.generator.RandomGenerator;

import java.util.List;
import java.util.Optional;

public final class FlightPlaceServiceImpl implements FlightPlaceService {
    private static Logger log = Logger.getLogger("DBLogger");
    private final FlightPlaceDAO dao = FlightPlaceDAOImpl.getInstance();

    private final static FlightPlaceService instance = new FlightPlaceServiceImpl();

    public static FlightPlaceService getInstance() {
        log.info("getInstance(): Returning instance of FlightPlaceServiceImpl");
        return instance;
    }

    private FlightPlaceServiceImpl() {
    }

    @Override
    public void add(FlightPlace flightPlaces) {
        log.info("add(airport): Delegating an 'add flight places' query to DAO with the following 'flightPlaces' object: " + flightPlaces);
        dao.add(flightPlaces);
    }

    @Override
    public Optional<FlightPlace> get(long id) {
        log.info("get(id): Delegating a 'get flight places by id' query to DAO with the following 'id' = " + id);
        return dao.get(id);
    }

    @Override
    public Optional<FlightPlace> getByFlightId(long flightId) {
        log.info("getByFlightId(flightId): Delegating a 'get flight places by flight id' query to DAO with the following 'id' = " + flightId);
        return dao.getByFlightId(flightId);
    }

    @Override
    public void update(FlightPlace flightPlaces) {
        log.info("update(flightPlaces): Delegating an 'update flight places' query to DAO with the following 'flightPlaces' object: " + flightPlaces);
        dao.update(flightPlaces);
    }

    @Override
    public void delete(FlightPlace flightPlaces) {
        log.info("delete(flightPlaces): Delegating a 'delete flight places' query to DAO with the following 'flightPlaces' object: " + flightPlaces);
        dao.delete(flightPlaces);
    }

    @Override
    public List<FlightPlace> getAll() {
        log.info("getAll(): Delegating a 'get all flight places' query to DAO.");
        return dao.getAll();
    }

    /**
     * Method for revert places to flights if session of user is ended and he doesn't pay for invoice.
     * If you use it for reset places in ticket from econom to business and conversely,
     * you must first revert places, then change ticket class (to business\econom).
     * After reverting tickets are deleted.
     *
     * @param tickets List of tickets on which we must update sitting places to flight and bitsetFlightPlace
     */
    @Override
    public void revertSittingPlaces(List<Ticket> tickets) {
        log.info("revertSittingPlaces(tickets): Starting to execute the method.");
        Flight flight;
        log.info("revertSittingPlaces(tickets): Getting instances of FlightPlaceServiceImpl, FlightServiceImpl, TicketServiceImpl.");
        FlightPlaceService flightPlaceService = FlightPlaceServiceImpl.getInstance();
        FlightService flightService = FlightServiceImpl.getInstance();
        TicketService ticketService = TicketServiceImpl.getInstance();
        log.info("revertSittingPlaces(tickets): Starting to revert places.");
        for (Ticket ticket : tickets) {
            long flightId = ticket.getFlight().getFlightId();
            FlightPlace flightPlace = flightPlaceService.getByFlightId((int) flightId).orElse(null);
            flight = flightService.get((int) flightId).orElse(null);
            OurBitSet newPlacesBitSet;
            if (ticket.isBusinessClass()) {
                flight.setAvailablePlacesBusiness(flight.getAvailablePlacesBusiness() + 1);
                newPlacesBitSet = flightPlace.getBitPlacesBusiness();
                newPlacesBitSet.clear(ticket.getSittingPlace());
                flightPlace.setBitPlacesBusiness(newPlacesBitSet);
            } else {
                flight.setAvailablePlacesEconom(flight.getAvailablePlacesEconom() + 1);
                newPlacesBitSet = flightPlace.getBitPlacesEconom();
                newPlacesBitSet.clear(ticket.getSittingPlace());
                flightPlace.setBitPlacesEconom(newPlacesBitSet);
            }
            log.info("revertSittingPlaces(tickets): Updating flightPlaceService, flightService, ticketService.");
            flightPlaceService.update(flightPlace);
            flightService.update(flight);
            ticketService.delete(ticket);
        }
    }

    /**
     * Method returns and reserved random place in airplane after user has add it to cart.
     * Set bit for place with BitSet in FlightPlace and decrease available places of ticket class in FlightService.
     *
     * @param flightId id of FlightService to reserve.
     * @param business if true it will get random place from BitSet in business class and reserved it.
     * @return random reserved place for this flight in int
     */
    @Override
    public int getRandomSittingPlace(long flightId, boolean business) {
        log.info("getRandomSittingPlace(flightId, business): Getting instances of FlightServiceImpl, FlightPlaceServiceImpl.");
        FlightService flightService = FlightServiceImpl.getInstance();
        FlightPlaceService flightPlaceService = FlightPlaceServiceImpl.getInstance();
        log.info("getRandomSittingPlace(flightId, business): Getting a flight place by flight id.");
        FlightPlace flightPlace = flightPlaceService.getByFlightId((int) flightId).orElse(null);
        Flight flight = flightService.get((int) flightId).orElse(null);

        OurBitSet placesBitSet;
        int availablePlacesInClass;

        log.info("getRandomSittingPlace(flightId, business): Getting available places.");
        if (business) {
            placesBitSet = flightPlace.getBitPlacesBusiness();
            availablePlacesInClass = flight.getAvailablePlacesBusiness();
        } else {
            placesBitSet = flightPlace.getBitPlacesEconom();
            availablePlacesInClass = flight.getAvailablePlacesEconom();
        }

        int lengthPlacesBitSet = placesBitSet.length();
        int reservedPlace = 0;

        while (true) {
            if (availablePlacesInClass == 0) {
                log.info("getRandomSittingPlace(flightId, business): Not enough placesBitSet.");
                break;
            }
            log.info("getRandomSittingPlace(flightId, business): Reserving random place.");
            reservedPlace = RandomGenerator.createNumber(1, lengthPlacesBitSet);
            if (placesBitSet.get(reservedPlace)) {
                continue;
            }
            placesBitSet.set(reservedPlace);
            log.info("getRandomSittingPlace(flightId, business): Reducing available places.");
            if (business) {
                flight.setAvailablePlacesBusiness(flight.getAvailablePlacesBusiness() - 1);
            } else {
                flight.setAvailablePlacesEconom(flight.getAvailablePlacesEconom() - 1);
            }
            log.info("getRandomSittingPlace(flightId, business): Updating flightService.");
            flightService.update(flight);
            flight = flightService.get((int) flightId).orElse(null);
            log.info("getRandomSittingPlace(flightId, business): Getting a flight place by flight id.");
            flightPlace = flightPlaceService.getByFlightId((int) flight.getFlightId()).orElse(null);
            if (business) {
                flightPlace.setBitPlacesBusiness(placesBitSet);
            } else {
                flightPlace.setBitPlacesEconom(placesBitSet);
            }
            log.info("getRandomSittingPlace(flightId, business): Updating flightPlaceService.");
            flightPlaceService.update(flightPlace);
            break;
        }

        log.info("getRandomSittingPlace(flightId, business): Returning reservedPlace number: " + reservedPlace);
        return reservedPlace;
    }
}
