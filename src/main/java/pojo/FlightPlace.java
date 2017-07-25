package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.BitSet;

@Data
@AllArgsConstructor
public class FlightPlace {
    private long flightPlacesId;
    private Flight flightId;
    private BitSet bitPlaces;
}
