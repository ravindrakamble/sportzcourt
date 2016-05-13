package com.sportzcourt.booking.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sunil.Sindhe on 12/16/2015.
 */
public class ValidationHelper {

    /** The Constant REGEX_FIRST_NAME. */
    private static final String REGEX_FIRST_NAME = "^[a-zA-Z0-9'\\- ]{5,}$";

    /** The Constant FIRST_NAME. */
    private static final Pattern FIRST_NAME = Pattern.compile(REGEX_FIRST_NAME);

    /** The Constant REGEX_LAST_NAME. */
    private static final String REGEX_LAST_NAME = "^[a-zA-Z'\\- ]{2,}$";

    /** The Constant LAST_NAME. */
    private static final Pattern LAST_NAME = Pattern.compile(REGEX_LAST_NAME);

    /** The Constant REGEX_PASSWORD. */
    //Password should contain a digit[0-9], a lower case letter[a-z], an upper case letter[A-Z], one of !@#$%&* characters
    private static final String REGEX_PASSWORD= "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*]).{4,20})";

    /** The Constant PASSWORD. */
    private static final Pattern PASSWORD = Pattern.compile(REGEX_PASSWORD);

    /** The Constant REGEX_EMAIL. */
    public static final String REGEX_EMAIL = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+";

    /** The Constant EMAIL. */
    private static final Pattern EMAILID = Pattern.compile(REGEX_EMAIL);

    /** The Constant REGEX_ADDRESS. */
    private static final String REGEX_ADDRESS= "^[A-Za-z0-9#,.\\'\\-\\/ ]{5,}+$";

    /** The Constant ADDRESS. */
    private static final Pattern ADDRESS = Pattern.compile(REGEX_ADDRESS);

    /** The Constant REGEX_ADDRESS. */
    private static final String REGEX_OPTIONAL_ADDRESS= "^[A-Za-z0-9#,.\\'\\-\\/ ]+$";

    /** The Constant ADDRESS. */
    private static final Pattern OPTIONAL_ADDRESS = Pattern.compile(REGEX_OPTIONAL_ADDRESS);

    /** The Constant REGEX_CITY. */
    private static final String REGEX_CITY= "^[a-zA-Z ]{2,}+$";

    /** The Constant CITY. */
    private static final Pattern CITY = Pattern.compile(REGEX_CITY);

    /** The Constant REGEX_ZIP. */
    private static final String REGEX_ZIP= "^(?!0+$)[0-9]{5}$";

    /** The Constant ZIP. */
    private static final Pattern ZIP = Pattern.compile(REGEX_ZIP);

    /** The Constant REGEX_PHONE_FIELD1. */
    private static final String REGEX_PHONE_NUMBER= "^[0-9]{10,30}$";

    /** The Constant PHONE_FIELD1. */
    private static final Pattern PHONE_NUMBER = Pattern.compile(REGEX_PHONE_NUMBER);

    /** The Constant REGEX_PHONE_FIELD2. */
    private static final String REGEX_PHONE_FIELD2= "^[0-9]{3}$";

    /** The Constant PHONE_FIELD2. */
    private static final Pattern PHONE_FIELD2 = Pattern.compile(REGEX_PHONE_FIELD2);

    /** The Constant REGEX_PHONE_FIELD3. */
    private static final String REGEX_PHONE_FIELD3= "^[0-9]{4}$";

    /** The Constant PHONE_FIELD3. */
    private static final Pattern PHONE_FIELD3 = Pattern.compile(REGEX_PHONE_FIELD3);

    private static final String REGEX_BANK_NAME= "^[-a-zA-Z0-9& \']{5,}$";

    private static final Pattern BANK_NAME = Pattern.compile(REGEX_BANK_NAME);

    private static final String REGEX_BANK_ACC_NUMBER= "^(?!0+$)[0-9A-Z]{16}$";

    private static final Pattern BANK_ACC_NUMBER = Pattern.compile(REGEX_BANK_ACC_NUMBER);

    private static final String REGEX_CREDIT_CARD_NUMBER= "^(?!0+$)[0-9A-Z]{16}$";

    private static final Pattern CREDIT_CARD_NUMBER = Pattern.compile(REGEX_CREDIT_CARD_NUMBER);

