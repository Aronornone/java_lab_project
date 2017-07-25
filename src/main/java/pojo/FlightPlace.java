package pojo;

import java.util.BitSet;

public class FlightPlace {
    private long flightPlacesId;
    private Flight flightId;
    private BitSet bitPlaces;

    public long getFlightPlacesId() {
        return flightPlacesId;
    }

    public void setFlightPlacesId(long flightPlacesId) {
        this.flightPlacesId = flightPlacesId;
    }

    public Flight getFlightId() {
        return flightId;
    }

    public void setFlightId(Flight flightId) {
        this.flightId = flightId;
    }

    public BitSet getBitPlaces() {
        return bitPlaces;
    }

    public void setBitPlaces(BitSet bitPlaces) {
        this.bitPlaces = bitPlaces;
    }
}
