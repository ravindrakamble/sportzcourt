/*******************************************************************************
 * Copyright (c) 2014 Evgeny Gorbin
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/
package com.socialnetwork.abhishekchandale.snloginlib.twitter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.socialnetwork.abhishekchandale.snloginlib.core.AccessToken;
import com.socialnetwork.abhishekchandale.snloginlib.core.OAuthActivity;
import com.socialnetwork.abhishekchandale.snloginlib.core.OAuthSocialNetwork;
import com.socialnetwork.abhishekchandale.snloginlib.core.SocialNetworkAsyncTask;
import com.socialnetwork.abhishekchandale.snloginlib.core.SocialNetworkException;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnLoginCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnRequestAccessTokenCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnRequestDetailedSocialPersonCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnRequestSocialPersonCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnRequestSocialPersonsCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.persons.SocialPerson;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import twitter4j.PagableResponseList;
import twitter4j.Relationship;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Class for Twitter social network integration
 *
 * @author Anton Krasov
 * @author Evgeny Gorbin (gorbin.e.o@gmail.com)
 */
public class TwitterSocialNetwork extends OAuthSocialNetwork {
    /*** Social network ID in asne modules, should be unique*/
    public static final int ID = 1;
    private static final String SAVE_STATE_KEY_OAUTH_TOKEN = "TwitterSocialNetwork.SAVE_STATE_KEY_OAUTH_TOKEN";
    private static final String SAVE_STATE_KEY_OAUTH_SECRET = "TwitterSocialNetwork.SAVE_STATE_KEY_OAUTH_SECRET";
    private static final String SAVE_STATE_KEY_USER_ID = "TwitterSocialNetwork.SAVE_STATE_KEY_USER_ID";
    private static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    // max 16 bit to use in startActivityForResult
    private static final int REQUEST_AUTH = UUID.randomUUID().hashCode() & 0xFFFF;
    //    private final String TWITTER_CALLBACK_URL = "oauth://ASNE";
    private final String mConsumerKey;
    private final String mConsumerSecret;
    private String mRedirectURL;
    private Twitter mTwitter;
    private RequestToken mRequestToken;

    public TwitterSocialNetwork(Activity ctx, String consumerKey, String consumerSecret, String redirectURL) {
        super(ctx);

        mConsumerKey = consumerKey;
        mConsumerSecret = consumerSecret;
        mRedirectURL = redirectURL;


        if (TextUtils.isEmpty(mConsumerKey) || TextUtils.isEmpty(mConsumerSecret)) {
            throw new IllegalArgumentException("consumerKey and consumerSecret are invalid");
        }
        /*
        *
        * No authentication challenges found
        * Relevant discussions can be found on the Internet at:
        * http://www.google.co.jp/search?q=8e063946 or
        * http://www.google.co.jp/search?q=ef59cf9f
        *
        * */
        initTwitterClient();
    }

//    public TwitterSocialNetwork(Fragment fragment, Context context, String consumerKey, String consumerSecret, String redirectURL) {
//        super(fragment, context);
//
//        fConsumerKey = consumerKey;
//        fConsumerSecret = consumerSecret;
//        fRedirectURL = redirectURL;
//
//
//        if (TextUtils.isEmpty(fConsumerKey) || TextUtils.isEmpty(fConsumerSecret)) {
//            throw new IllegalArgumentException("consumerKey and consumerSecret are invalid");
//        }
//        /*
//        *
//        * No authentication challenges found
//        * Relevant discussions can be found on the Internet at:
//        * http://www.google.co.jp/search?q=8e063946 or
//        * http://www.google.co.jp/search?q=ef59cf9f
//        *
//        * */
//        initTwitterClient();
//    }

    private void initTwitterClient() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(mConsumerKey);
        builder.setOAuthConsumerSecret(mConsumerSecret);
        String accessToken = mSharedPreferences.getString(SAVE_STATE_KEY_OAUTH_TOKEN, null);
        String accessTokenSecret = mSharedPreferences.getString(SAVE_STATE_KEY_OAUTH_SECRET, null);