    private static final String REGEX_ROUTING_NUMBER= "^(?!0+$)[0-9A-Z]{9}$";

    private static final Pattern BANK_ROUTING_NUMBER = Pattern.compile(REGEX_ROUTING_NUMBER);

    private static final String REGEX_CREDIT_CARD_NAME = "^[-a-zA-Z0-9& \']+$";

    private static final Pattern CREDIT_CARD_NAME = Pattern.compile(REGEX_CREDIT_CARD_NAME);

    private static final String REG_EX_CSN_NON_ZERO_NUMBER  = "^(?!0+$)[0-9]{10}$";

    private static final Pattern CSN_NON_ZERO_NUMBER = Pattern.compile(REG_EX_CSN_NON_ZERO_NUMBER);

    private static final String REG_EX_SSN_NON_ZERO_NUMBER   = "^(?!0+$)[0-9]{9}$";

    private static final Pattern SSN_NON_ZERO_NUMBER = Pattern.compile(REG_EX_SSN_NON_ZERO_NUMBER);

    public static final String REGEX_UPPER_CASE = "(?=\\p{Upper})";

    public static boolean isNetworkAvailable(Context context) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    // validating email id
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    public static boolean isValidPassword(String password) {
        if (password != null && password.length() > 6) {
            return PASSWORD.matcher(password).matches();
        }
        return false;
    }


    /**
     * Checks if is valid first name.
     *
     * @param firstName the first name
     * @return true, if is valid first name
     */
    public static boolean isValidUsernameName(final String firstName) {
        return FIRST_NAME.matcher(firstName).matches() || EMAILID.matcher(firstName).matches();

    }

    /**
     * Checks if is valid last name.
     *
     * @param lastName the last name
     * @return true, if is valid last name
     */
    public static boolean isValidLastName(final String lastName) {
        return LAST_NAME.matcher(lastName).matches();

    }

    /**
     * Checks if is valid address.
     *
     * @param address the address
     * @return true, if is valid address
     */
    public static boolean isValidAddress(final String address) {
        return ADDRESS.matcher(address).matches();

    }

    /**
     * Checks if is valid city.
     *
     * @param city the city
     * @return true, if is valid city
     */
    public static boolean isValidCity(final String city) {
        return CITY.matcher(city).matches();

    }

    /**
     * Checks if is valid zip.
     *
     * @param zip the zip
     * @return true, if is valid zip
     */
    public static boolean isValidZip(final String zip) {
        return  ZIP.matcher(zip).matches();

    }

    /**
     * Checks if is valid phone no.
     *
     * @param phoneNo the phone no
     * @return true, if is valid phone no
     */
    public static boolean isValidPhoneNumber(final String phoneNo) {
        AppUtils.log("PHONE_NUMBER " + PHONE_NUMBER.matcher(phoneNo).matches());
        return  PHONE_NUMBER.matcher(phoneNo).matches();

    }

    public static boolean isValidPhoneSecondField(final String phoneNo) {
        return  PHONE_FIELD2.matcher(phoneNo).matches();

    }

    public static boolean isValidPhoneThirdField(final String phoneNo) {
        return  PHONE_FIELD3.matcher(phoneNo).matches();

    }

    public static boolean isValidBankName(final String name) {
        return  BANK_NAME.matcher(name).matches();

    }

    public static boolean isValidBankAccountNumber(final String accNo) {
        return  BANK_ACC_NUMBER.matcher(accNo).matches();

    }

    public static boolean isValidCreditCardName(final String name) {
        return  CREDIT_CARD_NAME.matcher(name).matches();

    }

    public static boolean isValidBankRoutingNumber(final String routingNo) {
        return  BANK_ROUTING_NUMBER.matcher(routingNo).matches();

    }

    public static boolean isValidCsnNumber(final String csnNo) {
        return  CSN_NON_ZERO_NUMBER.matcher(csnNo).matches();

    }

    public static boolean isValidSsnNumber(final String SsnNo) {
        return  SSN_NON_ZERO_NUMBER.matcher(SsnNo).matches();

    }

    public static boolean isValidOptionalAddress(final String address) {
        return  OPTIONAL_ADDRESS.matcher(address).matches();

    }
}
