package utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class RandomGenerator {
    private static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static String createRandomSequence(int howMany) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < howMany; i++) {
            sb.append(alphabet[createNumber(0, alphabet.length)]);
        }
        return sb.toString();
    }

    public static int createNumber(int howManyDigits) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < howManyDigits; i++) {
            sb.append(createNumber(0, 10));
        }
        return Integer.parseInt(sb.toString());
    }

    public static int createNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static long createNumber(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }

    public static String createRandomDateTime(String from, String to) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = sdf.parse(from);
        Date date2 = sdf.parse(to);
        LocalDateTime dateTime = new Timestamp(createNumber(date1.getTime(), date2.getTime())).toLocalDateTime();
        dateTime = dateTime.withSecond(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }


}
