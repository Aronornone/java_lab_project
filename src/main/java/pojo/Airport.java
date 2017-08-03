package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Airport {
    private long airportId;
    private String code;
    private String city;
    private String airportName;
    private Double latitude;
    private Double longitude;

    public Airport(String code, String city, String airportName, Double latitude, Double longitude) {
        this.code = code;
        this.city = city;
        this.airportName = airportName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
