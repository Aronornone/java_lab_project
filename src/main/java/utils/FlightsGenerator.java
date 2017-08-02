package utils;

import db.service.AirplaneService;
import db.service.AirportService;
import lombok.SneakyThrows;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static db.DataSource.getConnection;
import static java.lang.StrictMath.*;
import static utils.FlightsGenerator.DistanceCounter.calculateDistance;
import static utils.RandomGenerator.*;

public class FlightsGenerator {
    private static AirplaneService as = new AirplaneService();
    private static AirportService aps = new AirportService();

    //Run it to insert randomly generated flights
    public static void main(String[] args) {
        fillFlightsTable(50); //put number of flights you want to insert into table
    }

    @SneakyThrows
    private static void fillFlightsTable(int number) {
        String query = "INSERT INTO flight (airplane_id,flight_number,departure_airport_id,arrival_airport_id, base_cost,available_places_econom,available_places_business,flight_datetime) VALUES " + createValues(number);
        Statement statement = getConnection().createStatement();
        statement.executeUpdate(query);
        System.out.println("Done");
    }

    private static String createValues(int number) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < number; j++) {
            sb.append(getRandomFlight()).append(',');
//            System.out.println("Processed "+(j+1));
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private static String getRandomFlight() {
        int airplaneId = createNumber(1, as.getAll().size());
        int depAirportId = createNumber(1, aps.getAll().size());
        int arrAirportId = createNumber(1, aps.getAll().size());
        String result = "(%d,'%s',%d ,%d, %d, %d ,%d ,'%s')";
        List params = new ArrayList();
        params.add(airplaneId);
        params.add(createRandomSequence(3) + "-" + createNumber(2));
        params.add(depAirportId);
        params.add(arrAirportId);
        params.add(countBaseCost(getDistance(depAirportId, arrAirportId)));
        params.add(as.get(airplaneId).get().getCapacityEconom());
        params.add(as.get(airplaneId).get().getCapacityBusiness());
        params.add(createRandomDateTime("01/09/2017", "01/09/2019"));
        return String.format(result, params.toArray());
    }

    private static double getDistance(int departureAirportId, int arrivalAirportId) {
        double lat1 = aps.get(departureAirportId).get().getLatitude();
        double lon1 = aps.get(departureAirportId).get().getLongitude();
        double lat2 = aps.get(arrivalAirportId).get().getLatitude();
        double lon2 = aps.get(arrivalAirportId).get().getLongitude();
        return calculateDistance(lat1, lon1, lat2, lon2);
    }

    private static int countBaseCost(double distance) {
//        double baseCost = 0;
//        double x = -5;
//        double counter = distance;
//        while (counter > 100) {
//            baseCost += 0.04 * Math.exp(-x);
//            counter -= 100;
//            x += 0.10;
//        }
//        return (int) (baseCost + distance * 0.04) * 60; // converted
        int basePrice=(int)(2.4*distance+500);
//        System.out.println("Distance: "+distance+" Price: "+basePrice);
        return basePrice ;
    }


    static class DistanceCounter {
        static double calculateDistance(double lat1, double long1, double lat2, double long2) {
            double earthRadius = 6371;//km
            double a = countA(lat1, long1, lat2, long2);
            double c = countC(a);
            return earthRadius * c;
        }

        private static double countC(double a) {
            return 2 * atan2(sqrt(a), sqrt(1 - a));
        }

        private static double countA(double lat1, double long1, double lat2, double long2) {
            double phi1 = toRadians(lat1);
            double phi2 = toRadians(lat2);
            double phi = toRadians(lat2 - lat1);
            double lambda = toRadians(long2 - long1);
            return sin(phi / 2) * sin(phi / 2) + cos(phi1) * cos(phi2) * sin(lambda / 2) * sin(lambda / 2);
        }
    }
}
