package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Airport {
    private long airportId;
    private String name;
    private String city;

    public Airport(String name, String city) {
        this.name = name;
        this.city = city;
    }
}
