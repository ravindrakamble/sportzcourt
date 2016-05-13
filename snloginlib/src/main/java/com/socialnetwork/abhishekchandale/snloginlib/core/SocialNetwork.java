/*******************************************************************************
 * Copyright (c) 2014 Evgeny Gorbin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/
package com.socialnetwork.abhishekchandale.snloginlib.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnLoginCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnRequestAccessTokenCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnRequestDetailedSocialPersonCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnRequestSocialPersonCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnRequestSocialPersonsCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.base.SocialNetworkListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.socialnetwork.abhishekchandale.snloginlib.core.Consts.TAG;

public abstract class SocialNetwork {

    /*** Used to check is login request in progress*/
    public static final String REQUEST_LOGIN = "SocialNetwork.REQUEST_LOGIN";
    /*** Used to check is login request in progress for social networks with OAuth*/
    public static final String REQUEST_LOGIN2 = "SocialNetwork.REQUEST_LOGIN2";
    /*** Used to check is access token request in progress*/
    public static final String REQUEST_ACCESS_TOKEN = "SocialNetwork.REQUEST_ACCESS_TOKEN";
    /*** Used to check is get detailed person request in progress*/
    public static final String REQUEST_GET_DETAIL_PERSON = "SocialNetwork.REQUEST_GET_DETAIL_PERSON";
    /*** Used to check is get person request in progress*/
    public static final String REQUEST_GET_PERSON = "SocialNetwork.REQUEST_GET_PERSON";
    /*** Used to check is get persons request in progress*/
    public static final String REQUEST_GET_PERSONS = "SocialNetwork.REQUEST_GET_PERSONS";
    /*** Used to check is get current person request in progress*/
    public static final String REQUEST_GET_CURRENT_PERSON = "SocialNetwork.REQUEST_GET_CURRENT_PERSON";
    /*** Used to check is post message request in progress*/
    public static final String REQUEST_POST_MESSAGE = "SocialNetwork.REQUEST_POST_MESSAGE";
    /*** Used to check is post photo request in progress*/
    public static final String REQUEST_POST_PHOTO = "SocialNetwork.REQUEST_POST_PHOTO";
    /*** Used to check is post link request in progress*/
    public static final String REQUEST_POST_LINK = "SocialNetwork.REQUEST_POST_LINK";
    /*** Used to check is post dialog request in progress*/
    public static final String REQUEST_POST_DIALOG = "SocialNetwork.REQUEST_POST_DIALOG";
    /*** Used to check is checking friend request in progress*/
    public static final String REQUEST_CHECK_IS_FRIEND = "SocialNetwork.REQUEST_CHECK_IS_FRIEND";
    /*** Used to check is get friends list request in progress*/
    public static final String REQUEST_GET_FRIENDS = "SocialNetwork.REQUEST_GET_FRIENDS";
    /*** Used to check is add friend request in progress*/
    public static final String REQUEST_ADD_FRIEND = "SocialNetwork.REQUEST_ADD_FRIEND";
    /*** Used to check is remove friend request in progress*/
    public static final String REQUEST_REMOVE_FRIEND = "SocialNetwork.REQUEST_REMOVE_FRIEND";

    /*** Share bundle constant for message*/
    public static final String BUNDLE_MESSAGE = "message";
    /*** Share bundle constant for link*/
    public static final String BUNDLE_LINK = "link";
    /*** Share bundle constant for friendslist*/
    public static final String DIALOG_FRIENDS = "friends";
    /*** Share bundle constant for title*/
    public static final String BUNDLE_NAME = "name";
    /*** Share bundle constant for application name*/
    public static final String BUNDLE_APP_NAME = "app_name";
    /*** Share bundle constant for caption*/
    public static final String BUNDLE_CAPTION = "caption";
    /*** Share bundle constant for picture*/
    public static final String BUNDLE_PICTURE = "picture";

    /*** Shared preferences name */
    private static final String SHARED_PREFERENCES_NAME = "social_networks";
    protected Activity mSocialNetworkManager;
    protected SharedPreferences mSharedPreferences;
    protected Map<String, SocialNetworkListener> mGlobalListeners = new HashMap<String, SocialNetworkListener>();
    protected Map<String, SocialNetworkListener> mLocalListeners = new HashMap<String, SocialNetworkListener>();

    /**
     * @param ctx ant not activity or context, as we will need to call startActivityForResult,
     *                 we will want to receice on onActivityResult in out SocialNetworkManager
     *                 fragment
     */
    protected SocialNetwork(Activity ctx) {
        mSocialNetworkManager = ctx;
        mSharedPreferences = /*mSocialNetworkManager.getActivity().*/ctx.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * @param ctx the SocialMediaManager fragment.
     * @param context ant Activity or Application if not being called from a fragment
     */
    protected SocialNetwork(Activity ctx, Context context) {
        //we keep the fragment in case it is needed in future. it also minimises the changes required.
        mSocialNetworkManager = ctx;
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
    //////////////////// LIFECYCLE ////////////////////

    /**
     * Called when the Social Network activity is starting. Overrided in chosen social network
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    public void onCreate(Bundle savedInstanceState) {

    }

    /**
     * Called after onCreate(Bundle) — or after onRestart() when the activity had been stopped, but is now again being displayed to the user. Overrided in chosen social network.
     */
    public void onStart() {

    }

    /**
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(), for your activity to start interacting with the user. Overrided in chosen social network.
     */
    public void onResume() {

    }

    /**
     * Called as part of the activity lifecycle when an activity is going into the background, but has not (yet) been killed. Overrided in chosen social network.
     */
    public void onPause() {

    }

    /**
     * Called when you are no longer visible to the user. Overrided in chosen social network.
     */
    public void onStop() {

    }

    /**
     * Perform any final cleanup: cancel all request before activity destroyed.
     */
    public void onDestroy() {
        cancelAll();
    }

    /**
     * Called to retrieve per-instance state from an activity before being killed so that the state can be restored in onCreate(Bundle) or onRestoreInstanceState(Bundle) (the Bundle populated by this method will be passed to both). Overrided in chosen social network.
     * @param outState Bundle in which to place your saved state.
     */
    public void onSaveInstanceState(Bundle outState) {

    }

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with, the resultCode it returned, and any additional data from it. Overrided in chosen social network
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    //////////////////// API ////////////////////

    /**
     * Check if selected social network connected: true or false
     * @return true if connected, else false
     */
    public abstract boolean isConnected();

    /**
     * Login to social network using global listener
     */
    public void requestLogin() {
        requestLogin(null);
    }

    /**
     * Login to social network using local listener
     * @param onLoginCompleteListener listener for login complete
     */
    public void requestLogin(OnLoginCompleteListener onLoginCompleteListener) {
        /*if (isConnected()) {
            //throw new SocialNetworkException("Already connected, please check isConnected() method");
            Toast.makeText(mSocialNetworkManager,"Opps Already Connected :",Toast.LENGTH_SHORT).show();
        }*/

        registerListener(REQUEST_LOGIN, onLoginCompleteListener);
    }

    /**
     * Logout from social network
     */
    public abstract void logout();

    /**
     * Get id of social network
     * @return Social network ids:<br>
     * 1 - Twitter<br>
     * 2 - LinkedIn<br>
     * 3 - Google Plus<br>
     * 4 - Facebook<br>
     * 5 - Vkontakte<br>
     * 6 - Odnoklassniki<br>
     * 7 - Instagram<br>
     */
    public abstract int getID();


    public abstract AccessToken getAccessToken();

    public void requestAccessToken(){
        requestAccessToken(null);
    }


    public void requestAccessToken(OnRequestAccessTokenCompleteListener onRequestAccessTokenCompleteListener) {
        registerListener(REQUEST_ACCESS_TOKEN, onRequestAccessTokenCompleteListener);
    }

	public void requestCurrentPerson() {
        requestCurrentPerson(null);
    }

    public void requestCurrentPerson(OnRequestSocialPersonCompleteListener onRequestSocialPersonCompleteListener) {
        registerListener(REQUEST_GET_CURRENT_PERSON, onRequestSocialPersonCompleteListener);
    }
    public void requestSocialPerson(String userID) {
        requestSocialPerson(userID, null);
    }


    public void requestSocialPerson(String userID, OnRequestSocialPersonCompleteListener onRequestSocialPersonCompleteListener) {
        registerListener(REQUEST_GET_PERSON, onRequestSocialPersonCompleteListener);
    }


    public void requestSocialPersons(String[] userID) {
        requestSocialPersons(userID, null);
    }


    public void requestSocialPersons(String[] userID, OnRequestSocialPersonsCompleteListener onRequestSocialPersonsCompleteListener) {
        registerListener(REQUEST_GET_PERSONS, onRequestSocialPersonsCompleteListener);
    }

    public void requestDetailedSocialPerson(String userID) {
        requestDetailedSocialPerson(userID, null);
    }

    /**
     * Get detailed profile for user by id using local listener. Look for detailed persons in social networks packages.
     * @param userID user id in social network
     * @param onRequestDetailedSocialPersonCompleteListener listener for request detailed social person
     */
    public void requestDetailedSocialPerson(String userID, OnRequestDetailedSocialPersonCompleteListener onRequestDetailedSocialPersonCompleteListener) {
        registerListener(REQUEST_GET_DETAIL_PERSON, onRequestDetailedSocialPersonCompleteListener);
    }

    /**
     * Get detailed profile for current user using global listener. Look for detailed persons in social networks packages.
     */
    public void requestDetailedCurrentPerson(){
        requestDetailedSocialPerson(null);
    }

    /**
     * Get detailed profile for current user using global listener. Look for detailed persons in social networks packages.
     * @param onRequestDetailedSocialPersonCompleteListener listener for request detailed social person
     */
    public void requestDetailedCurrentPerson(OnRequestDetailedSocialPersonCompleteListener onRequestDetailedSocialPersonCompleteListener) {
        requestDetailedSocialPerson(null, onRequestDetailedSocialPersonCompleteListener);
    }


    /**
     * Cancel login request
     */
    public void cancelLoginRequest() {
        mLocalListeners.remove(REQUEST_LOGIN);
    }


    public void cancelAccessTokenRequest() {
        mLocalListeners.remove(REQUEST_ACCESS_TOKEN);
    }


    public void cancelGetCurrentPersonRequest() {
        mLocalListeners.remove(REQUEST_GET_CURRENT_PERSON);
    }

    public void cancelGetSocialPersonRequest() {
        mLocalListeners.remove(REQUEST_GET_PERSON);
    }


    public void cancelGetSocialPersonsRequest() {
        mLocalListeners.remove(REQUEST_GET_PERSONS);
    }


    public void cancelGetDetailedSocialRequest() {
        mLocalListeners.remove(REQUEST_GET_DETAIL_PERSON);
    }


    public void cancelPostMessageRequest() {
        mLocalListeners.remove(REQUEST_POST_MESSAGE);
    }

    /**
     * Cancel post photo request
     */
    public void cancelPostPhotoRequest() {
        mLocalListeners.remove(REQUEST_POST_PHOTO);
    }

    /**
     * Cancel post link request
     */
    public void cancelPostLinkRequest() {
        mLocalListeners.remove(REQUEST_POST_LINK);
    }

    /**
     * Cancel share dialog request
     */
    public void cancelPostDialogRequest() {
        mLocalListeners.remove(REQUEST_POST_DIALOG);
    }

    /**
     * Cancel check friend request
     */
    public void cancelCheckIsFriendRequest() {
        mLocalListeners.remove(REQUEST_CHECK_IS_FRIEND);
    }

    /**
     * Cancel friends list request
     */
    public void cancelGetFriendsRequest() {
        mLocalListeners.remove(REQUEST_GET_FRIENDS);
    }

    /**
     * Cancel add friend request
     */
    public void cancelAddFriendRequest() {
        mLocalListeners.remove(REQUEST_ADD_FRIEND);
    }

    /**
     * Cancel remove friend request
     */
    public void cancelRemoveFriendRequest() {
        mLocalListeners.remove(REQUEST_REMOVE_FRIEND);
    }

    /**
     * Cancel all requests
     */
    public void cancelAll() {
        Log.d(TAG, this + ":SocialNetwork.cancelAll()");

        // we need to call all, because in implementations we can possible do aditional work in specific methods
        cancelLoginRequest();
        cancelAccessTokenRequest();
        cancelGetCurrentPersonRequest();
        cancelGetSocialPersonRequest();
        cancelGetSocialPersonsRequest();
        cancelGetDetailedSocialRequest();
        cancelPostMessageRequest();
        cancelPostPhotoRequest();
        cancelPostLinkRequest();
        cancelPostDialogRequest();
        cancelGetFriendsRequest();
        cancelCheckIsFriendRequest();
        cancelAddFriendRequest();
        cancelRemoveFriendRequest();

        // remove all local listeners
        mLocalListeners = new HashMap<String, SocialNetworkListener>();
    }

    //////////////////// UTIL METHODS ////////////////////

    protected void checkRequestState(AsyncTask request) throws SocialNetworkException {
        if (request != null) {
            throw new SocialNetworkException(request.toString() + "Request is already running");
        }
    }

    private void registerListener(String listenerID, SocialNetworkListener socialNetworkListener) {
        if (socialNetworkListener != null) {
            mLocalListeners.put(listenerID, socialNetworkListener);
        } else {
            mLocalListeners.put(listenerID, mGlobalListeners.get(listenerID));
        }
    }

    //////////////////// SETTERS FOR GLOBAL LISTENERS ////////////////////

    /**
     * Register a callback to be invoked when login complete.
     * @param onLoginCompleteListener the callback that will run
     */
    public void setOnLoginCompleteListener(OnLoginCompleteListener onLoginCompleteListener) {
        mGlobalListeners.put(REQUEST_LOGIN, onLoginCompleteListener);
    }

    /**
     * Register a callback to be invoked when {@link com.socialnetwork.abhishekchandale.snloginlib.core.AccessToken} request complete.
     * @param onRequestAccessTokenCompleteListener the callback that will run
     */
    public void setOnRequestAccessTokenCompleteListener(OnRequestAccessTokenCompleteListener onRequestAccessTokenCompleteListener) {
        mGlobalListeners.put(REQUEST_ACCESS_TOKEN, onRequestAccessTokenCompleteListener);
    }

    /**
     * Register a callback to be invoked when current {@link com.socialnetwork.abhishekchandale.snloginlib.core.persons.SocialPerson} request complete.
     * @param onRequestCurrentPersonCompleteListener the callback that will run
     */
    public void setOnRequestCurrentPersonCompleteListener(OnRequestSocialPersonCompleteListener onRequestCurrentPersonCompleteListener) {
        mGlobalListeners.put(REQUEST_GET_CURRENT_PERSON, onRequestCurrentPersonCompleteListener);
    }

    /**
     * Register a callback to be invoked when {@link com.socialnetwork.abhishekchandale.snloginlib.core.persons.SocialPerson} by user id request complete.
     * @param onRequestSocialPersonCompleteListener the callback that will run
     */
    public void setOnRequestSocialPersonCompleteListener(OnRequestSocialPersonCompleteListener onRequestSocialPersonCompleteListener) {
        mGlobalListeners.put(REQUEST_GET_PERSON, onRequestSocialPersonCompleteListener);
    }

    /**
     * Register a callback to be invoked when detailed social person request complete. Look for detailed persons in social networks packages.
     * @param onRequestDetailedSocialPersonCompleteListener the callback that will run
     */
	public void setOnRequestDetailedSocialPersonCompleteListener(OnRequestDetailedSocialPersonCompleteListener onRequestDetailedSocialPersonCompleteListener) {
        mGlobalListeners.put(REQUEST_GET_DETAIL_PERSON, onRequestDetailedSocialPersonCompleteListener);
    }

    /**
     * Register a callback to be invoked when {@link com.socialnetwork.abhishekchandale.snloginlib.core.persons.SocialPerson}s by array of user ids request complete.
     * @param onRequestSocialPersonsCompleteListener the callback that will run
     */
    public void setOnRequestSocialPersonsCompleteListener(OnRequestSocialPersonsCompleteListener onRequestSocialPersonsCompleteListener) {
        mGlobalListeners.put(REQUEST_GET_PERSONS, onRequestSocialPersonsCompleteListener);
    }


}
