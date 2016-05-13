/*******************************************************************************
 * Copyright (c) 2016 Evgeny Gorbin
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

package com.socialnetwork.abhishekchandale.snloginlib.facebook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.Utility;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.socialnetwork.abhishekchandale.snloginlib.core.SocialNetwork;
import com.socialnetwork.abhishekchandale.snloginlib.core.SocialNetworkException;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnLoginCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnRequestAccessTokenCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnRequestDetailedSocialPersonCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnRequestSocialPersonCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnRequestSocialPersonsCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.persons.SocialPerson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for Facebook social network integration
 *
 * @author Evgeny Gorbin (gorbin.e.o@gmail.com)
*/
public class FacebookSocialNetwork extends SocialNetwork {
    /*** Social network ID in asne modules, should be unique*/
    public static final int ID = 4;

    private static final String PERMISSION = "publish_actions";
    private Activity fragment;
    private CallbackManager callbackManager;
    private com.socialnetwork.abhishekchandale.snloginlib.core.AccessToken accessToken;
    private ShareDialog shareDialog;
    private String mPhotoPath;
    private String mStatus;
    private Bundle mBundle;
    private List<String> permissions;
    private PendingAction mPendingAction = PendingAction.NONE;
    private String requestID;
    private FacebookCallback<LoginResult> LoginCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            if (mLocalListeners.containsKey(REQUEST_LOGIN)) {
                ((OnLoginCompleteListener) mLocalListeners.get(REQUEST_LOGIN)).onLoginSuccess(getID());
                mLocalListeners.remove(REQUEST_LOGIN);
            }
            if(mPendingAction != PendingAction.NONE) {
               // handlePendingAction();
            }
            accessToken = new com.socialnetwork.abhishekchandale.snloginlib.core.AccessToken(loginResult.getAccessToken().getToken(), null);

        }

        @Override
        public void onCancel() {
            if (mLocalListeners.containsKey(REQUEST_LOGIN)) {
                mLocalListeners.get(REQUEST_LOGIN).onError(getID(), REQUEST_LOGIN, null, null);
                mLocalListeners.remove(REQUEST_LOGIN);
            }
            if(mPendingAction != PendingAction.NONE) {
               // publishSuccess(requestID, "requestPermissions canceled");
            }

        }

        @Override
        public void onError(FacebookException exception) {

            if (mLocalListeners.containsKey(REQUEST_LOGIN)) {
                mLocalListeners.get(REQUEST_LOGIN).onError(getID(), REQUEST_LOGIN, exception.getMessage(), null);
                mLocalListeners.remove(REQUEST_LOGIN);
            }
            if(mPendingAction != PendingAction.NONE) {
               // publishSuccess(requestID, exception.toString());
            }
        }
    };
    private FacebookCallback<Sharer.Result> ShareCallBack = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {
          //  ((OnPostingCompleteListener) mLocalListeners.get(requestID)).onPostSuccessfully(getID());
            //mLocalListeners.remove(requestID);
        }

        @Override
        public void onCancel() {
            mLocalListeners.get(requestID).onError(getID(), requestID, "ShareDialog canceled", null);
        }

        @Override
        public void onError(FacebookException error) {
            mLocalListeners.get(requestID).onError(getID(), requestID, error.getLocalizedMessage(), null);
        }
    };


    //TODO: refactor to use an init that is shared by constructors
    public FacebookSocialNetwork(Activity fragment, ArrayList<String> permissions) {
        super(fragment);
        this.fragment = fragment;
        FacebookSdk.sdkInitialize(fragment.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(fragment);
        shareDialog.registerCallback(callbackManager, ShareCallBack);
        String applicationID = Utility.getMetadataApplicationId(fragment.getApplicationContext());

        if (applicationID == null) {
            throw new IllegalStateException("applicationID can't be null\n" +
                    "Please check https://developers.facebook.com/docs/android/getting-started/");
        }
        this.permissions = permissions;
    }

//TODO
//    public FacebookSocialNetwork(Fragment fragment, Context context, ArrayList<String> permissions) {
//        super(fragment, context);
//        FacebookSdk.sdkInitialize(fragment.getActivity().getApplicationContext());
//        String applicationID = Utility.getMetadataApplicationId(context);
//
//        if (applicationID == null) {
//            throw new IllegalStateException("applicationID can't be null\n" +
//                    "Please check https://developers.facebook.com/docs/android/getting-started/");
//        }
//        this.permissions = permissions;
//    }

    /**
     * Check is social network connected
     * @return true if connected to Facebook social network and false if not
     */
    @Override
    public boolean isConnected() {
        return com.facebook.AccessToken.getCurrentAccessToken() != null;
    }

    /**
     * Make login request - authorize in Facebook social network
     * @param onLoginCompleteListener listener to trigger when Login complete
     */
    @Override
    public void requestLogin(OnLoginCompleteListener onLoginCompleteListener) {
        super.requestLogin(onLoginCompleteListener);
        LoginManager.getInstance().logInWithReadPermissions(fragment, permissions);
        LoginManager.getInstance().registerCallback(callbackManager, LoginCallback);
    }

    @Override
    public void requestAccessToken(OnRequestAccessTokenCompleteListener onRequestAccessTokenCompleteListener) {
        super.requestAccessToken(onRequestAccessTokenCompleteListener);
        if(com.facebook.AccessToken.getCurrentAccessToken() != null) {
            accessToken = new com.socialnetwork.abhishekchandale.snloginlib.core.AccessToken(com.facebook.AccessToken.getCurrentAccessToken().getToken(), null);
            ((OnRequestAccessTokenCompleteListener) mLocalListeners.get(REQUEST_ACCESS_TOKEN)).onRequestAccessTokenComplete(getID(), accessToken);
        }
    }

    /**
     * Logout from Facebook social network
     */
    @Override
    public void logout() {
        LoginManager.getInstance().logOut();
    }

    /**
     * Get id of Facebook social network
     * @return Social network id for Facebook = 4
     */
    @Override
    public int getID() {
        return ID;
    }

    @Override
    public com.socialnetwork.abhishekchandale.snloginlib.core.AccessToken getAccessToken() {
        if(com.facebook.AccessToken.getCurrentAccessToken() != null) {
            accessToken = new com.socialnetwork.abhishekchandale.snloginlib.core.AccessToken(com.facebook.AccessToken.getCurrentAccessToken().getToken(), null);
        }
        return accessToken;
    }


    @Override
    public void requestCurrentPerson(OnRequestSocialPersonCompleteListener onRequestSocialPersonCompleteListener) {
        super.requestCurrentPerson(onRequestSocialPersonCompleteListener);

        if (com.facebook.AccessToken.getCurrentAccessToken() == null) {
            if (mLocalListeners.get(REQUEST_GET_CURRENT_PERSON) != null) {
                mLocalListeners.get(REQUEST_GET_CURRENT_PERSON).onError(getID(),
                        REQUEST_GET_PERSON, "Please login first", null);
            }
            return;
        }

        GraphRequest request = GraphRequest.newMeRequest(
                com.facebook.AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject me, GraphResponse response) {
                        if (response.getError() != null) {
                            if (mLocalListeners.get(REQUEST_GET_CURRENT_PERSON) != null) {
                                mLocalListeners.get(REQUEST_GET_CURRENT_PERSON).onError(
                                        getID(), REQUEST_GET_CURRENT_PERSON, response.getError().getErrorMessage(), null);
                            }
                            return;
                        }
                        if (mLocalListeners.get(REQUEST_GET_CURRENT_PERSON) != null) {
                            SocialPerson socialPerson = new SocialPerson();
                            try {
                                getSocialPerson(socialPerson, me);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mLocalListeners.get(REQUEST_GET_CURRENT_PERSON).onError(
                                        getID(), REQUEST_GET_CURRENT_PERSON, e.getLocalizedMessage(), null);
                            }
                            ((OnRequestSocialPersonCompleteListener) mLocalListeners.get(REQUEST_GET_CURRENT_PERSON))
                                    .onRequestSocialPersonSuccess(getID(), socialPerson);
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,link");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    public void requestSocialPerson(String userID, OnRequestSocialPersonCompleteListener onRequestSocialPersonCompleteListener) {
        throw new SocialNetworkException("requestSocialPerson isn't allowed for FacebookSocialNetwork");
    }


    @Override
    public void requestSocialPersons(String[] userID, OnRequestSocialPersonsCompleteListener onRequestSocialPersonsCompleteListener) {
        throw new SocialNetworkException("requestSocialPersons isn't allowed for FacebookSocialNetwork");
    }

    @Override
    public void requestDetailedSocialPerson(String userId, OnRequestDetailedSocialPersonCompleteListener onRequestDetailedSocialPersonCompleteListener) {
        super.requestDetailedSocialPerson(userId, onRequestDetailedSocialPersonCompleteListener);

        if(userId != null){
            throw new SocialNetworkException("requestDetailedSocialPerson isn't allowed for FacebookSocialNetwork");
        } else {
            if (com.facebook.AccessToken.getCurrentAccessToken() == null) {
                if (mLocalListeners.get(REQUEST_GET_DETAIL_PERSON) != null) {
                    mLocalListeners.get(REQUEST_GET_DETAIL_PERSON).onError(getID(),
                            REQUEST_GET_DETAIL_PERSON, "Please login first", null);
                }
                return;
            }

            GraphRequest request = GraphRequest.newMeRequest(
                    com.facebook.AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {
                            if (response.getError() != null) {
                                if (mLocalListeners.get(REQUEST_GET_DETAIL_PERSON) != null) {
                                    mLocalListeners.get(REQUEST_GET_DETAIL_PERSON).onError(
                                            getID(), REQUEST_GET_DETAIL_PERSON, response.getError().getErrorMessage(), null);
                                }
                                return;
                            }
                            if (mLocalListeners.get(REQUEST_GET_DETAIL_PERSON) != null) {
                                FacebookPerson facebookPerson = new FacebookPerson();
                                try {
                                    getDetailedSocialPerson(facebookPerson, me);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    mLocalListeners.get(REQUEST_GET_DETAIL_PERSON).onError(
                                            getID(), REQUEST_GET_DETAIL_PERSON, e.getLocalizedMessage(), null);
                                }
                                ((OnRequestDetailedSocialPersonCompleteListener) mLocalListeners.get(REQUEST_GET_DETAIL_PERSON))
                                        .onRequestDetailedSocialPersonSuccess(getID(), facebookPerson);
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,link,about,bio,birthday," +
                    "first_name,gender,hometown,is_verified,last_name,locale,middle_name," +
                    "timezone,updated_time,verified,website,cover");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }


    private SocialPerson getSocialPerson(SocialPerson socialPerson, JSONObject user) throws JSONException {

        if(user.has("id")) {
            socialPerson.id = user.getString("id");
            socialPerson.avatarURL = String.format("https://graph.facebook.com/%s/picture?type=large", user.getString("id"));
            if(user.has("link")) {
                socialPerson.profileURL = user.getString("link");
            } else {
                socialPerson.profileURL = String.format("https://www.facebook.com/", user.getString("id"));
            }
        }
        if(user.has("name")) {
            socialPerson.name = user.getString("name");
        }
        if(user.has("email")) {
            socialPerson.email = user.getString("email");
        }
        return socialPerson;
    }

    private FacebookPerson getDetailedSocialPerson(FacebookPerson facebookPerson, JSONObject user) throws JSONException {
        getSocialPerson(facebookPerson, user);
        if(user.has("first_name")) {
            facebookPerson.firstName = user.getString("first_name");
        }
        if(user.has("middle_name")) {
            facebookPerson.middleName = user.getString("middle_name");
        }
        if(user.has("last_name")) {
            facebookPerson.lastName = user.getString("last_name");
        }
        if(user.has("gender")) {
            facebookPerson.gender = user.getString("gender");
        }
        if(user.has("birthday")) {
            facebookPerson.birthday = user.getString("birthday");
        }
        if(user.has("verified")) {
            facebookPerson.verified = user.getString("verified");
        }
        return facebookPerson;
    }



    private void performPublish(PendingAction action) {
        if(com.facebook.AccessToken.getCurrentAccessToken() != null) {
            mPendingAction = action;
        } else {
            mLocalListeners.get(requestID).onError(getID(),
                    requestID, "User should be logged first", null);
        }
    }


    /**
     * Overrided for facebook
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }











    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE,
        POST_LINK
    }
}
