package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.BitSet;

@Data
@AllArgsConstructor
public class FlightPlace {
    private long flightPlacesId;
    private Flight flightId;
    private BitSet bitPlacesEconom;
    private BitSet bitPlacesBusiness;

    public FlightPlace(Flight flightId, BitSet bitPlacesEconom, BitSet bitPlacesBusiness) {
        this.flightId = flightId;
        this.bitPlacesEconom = bitPlacesEconom;
        this.bitPlacesBusiness = bitPlacesBusiness;
    }
}
