package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import utils.OurBitSet;

@Data
@AllArgsConstructor
public class FlightPlace {
    private long flightPlacesId;
    private Flight flightId;
    private OurBitSet bitPlacesEconom;
    private OurBitSet bitPlacesBusiness;

    public FlightPlace(Flight flightId, OurBitSet bitPlacesEconom, OurBitSet bitPlacesBusiness) {
        this.flightId = flightId;
        this.bitPlacesEconom = bitPlacesEconom;
        this.bitPlacesBusiness = bitPlacesBusiness;
    }
}
