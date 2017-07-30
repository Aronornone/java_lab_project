package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Airplane {
    private long airplaneId;
    private String name;
    private int capacityEconom;
    private int capacityBusiness;

    public Airplane(String name, int capacityEconom, int capacityBusiness) {
        this.name = name;
        this.capacityEconom = capacityEconom;
        this.capacityBusiness = capacityBusiness;
    }
}
