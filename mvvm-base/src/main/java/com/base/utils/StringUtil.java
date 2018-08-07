package com.base.utils;

import java.util.regex.Pattern;

/**
 * ka
 * 08/11/2017
 */

public class StringUtil {

    private static final String PHONE_REGEX = "(\\+[0-9]+[\\- .]*)?(\\([0-9]+\\)[\\- .]*)?([0-9][0-9\\- .]+[0-9])";
    private static final String IP_REGEX = "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9]))";

    public static boolean isValidEmail(String email) {
        Pattern EMAIL_ADDRESS_PATTERN = Pattern
                .compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static boolean isExistNull(String... arr) {
        boolean isExitNull = true;
        if (arr == null) {
            return true;
        } else {
            for (String item: arr) {
                if (!checkEmpty(item)) {
                    isExitNull = false;
                    continue;
                }
                isExitNull = true;
                break;
            }
        }

        return isExitNull;
    }

    /**
     * check phone number is valid or not
     *
     * @param phoneNumber phone number
     *
     * @return valid or not
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return !isExistNull(phoneNumber) && (phoneNumber.length() == 10 || phoneNumber.length() == 11);
    }

    /**
     * check ip address is valid or not
     *
     * @param ipAddress ip address
     *
     * @return valid or not
     */
    public static boolean isValidIpAddress(String ipAddress) {
        return !isExistNull(ipAddress) && ipAddress.matches(IP_REGEX);
    }

    private static Boolean checkEmpty(String text) {
        return text == null || text.isEmpty();
    }
}
