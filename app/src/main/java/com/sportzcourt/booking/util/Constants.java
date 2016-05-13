package com.sportzcourt.booking.util;

/**
 * Created by ravindra.kambale on 8/28/2015.
 */
public final class Constants {
    private Constants() {}

    public static final class App {
        private App(){}

        public static final int SPLASH_TIME_IN_MILISECONDS = 3000;
        public static final String KEY_EULA_ACCEPTED = "eula_accepted";
    }

    public static final class SNConstants{
        private SNConstants(){}
        public static final int FACEBOOK=4;
        public static final  int TWITTER=1;
        public static final int GPLUS=3;
        public static final int RC_SIGN_IN = 0;
        public static final  String TYPE="Type";
        //Twiiter consumer key and Secret Key
        public static final String TWITTER_CONSUMER_KEY = "qBBOyRff2iAsspXTJleUIUYey";
        public static final String TWITTER_CONSUMER_SECRET = "zwxzTY5mUy6NZWqRGfxfMkNcx641nzJoCNlOmoQwK1x45Ic9He";
        public static final String TWITTER_CALLBACK_URL = "http://example.com";
        public static final String FB_SCOPE="public_profile, email, user_friends, user_location, user_birthday";
        public static final int  MY_PERMISSIONS_REQUEST_GET_ACCOUNTS=1;

    }

    public static final class FieldCheck {
        private FieldCheck(){}

        public static final int VALID = -1;
        public static final int PASSWORD_FIELD_EMPTY = 0;
        public static final int USERNAME_FIELD_EMPTY = 1;
        public static final int BOTH_FIELDS_EMPTY = 2;
        public static final int INVALID_EMAIL = 3;
    }
    public static final class Http {
        private Http() {}


        public static final int READ_TIMEOUT = 10;
        public static final int CONNECT_TIMEOUT = 5;
        /**
         * Base URL for all requests
         */
        public static final String URL_BASE = "https://hmwebservices.appspot.com/_ah/api/";


        /**
         * Contacts URL
         */
        public static final String URL_GET_CONTACTS_FRAG = "/1/login";
        public static final String URL_GET_CONTACTS = URL_BASE + URL_GET_CONTACTS_FRAG;

        /**
         * PARAMS for auth
         */
        public static final String PARAM_USERNAME = "username";
        public static final String PARAM_PASSWORD = "password";


        public static final String PARSE_APP_ID = "zHb2bVia6kgilYRWWdmTiEJooYA17NnkBSUVsr4H";
        public static final String PARSE_REST_API_KEY = "N2kCY1T3t3Jfhf9zpJ5MCURn3b25UpACILhnf5u9";
        public static final String HEADER_PARSE_REST_API_KEY = "X-Parse-REST-API-Key";
        public static final String HEADER_PARSE_APP_ID = "X-Parse-Application-Id";
        public static final String CONTENT_TYPE_JSON = "application/json";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String CONTENT_TYPE = "Content-Type";


    }
}
