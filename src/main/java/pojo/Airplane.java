package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Airplane {
    private long airplaneId;
    private String name;
    private int capacityEconom;
    private int capacityBusiness;
}