        TwitterFactory factory = new TwitterFactory(builder.build());

        if (TextUtils.isEmpty(accessToken) && TextUtils.isEmpty(accessTokenSecret)) {
            mTwitter = factory.getInstance();
        } else {
            mTwitter = factory.getInstance(new twitter4j.auth.AccessToken(accessToken, accessTokenSecret));
        }
    }

    /**
     * Check is social network connected
     * @return true if connected to Twitter social network and false if not
     */
    @Override
    public boolean isConnected() {
        String accessToken = mSharedPreferences.getString(SAVE_STATE_KEY_OAUTH_TOKEN, null);
        String accessTokenSecret = mSharedPreferences.getString(SAVE_STATE_KEY_OAUTH_SECRET, null);
        return accessToken != null && accessTokenSecret != null;
    }

    /**
     * Make login request - authorize in Twitter social network
     * @param onLoginCompleteListener listener to trigger when Login complete
     */
    @Override
    public void requestLogin(OnLoginCompleteListener onLoginCompleteListener) {
        super.requestLogin(onLoginCompleteListener);
        try {
            executeRequest(new RequestLoginAsyncTask(), null, REQUEST_LOGIN);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Logout from Twitter social network
     */
    @Override
    public void logout() {
        mSharedPreferences.edit()
                .remove(SAVE_STATE_KEY_OAUTH_TOKEN)
                .remove(SAVE_STATE_KEY_OAUTH_SECRET)
                .remove(SAVE_STATE_KEY_USER_ID)
                .apply();

        mTwitter = null;
        initTwitterClient();
    }

    /**
     * Get id of Twitter social network
     * @return Social network id for Twitter = 1
     */
    @Override
    public int getID() {
        return ID;
    }

    /**
     * Method to get AccessToken of Twitter social network
     * @return {@link com.socialnetwork.abhishekchandale.snloginlib.core.AccessToken}
     */
    @Override
    public AccessToken getAccessToken() {
        return new AccessToken(
                mSharedPreferences.getString(SAVE_STATE_KEY_OAUTH_TOKEN, null),
                mSharedPreferences.getString(SAVE_STATE_KEY_OAUTH_SECRET, null)
        );
    }

    /**
     * Request {@link com.socialnetwork.abhishekchandale.snloginlib.core.AccessToken} of Twitter social network that you can get from onRequestAccessTokenCompleteListener
     * @param onRequestAccessTokenCompleteListener listener for {@link com.socialnetwork.abhishekchandale.snloginlib.core.AccessToken} request
     */
    @Override
    public void requestAccessToken(OnRequestAccessTokenCompleteListener onRequestAccessTokenCompleteListener) {
        super.requestAccessToken(onRequestAccessTokenCompleteListener);
        ((OnRequestAccessTokenCompleteListener) mLocalListeners.get(REQUEST_ACCESS_TOKEN))
                .onRequestAccessTokenComplete(getID(), new AccessToken(
                        mSharedPreferences.getString(SAVE_STATE_KEY_OAUTH_TOKEN, null),
                        mSharedPreferences.getString(SAVE_STATE_KEY_OAUTH_SECRET, null)
                ));
    }

    /**
     * Request current user {@link com.socialnetwork.abhishekchandale.snloginlib.core.persons.SocialPerson}
     * @param onRequestSocialPersonCompleteListener listener for request {@link com.socialnetwork.abhishekchandale.snloginlib.core.persons.SocialPerson}
     */
    @Override
    public void requestCurrentPerson(OnRequestSocialPersonCompleteListener onRequestSocialPersonCompleteListener) {
        super.requestCurrentPerson(onRequestSocialPersonCompleteListener);
        executeRequest(new RequestGetSocialPersonAsyncTask(), null, REQUEST_GET_CURRENT_PERSON);
    }

    /**
     * Request {@link com.socialnetwork.abhishekchandale.snloginlib.core.persons.SocialPerson} by user id
     * @param userID id of Twitter user
     * @param onRequestSocialPersonCompleteListener listener for {@link com.socialnetwork.abhishekchandale.snloginlib.core.persons.SocialPerson} request
     */
    @Override
    public void requestSocialPerson(String userID, OnRequestSocialPersonCompleteListener onRequestSocialPersonCompleteListener) {
        super.requestSocialPerson(userID, onRequestSocialPersonCompleteListener);
        if (TextUtils.isEmpty(userID)) {
            throw new SocialNetworkException("userID can't be null or empty");
        }
        Bundle args = new Bundle();
        try {
            args.putLong(RequestGetSocialPersonAsyncTask.PARAM_USER_ID, Long.parseLong(userID));
        } catch (NumberFormatException e) {
            throw new SocialNetworkException("userID should be long number");
        }
        executeRequest(new RequestGetSocialPersonAsyncTask(), args, REQUEST_GET_PERSON);
    }

    /**
     * Request ArrayList of {@link com.socialnetwork.abhishekchandale.snloginlib.core.persons.SocialPerson} by array of userIds
     * @param userID array of user ids in social network
     * @param onRequestSocialPersonsCompleteListener listener for request ArrayList of {@link com.socialnetwork.abhishekchandale.snloginlib.core.persons.SocialPerson}
     */
    @Override
    public void requestSocialPersons(String[] userID, OnRequestSocialPersonsCompleteListener onRequestSocialPersonsCompleteListener) {
        super.requestSocialPersons(userID, onRequestSocialPersonsCompleteListener);
        ArrayList<String> users = new ArrayList<String>(Arrays.asList(userID));
        int i = 0;
        long[] usersId = new long[users.size()];
        for (String user : users) {
            if (TextUtils.isEmpty(user)) {
                users.remove(user);
                break;
            }
            try {
                usersId[i] = Long.parseLong(user);
            } catch (NumberFormatException e) {
                throw new SocialNetworkException("userID should be long number");
            }
            i++;
        }

        Bundle args = new Bundle();
        args.putLongArray(RequestSocialPersonsAsyncTask.PARAM_USER_ID, usersId);
        executeRequest(new RequestSocialPersonsAsyncTask(), args, REQUEST_GET_PERSON);
    }

    /**
     * Request user {@link com.socialnetwork.abhishekchandale.snloginlib.twitter.TwitterPerson} by userId - detailed user data
     * @param userId id of Twitter user
     * @param onRequestDetailedSocialPersonCompleteListener listener for request detailed social person
     */
    @Override
    public void requestDetailedSocialPerson(String userId, OnRequestDetailedSocialPersonCompleteListener onRequestDetailedSocialPersonCompleteListener) {
        super.requestDetailedSocialPerson(userId, onRequestDetailedSocialPersonCompleteListener);
        Bundle args = new Bundle();
        if (userId != null) {
            try {
                args.putLong(RequestGetDetailedPersonAsyncTask.PARAM_USER_ID, Long.parseLong(userId));
            } catch (NumberFormatException e) {
                throw new SocialNetworkException("userID should be long number");
            }
        }
        executeRequest(new RequestGetDetailedPersonAsyncTask(), args, REQUEST_GET_DETAIL_PERSON);
    }

    private SocialPerson getSocialPerson(SocialPerson socialPerson, User user) {
        socialPerson.id = Long.toString(user.getId());
        socialPerson.name = user.getName();
        socialPerson.avatarURL = user.getBiggerProfileImageURL();
        socialPerson.profileURL = user.getURL();
        return socialPerson;
    }

    private TwitterPerson getDetailedSocialPerson(TwitterPerson twitterPerson, User user) {
        getSocialPerson(twitterPerson, user);
        twitterPerson.createdDate = user.getCreatedAt().getTime();
        twitterPerson.description = user.getDescription();
        twitterPerson.favoritesCount = user.getFavouritesCount();
        twitterPerson.followersCount = user.getFollowersCount();
        twitterPerson.friendsCount = user.getFriendsCount();
        twitterPerson.lang = user.getLang();
        twitterPerson.location = user.getLocation();
        twitterPerson.screenName = user.getScreenName();
        if (user.getStatus() != null) {
            twitterPerson.status = user.getStatus().getText();
        } else {
            twitterPerson.status = null;
        }
        twitterPerson.timezone = user.getTimeZone();
        twitterPerson.isTranslator = user.isTranslator();
        twitterPerson.isVerified = user.isVerified();
        return twitterPerson;
    }

    /**
     * Overrided for Twitter support
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int sanitizedRequestCode = requestCode & 0xFFFF;
        if (sanitizedRequestCode != REQUEST_AUTH) return;
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data != null ? data.getData() : null;

        if (uri != null && uri.toString().startsWith(mRedirectURL)) {
            String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

            RequestLogin2AsyncTask requestLogin2AsyncTask = new RequestLogin2AsyncTask();
            mRequests.put(REQUEST_LOGIN2, requestLogin2AsyncTask);
            Bundle args = new Bundle();
            args.putString(RequestLogin2AsyncTask.PARAM_VERIFIER, verifier);
            requestLogin2AsyncTask.execute(args);
        } else {
            if (mLocalListeners.get(REQUEST_LOGIN) != null) {
                mLocalListeners.get(REQUEST_LOGIN).onError(getID(), REQUEST_LOGIN, "incorrect URI returned: " + uri, null);
                mLocalListeners.remove(REQUEST_LOGIN);
            }
            /*
            *
            * No authentication challenges found
            * Relevant discussions can be found on the Internet at:
            * http://www.google.co.jp/search?q=8e063946 or
            * http://www.google.co.jp/search?q=ef59cf9f
            *
            * */
            initTwitterClient();
        }
    }

    /**
     * Cancel login request
     */
    @Override
    public void cancelLoginRequest() {
        super.cancelLoginRequest();
        initTwitterClient();
    }

    private class RequestLoginAsyncTask extends SocialNetworkAsyncTask {
        private static final String RESULT_OAUTH_LOGIN = "LoginAsyncTask.RESULT_OAUTH_LOGIN";

        @Override
        protected Bundle doInBackground(Bundle... params) {
            Bundle result = new Bundle();

            try {
                mRequestToken = mTwitter.getOAuthRequestToken(mRedirectURL);
                Uri oauthLoginURL = Uri.parse(mRequestToken.getAuthenticationURL() + "&force_login=true");

                result.putString(RESULT_OAUTH_LOGIN, oauthLoginURL.toString());
            } catch (TwitterException e) {
                result.putString(RESULT_ERROR, e.getMessage() == null ? "canceled" : e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(Bundle result) {
            if (!handleRequestResult(result, REQUEST_LOGIN)) return;

            if (result.containsKey(RESULT_OAUTH_LOGIN)) {
                Intent intent = new Intent(mSocialNetworkManager, OAuthActivity.class)
                        .putExtra(OAuthActivity.PARAM_CALLBACK, mRedirectURL)
                        .putExtra(OAuthActivity.PARAM_URL_TO_LOAD, result.getString(RESULT_OAUTH_LOGIN));

                mSocialNetworkManager.startActivityForResult(intent, REQUEST_AUTH);
            }
        }
    }

    private class RequestLogin2AsyncTask extends SocialNetworkAsyncTask {
        public static final String PARAM_VERIFIER = "Login2AsyncTask.PARAM_VERIFIER";

        private static final String RESULT_TOKEN = "Login2AsyncTask.RESULT_TOKEN";
        private static final String RESULT_SECRET = "Login2AsyncTask.RESULT_SECRET";
        private static final String RESULT_USER_ID = "Login2AsyncTask.RESULT_USER_ID";

        @Override
        protected Bundle doInBackground(Bundle... params) {
            String verifier = params[0].getString(PARAM_VERIFIER);

            Bundle result = new Bundle();

            try {
                twitter4j.auth.AccessToken accessToken = mTwitter.getOAuthAccessToken(mRequestToken, verifier);

                result.putString(RESULT_TOKEN, accessToken.getToken());
                result.putString(RESULT_SECRET, accessToken.getTokenSecret());
                result.putLong(RESULT_USER_ID, accessToken.getUserId());
            } catch (Exception e) {
                result.putString(RESULT_ERROR, e.getMessage() == null ? "canceled" : e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(Bundle result) {
            mRequests.remove(REQUEST_LOGIN2);
            if (!handleRequestResult(result, REQUEST_LOGIN)) {
                initTwitterClient();
                return;
            }

            // Shared Preferences
            mSharedPreferences.edit()
                    .putString(SAVE_STATE_KEY_OAUTH_TOKEN, result.getString(RESULT_TOKEN))
                    .putString(SAVE_STATE_KEY_OAUTH_SECRET, result.getString(RESULT_SECRET))
                    .putLong(SAVE_STATE_KEY_USER_ID, result.getLong(RESULT_USER_ID))
                    .apply();

            initTwitterClient();

            if (mLocalListeners.get(REQUEST_LOGIN) != null) {
                ((OnLoginCompleteListener) mLocalListeners.get(REQUEST_LOGIN)).onLoginSuccess(getID());
            }

            mLocalListeners.remove(REQUEST_LOGIN);
        }
    }

    private class RequestGetSocialPersonAsyncTask extends SocialNetworkAsyncTask {
        public static final String PARAM_USER_ID = "RequestGetPersonAsyncTask.PARAM_USER_ID";
        public static final String CURRENT = "RequestGetPersonAsyncTask.CURRENT";

        @Override
        protected Bundle doInBackground(Bundle... params) {
            Bundle args = params[0];
            Bundle result = new Bundle();
            Long userID;

            if (args.containsKey(PARAM_USER_ID)) {
                userID = args.getLong(PARAM_USER_ID);
                result.putBoolean(CURRENT, false);
            } else {
                userID = mSharedPreferences.getLong(SAVE_STATE_KEY_USER_ID, -1);
                result.putBoolean(CURRENT, true);
            }
            try {
                User user = mTwitter.showUser(userID);
                SocialPerson socialPerson = new SocialPerson();
                getSocialPerson(socialPerson, user);
                result.putParcelable(REQUEST_GET_PERSON, socialPerson);
            } catch (TwitterException e) {
                result.putString(RESULT_ERROR, e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Bundle result) {
            SocialPerson socialPerson = result.getParcelable(REQUEST_GET_PERSON);
            if (result.containsKey(CURRENT) && result.getBoolean(CURRENT)) {
                if (!handleRequestResult(result, REQUEST_GET_CURRENT_PERSON)) return;
                ((OnRequestSocialPersonCompleteListener) mLocalListeners.get(REQUEST_GET_CURRENT_PERSON))
                        .onRequestSocialPersonSuccess(getID(), socialPerson);
            } else {
                if (!handleRequestResult(result, REQUEST_GET_PERSON)) return;
                ((OnRequestSocialPersonCompleteListener) mLocalListeners.get(REQUEST_GET_PERSON))
                        .onRequestSocialPersonSuccess(getID(), socialPerson);
            }
        }
    }

    private class RequestGetDetailedPersonAsyncTask extends SocialNetworkAsyncTask {
        public static final String PARAM_USER_ID = "RequestGetPersonAsyncTask.PARAM_USER_ID";

        @Override
        protected Bundle doInBackground(Bundle... params) {
            Bundle args = params[0];
            Bundle result = new Bundle();
            Long userID;

            if (args.containsKey(PARAM_USER_ID)) {
                userID = args.getLong(PARAM_USER_ID);
            } else {
                userID = mSharedPreferences.getLong(SAVE_STATE_KEY_USER_ID, -1);
            }

            try {
                User user = mTwitter.showUser(userID);
                TwitterPerson twitterPerson = new TwitterPerson();
                getDetailedSocialPerson(twitterPerson, user);
                result.putParcelable(REQUEST_GET_DETAIL_PERSON, twitterPerson);
            } catch (TwitterException e) {
                result.putString(RESULT_ERROR, e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(Bundle result) {
            if (!handleRequestResult(result, REQUEST_GET_DETAIL_PERSON)) return;
            TwitterPerson twitterPerson = result.getParcelable(REQUEST_GET_DETAIL_PERSON);
            ((OnRequestDetailedSocialPersonCompleteListener) mLocalListeners.get(REQUEST_GET_DETAIL_PERSON))
                    .onRequestDetailedSocialPersonSuccess(getID(), twitterPerson);
        }
    }

    private class RequestSocialPersonsAsyncTask extends SocialNetworkAsyncTask {
        public static final String PARAM_USER_ID = "RequestGetPersonAsyncTask.PARAM_USER_ID";
        private static final String RESULT_USERS_ARRAY = "RequestPersonAsyncTask.RESULT_USERS_ARRAY";

        @Override
        protected Bundle doInBackground(Bundle... params) {
            Bundle args = params[0];
            Bundle result = new Bundle();
            long[] userIDs;
            userIDs = args.getLongArray(PARAM_USER_ID);

            try {
                List<User> users = mTwitter.lookupUsers(userIDs);
                ArrayList<SocialPerson> socialUsers = new ArrayList<SocialPerson>();
                SocialPerson socialUser = new SocialPerson();
                for (User user : users) {
                    getSocialPerson(socialUser, user);
                    socialUsers.add(socialUser);
                    socialUser = new SocialPerson();
                }
                result.putParcelableArrayList(RESULT_USERS_ARRAY, socialUsers);
            } catch (TwitterException e) {
                result.putString(RESULT_ERROR, e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Bundle result) {
            ArrayList<SocialPerson> arraylist = result.getParcelableArrayList(RESULT_USERS_ARRAY);
            if (!handleRequestResult(result, REQUEST_GET_PERSONS)) return;
            ((OnRequestSocialPersonsCompleteListener) mLocalListeners.get(REQUEST_GET_PERSONS))
                    .onRequestSocialPersonsSuccess(getID(), arraylist);
        }
    }

    private class RequestUpdateStatusAsyncTask extends SocialNetworkAsyncTask {
        public static final String PARAM_MESSAGE = "RequestUpdateStatusAsyncTask.PARAM_MESSAGE";
        public static final String PARAM_PHOTO_PATH = "RequestUpdateStatusAsyncTask.PARAM_PHOTO_PATH";
        private static final String RESULT_POST_PHOTO = "RequestUpdateStatusAsyncTask.RESULT_POST_PHOTO";
        private static final String RESULT_POST_LINK = "RequestUpdateStatusAsyncTask.RESULT_POST_LINK";


        @Override
        protected Bundle doInBackground(Bundle... params) {
            Bundle args = params[0];
            Bundle result = new Bundle();
            String paramMessage = "";
            String paramPhotoPath = null;
            String paramLink = null;

            if (args.containsKey(PARAM_MESSAGE)) {
                paramMessage = args.getString(PARAM_MESSAGE);
            }

            if (args.containsKey(PARAM_PHOTO_PATH)) {
                paramPhotoPath = args.getString(PARAM_PHOTO_PATH);

                result.putBoolean(RESULT_POST_PHOTO, true);
            } else {
                result.putBoolean(RESULT_POST_PHOTO, false);
            }

            if (args.containsKey(BUNDLE_LINK)) {
                paramLink = args.getString(BUNDLE_LINK);

                result.putBoolean(RESULT_POST_LINK, true);
            } else {
                result.putBoolean(RESULT_POST_LINK, false);
            }

            try {
                StatusUpdate status = new StatusUpdate(paramMessage);

                if (paramPhotoPath != null) {
                    status.setMedia(new File(paramPhotoPath));
                }
                if (paramLink != null) {
                    status = new StatusUpdate(paramMessage + " " + paramLink);
                }

                mTwitter.updateStatus(status);
            } catch (TwitterException e) {
                result.putString(RESULT_ERROR, e.getMessage());
            }

            return result;
        }


        @Override
        protected void onCancelled() {
        }
    }

    private class RequestGetFriendsAsyncTask extends SocialNetworkAsyncTask {
        public static final String RESULT_GET_FRIENDS = "RESULT_GET_FRIENDS";
        public static final String RESULT_GET_FRIENDS_ID = "RESULT_GET_FRIENDS_ID";

        @Override
        protected Bundle doInBackground(Bundle... params) {
            Bundle result = new Bundle();
            try {
                PagableResponseList<User> friends;// = new PagableResponseList<User>();
                long cursor = -1;
                long userID = mSharedPreferences.getLong(SAVE_STATE_KEY_USER_ID, -1);
                ArrayList<String> friendIds = new ArrayList<String>();
                ArrayList<SocialPerson> socialPersons = new ArrayList<SocialPerson>();
                SocialPerson socialPerson = new SocialPerson();
                do {
                    friends = mTwitter.getFriendsList(userID, cursor);
                    for (User user : friends) {
                        friendIds.add(String.valueOf(user.getId()));
                        getSocialPerson(socialPerson, user);
                        socialPersons.add(socialPerson);
                        socialPerson = new SocialPerson();
                    }
                } while ((cursor = friends.getNextCursor()) != 0);
                result.putStringArray(RESULT_GET_FRIENDS_ID, friendIds.toArray(new String[friendIds.size()]));
                result.putParcelableArrayList(RESULT_GET_FRIENDS, socialPersons);
            } catch (TwitterException e) {
                result.putString(RESULT_ERROR, e.getMessage());
            }
            return result;
        }

    }

    private class RequestCheckIsFriendAsyncTask extends SocialNetworkAsyncTask {
        public static final String PARAM_USER_ID = "PARAM_USER_ID";

        public static final String RESULT_IS_FRIEND = "RESULT_IS_FRIEND";
        public static final String RESULT_REQUESTED_ID = "RESULT_REQUESTED_ID";

        @Override
        protected Bundle doInBackground(Bundle... params) {
            Bundle args = params[0];
            Bundle result = new Bundle();
            Long userID = args.getLong(PARAM_USER_ID);

            result.putLong(RESULT_REQUESTED_ID, userID);
            try {
                long currentUserID = mSharedPreferences.getLong(SAVE_STATE_KEY_USER_ID, -1);

                Relationship relationship = mTwitter.showFriendship(currentUserID, userID);
                result.putBoolean(RESULT_IS_FRIEND, relationship.isSourceFollowingTarget());
            } catch (TwitterException e) {
                result.putString(RESULT_ERROR, e.getMessage());
            }

            return result;
        }


    }

    private class RequestAddFriendAsyncTask extends SocialNetworkAsyncTask {
        public static final String PARAM_USER_ID = "PARAM_USER_ID";

        public static final String RESULT_REQUESTED_ID = "RESULT_REQUESTED_ID";

        @Override
        protected Bundle doInBackground(Bundle... params) {
            Bundle args = params[0];
            Bundle result = new Bundle();
            Long userID = args.getLong(PARAM_USER_ID);

            result.putLong(RESULT_REQUESTED_ID, userID);
            try {
                mTwitter.createFriendship(userID);
            } catch (TwitterException e) {
                result.putString(RESULT_ERROR, e.getMessage());
            }

            return result;
        }

    }

    private class RequestRemoveFriendAsyncTask extends SocialNetworkAsyncTask {
        public static final String PARAM_USER_ID = "PARAM_USER_ID";

        public static final String RESULT_REQUESTED_ID = "RESULT_REQUESTED_ID";

        @Override
        protected Bundle doInBackground(Bundle... params) {
            Bundle args = params[0];
            Bundle result = new Bundle();
            Long userID = args.getLong(PARAM_USER_ID);

            result.putLong(RESULT_REQUESTED_ID, userID);
            try {
                mTwitter.destroyFriendship(userID);
            } catch (TwitterException e) {
                result.putString(RESULT_ERROR, e.getMessage());
            }

            return result;
        }


    }
}
