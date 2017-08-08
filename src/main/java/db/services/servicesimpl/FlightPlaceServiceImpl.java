package db.services.servicesimpl;

import db.dao.daoimpl.FlightPlaceDAOImpl;
import db.dao.interfaces.FlightPlaceDAO;
import db.services.interfaces.FlightPlaceService;
import db.services.interfaces.FlightService;
import db.services.interfaces.TicketService;
import pojo.Flight;
import pojo.FlightPlace;
import pojo.Ticket;
import utils.OurBitSet;
import utils.RandomGenerator;

import java.util.List;
import java.util.Optional;

public class FlightPlaceServiceImpl implements FlightPlaceService {
    private FlightPlaceDAO dao = new FlightPlaceDAOImpl();

    @Override
    public void add(FlightPlace flightPlaces) {
        dao.add(flightPlaces);
    }

    @Override
    public Optional<FlightPlace> get(long id) {
        return dao.get(id);
    }

    @Override
    public Optional<FlightPlace> getByFlightId(long flightId) {
        return dao.getByFlightId(flightId);
    }

    @Override
    public void update(FlightPlace flightPlaces) {
        dao.update(flightPlaces);
    }

    @Override
    public void delete(FlightPlace flightPlaces) {
        dao.delete(flightPlaces);
    }

    @Override
    public List<FlightPlace> getAll() {
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
        Flight flight;
        FlightPlaceService fps = new FlightPlaceServiceImpl();
        FlightService fs = new FlightServiceImpl();
        TicketService ts = new TicketServiceImpl();
        for (Ticket ticket : tickets) {
            long flightId = ticket.getFlight().getFlightId();
            FlightPlace flightPlace = fps.getByFlightId((int) flightId).get();
            flight = fs.get((int) flightId).get();
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
            fps.update(flightPlace);
            fs.update(flight);
            ts.delete(ticket);
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
        FlightService fs = new FlightServiceImpl();
        FlightPlaceService fps = new FlightPlaceServiceImpl();
        FlightPlace flightPlace = fps.getByFlightId((int) flightId).get();
        Flight flight = fs.get((int) flightId).get();

        OurBitSet placesBitSet;
        int availablePlacesInClass;

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
                //TODO: logging
                System.out.println("Not enough placesBitSet");
                break;
            }
            reservedPlace = RandomGenerator.createNumber(1, lengthPlacesBitSet);
            if (placesBitSet.get(reservedPlace)) {
                continue;
            }
            placesBitSet.set(reservedPlace);
            if (business) {
                flight.setAvailablePlacesBusiness(flight.getAvailablePlacesBusiness() - 1);
            } else {
                flight.setAvailablePlacesEconom(flight.getAvailablePlacesEconom() - 1);
            }
            fs.update(flight);
            flight = fs.get((int) flightId).get();
            flightPlace = fps.getByFlightId((int) flight.getFlightId()).get();
            if (business) {
                flightPlace.setBitPlacesBusiness(placesBitSet);
            } else {
                flightPlace.setBitPlacesEconom(placesBitSet);
            }
            fps.update(flightPlace);
            break;
        }
        return reservedPlace;
    }
}
