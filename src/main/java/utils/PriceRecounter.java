package utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PriceRecounter {
    //can make HashMap<String(factor+percents),Double/Integer(priceIncrease)> for easy display in the end
    public static double recountPrice(double basePrice, LocalDateTime dateTime, boolean business) {
        double result=basePrice;
        if (business) {
            result = affectPriceByPercents(basePrice, 20);
        }
        result = affectPriceByDate(result, dateTime);
        return (int) result; // casting int for simplicity
    }

    public static double affectPriceByPercents(double basePrice, int percents) {
        return basePrice * (1 + (double)percents / 100);
    }

    //max price increase=basePrice*0.3(30%)
    //price increases only if days until departure<120 days
    public static double affectPriceByDate(double basePrice, LocalDateTime dateTime) {
        long daysUntilDeparture = LocalDateTime.from(LocalDateTime.now()).until(dateTime, ChronoUnit.DAYS);
        if (daysUntilDeparture > 120) {
            return basePrice;
        }
        return basePrice + 0.3 * basePrice * (1 - daysUntilDeparture / 120);
    }

    public static double defectPriceByPercents(double price, int percents) {
        return price / (1 + (double)percents / 100);
    }
}
