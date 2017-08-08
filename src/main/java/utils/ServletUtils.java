package utils;

import db.dao.DataSource;
import db.services.interfaces.TicketService;
import db.services.servicesimpl.TicketServiceImpl;
import pojo.Flight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class ServletUtils {
    private static TicketService ts = new TicketServiceImpl();

    /**
     * Util method for ease work with BitSet from Mysql. Convert String to OurBitSet object.
     *
     * @param string String with sequence of 1 and 0 bits
     * @return OurBitSet that contains installed bits from String
     */
    public static OurBitSet bitSetConversionFromString(String string) {
        OurBitSet bitSet = new OurBitSet(string.length());
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '1') {
                bitSet.set(i);
            }
        }
        return bitSet;
    }

    /**
     * Util method for ease work with BitSet from Mysql. Convert OurBitSet object to String.
     *
     * @param bitSet OurBitSet that contains installed bits
     * @return String with sequence of 1 and 0 bits
     */
    public static String stringConversionFromBitSet(BitSet bitSet) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bitSet.length(); i++) {
            if (bitSet.get(i)) {
                stringBuilder.append('1');
            } else stringBuilder.append('0');
        }
        return stringBuilder.toString();
    }

    /**
     * Method for check if all fields of tickets in invoice are contains information. Then it saves ticket info, anyway.
     *
     * @param ticketsIds     array of Strings tickets ids
     * @param passengerNames array of Strings passenger names
     * @param passports      array of Strings passports
     * @return true if some of fields are empty, false if all fields are fill right.
     */
    public static boolean isEmptyWhilePayAndSave(String[] ticketsIds, String[] passengerNames,
                                                 String[] passports, String[] luggages) {
        boolean empty = false;
        if (ticketsIds != null && passengerNames != null && passports != null) {
            List<String> ticketsList = Arrays.asList(ticketsIds);
            List<String> passengersList = Arrays.asList(passengerNames);
            List<String> passportsList = Arrays.asList(passports);

            for (String string : ticketsList) {
                if (string.isEmpty()) empty = true;
            }
            for (String string : passengersList) {
                if (string.isEmpty()) empty = true;
            }
            for (String string : passportsList) {
                if (string.isEmpty()) empty = true;
            }

            List<String> luggagesList;
            boolean[] luggagesBoolean = new boolean[ticketsList.size()];

            if (luggages != null) {
                luggagesList = Arrays.asList(luggages);
                for (int i = 0; i < luggagesList.size(); i++) {
                    luggagesBoolean[i] = (luggagesList.get(i).equals("luggage"));
                }
            }
            ts.updateTicketWhilePay(ticketsIds, passengerNames, passports, luggagesBoolean);
        } else empty = true;
        return empty;
    }

}