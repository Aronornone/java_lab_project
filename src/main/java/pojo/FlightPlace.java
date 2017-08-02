package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import utils.OurBitSet;

@Data
@AllArgsConstructor
public class FlightPlace {
    private long flightPlacesId;
    private Flight flight;
    private OurBitSet bitPlacesEconom;
    private OurBitSet bitPlacesBusiness;

    public FlightPlace(Flight flight, OurBitSet bitPlacesEconom, OurBitSet bitPlacesBusiness) {
        this.flight = flight;
        this.bitPlacesEconom = bitPlacesEconom;
        this.bitPlacesBusiness = bitPlacesBusiness;
    }
}
