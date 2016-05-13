package com.sportzcourt.booking.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sportzcourt.booking.R;
import com.sportzcourt.booking.model.data.RegisterData;
import com.sportzcourt.booking.model.events.UserCredsEvent;
import com.sportzcourt.booking.model.request.LoginData;
import com.sportzcourt.booking.model.response.ApiResponse;
import com.sportzcourt.booking.util.AppUtils;
import com.sportzcourt.booking.util.Constants;
import com.sportzcourt.booking.util.UIUtils;
import com.socialnetwork.abhishekchandale.snloginlib.core.SocialNetwork;
import com.socialnetwork.abhishekchandale.snloginlib.core.SocialNetworkManager;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnLoginCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnRequestDetailedSocialPersonCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.listener.OnRequestSocialPersonCompleteListener;
import com.socialnetwork.abhishekchandale.snloginlib.core.persons.SocialPerson;
import com.socialnetwork.abhishekchandale.snloginlib.facebook.FacebookSocialNetwork;
import com.socialnetwork.abhishekchandale.snloginlib.gplus.GooglePlusSocialNetwork;
import com.socialnetwork.abhishekchandale.snloginlib.twitter.TwitterSocialNetwork;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Copyright 2016 (C) Happiest Minds Pvt Ltd..
 *
 * <P> Login Activity, with options of Socail Login Integration
 *
 * <P>Notes:
 * <P>Dependency:
 *
 * @authors Ravindra Kamble (ravindra.kambale@happiestminds.com)
 *          Sunil Rao S (sunil.sindhe@happiestminds.com)
 *
 * @created on: 4-Jan-2016
 */
public class LoginActivity extends BootstrapActivity implements TextView.OnEditorActionListener,
        SocialNetworkManager.OnInitializationCompleteListener,
        View.OnClickListener,OnLoginCompleteListener,
        OnRequestSocialPersonCompleteListener,
        OnRequestDetailedSocialPersonCompleteListener
        {

    //  SocialNetwork  InIt() classes
    protected SocialNetworkManager SNManager;
    protected boolean SNManagerInitialized = false;
    protected SocialNetwork socialNetwork;
    protected TwitterSocialNetwork twSN;
    protected FacebookSocialNetwork fbSN;
    protected GooglePlusSocialNetwork gpSN;

    private ImageButton btnGplus,btnTwitter,btnFacebok;
    private ProgressDialog pDialog;
    private SharedPreferences preferences;
    private Boolean button_state_twitter,button_state_facebook,button_state_gplus;
    EventBus bus;
    @Bind(R.id.et_email)EditText etUserEmail;
    @Bind(R.id.et_password)EditText etPassword;
    @Bind(R.id.btn_skip)Button btnSkip;
            @Bind(R.id.btn_forgotPassword)Button btnForgotPassword;
    @BindString(R.string.empty_username)String emptyUsername;
    @BindString(R.string.empty_password)String emptyPassword;
    @BindString(R.string.empty_username_password)String emptyUsernamePassword;
    @BindString(R.string.err_email)String invalidEmail;

            @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //check permission for MarshMallow
        if (ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.GET_ACCOUNTS)
              != PackageManager.PERMISSION_GRANTED) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
             android.Manifest.permission.GET_ACCOUNTS)) {
                //explains
         } else {
                //no explanation
             ActivityCompat.requestPermissions(this,
             new String[]{android.Manifest.permission.GET_ACCOUNTS},
             Constants.SNConstants.MY_PERMISSIONS_REQUEST_GET_ACCOUNTS);
            }
        }

        etPassword.setOnEditorActionListener(this);
        bus = EventBus.getDefault();
        Timber.i("Activity Created ");
        preferences=PreferenceManager.getDefaultSharedPreferences(this);
        button_state_twitter=preferences.getBoolean("login_button_twitter", false);
        button_state_facebook=preferences.getBoolean("login_button_facebook",false);
        button_state_gplus= preferences.getBoolean("login_button_gplus",false);
        pDialog=new ProgressDialog(this);
        SocialLoginInIt();
    }

        public void SocialLoginInIt(){
        pDialog=new ProgressDialog(this);
        btnGplus=(ImageButton)findViewById(R.id.btn_gplus);
        btnGplus.setOnClickListener(this);
        btnTwitter=(ImageButton)findViewById(R.id.btn_twitter);
        btnTwitter.setOnClickListener(this);
        btnFacebok=(ImageButton)findViewById(R.id.btn_facebook);
        btnFacebok.setOnClickListener(this);
        btnForgotPassword=(Button)findViewById(R.id.btn_forgotPassword);
        btnForgotPassword.setOnClickListener(this);

        if(button_state_twitter==true) {
            btnTwitter.setBackgroundResource(R.drawable.logout);
            btnTwitter.setOnClickListener(null);
        }
         if(button_state_facebook==true) {
             btnFacebok.setBackgroundResource(R.drawable.logout);
             btnFacebok.setOnClickListener(null);
         }
            if(button_state_gplus==true) {
                btnGplus.setBackgroundResource(R.drawable.logout);
                btnGplus.setOnClickListener(null);
            }


        if (SNManager == null) {
            ArrayList<String> fbScope = new ArrayList<String>();
            fbScope.addAll(Arrays.asList(Constants.SNConstants.FB_SCOPE));
            SNManager = new SocialNetworkManager();
            twSN = new TwitterSocialNetwork(this,Constants.SNConstants.TWITTER_CONSUMER_KEY,
                                                      Constants.SNConstants.TWITTER_CONSUMER_SECRET,
                                                      Constants.SNConstants.TWITTER_CALLBACK_URL);
            SNManager.addSocialNetwork(twSN);
            fbSN= new FacebookSocialNetwork(this, fbScope);
            SNManager.addSocialNetwork(fbSN);
            //gpSN=new GooglePlusSocialNetwork(this);
            //SNManager.addSocialNetwork(gpSN);
            SNManager.setOnInitializationCompleteListener(this);
        } else {
            SNManagerInitialized = true;
        }     
        
    }
    @OnClick({R.id.btn_signin, R.id.btn_forgotPassword,R.id.btn_gplus,R.id.btn_twitter,R.id.btn_facebook,
    R.id.btn_skip})
    public void handleButtonClick(Button button){
        UIUtils.hideSoftInput(this, etPassword);
        switch (button.getId()){
            case R.id.btn_signin:
                Timber.i("SignIn Button click");
                login();
                break;

            case R.id.btn_forgotPassword:
                Timber.i("Forgot Password");
                showForgotPasswordActivity();
                break;

            case R.id.btn_skip:
                showMainActivity();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        AppUtils.registerToDefaultBus(this, false);
    }

    @Override
    protected void onStop() {
        AppUtils.unregisterFromDefaultBus(this);

        super.onStop();
    }

    public void login(){
        int fieldCheck = emptyCheck();
        UIUtils.hideSoftInput(this, etPassword);
        Timber.i("Field Check value %d", fieldCheck);
        if(fieldCheck == Constants.FieldCheck.VALID) {
            showProgressDialog();
            LoginData loginData = new LoginData();
            loginData.setUsername(etUserEmail.getText().toString().trim());
            loginData.setPassword(etPassword.getText().toString().trim());
            Call<ApiResponse> call = apiService.login(loginData);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Response<ApiResponse> response) {
                    Timber.i("Login Successful");
                    showMainActivity();
                    hideProgressDialog();
                    bus.post("Login Successful");
                    //Post the creds as a sticky event so that the user can be automatically logged in later
                    bus.postSticky(new UserCredsEvent(true, etUserEmail.getText().toString()));
                }

                @Override
                public void onFailure(Throwable t) {
                    Timber.i("Login Failed");
                    hideProgressDialog();
                    bus.post("Login Failed");
                }
            });
        }else{
            switch (fieldCheck){
                case Constants.FieldCheck.BOTH_FIELDS_EMPTY:
                    etUserEmail.setError(emptyUsernamePassword);
                    etUserEmail.requestFocus();
                    break;

                case Constants.FieldCheck.USERNAME_FIELD_EMPTY:
                    etUserEmail.setError(emptyUsername);
                    etUserEmail.requestFocus();
                    break;

                case Constants.FieldCheck.PASSWORD_FIELD_EMPTY:
                    etPassword.setError(emptyPassword);
                    etPassword.requestFocus();
                    break;

                case Constants.FieldCheck.INVALID_EMAIL:
                    etUserEmail.setError(invalidEmail);
                    etUserEmail.requestFocus();
                    break;
            }
        }
    }

    private void showForgotPasswordActivity(){
        UIUtils.startActivity(this, ForgotPasswordActivity.class);
        this.finish();
    }

    private void showMainActivity(){
        UIUtils.startActivity(this, MainActivity.class);
        this.finish();
    }

    /**
     * Empty check for edit text views.
     *
     * @return true, if either username or password filed empty
     */
    private int emptyCheck() {
        int emptyCredential = Constants.FieldCheck.VALID;
        if (etUserEmail.length() == 0 || etPassword.length() == 0) {
            if (etUserEmail.length() == 0 && etPassword.length() == 0) {
                emptyCredential = Constants.FieldCheck.BOTH_FIELDS_EMPTY;
            } else if (etPassword.length() == 0) {
                emptyCredential = Constants.FieldCheck.PASSWORD_FIELD_EMPTY;
            } else {
                emptyCredential = Constants.FieldCheck.USERNAME_FIELD_EMPTY;
            }
        }

//        if (etUserEmail.length() > 0) {
//            if (!AppUtils.isEmailAddressValid(etUserEmail.getText().toString())) {
//                emptyCredential = Constants.FieldCheck.INVALID_EMAIL;
//            }
//        }

        return emptyCredential;
    }

    public void onEvent(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {

            Timber.i("Done button clicked");
            login();

            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        SharedPreferences.Editor editor=preferences.edit();
        switch (v.getId()){
            case R.id.btn_twitter:
                pDialog.setMessage("please wait..");
                pDialog.setCancelable(false);
                pDialog.show();
                socialNetwork = SNManager.getSocialNetwork(Constants.SNConstants.TWITTER);
                socialNetwork.requestLogin(this);
                editor.putBoolean("login_button_twitter", true);
                editor.commit();
                socialNetwork.logout();

                break;
            case R.id.btn_facebook:
                socialNetwork=SNManager.getSocialNetwork(Constants.SNConstants.FACEBOOK);
                 socialNetwork.requestLogin(this);
                 socialNetwork.logout();
                 editor.putBoolean("login_button_facebook",true);
                 editor.commit();
                break;
            case R.id.btn_gplus:
                socialNetwork = SNManager.getSocialNetwork(Constants.SNConstants.GPLUS);
                socialNetwork.requestLogin(this);
                editor.putBoolean("login_button_gplus",true);
                editor.commit();

                break;
        }
        
    }

    @Override
    public void onSocialNetworkManagerInitialized() {
        for (SocialNetwork socialNetwork : SNManager.getInitializedSocialNetworks()) {
            socialNetwork.setOnLoginCompleteListener(this);
            socialNetwork.setOnRequestCurrentPersonCompleteListener(this);
            socialNetwork.setOnRequestDetailedSocialPersonCompleteListener(this);
        }
    }

    @Override
    public void onLoginSuccess(int i) {
        pDialog.cancel();
        pDialog.setMessage("getting Info..");
        pDialog.setCancelable(false);
        pDialog.show();
        socialNetwork.requestCurrentPerson(this);
    }

    @Override
    public void onRequestDetailedSocialPersonSuccess(int i, SocialPerson socialPerson) {

    }

    @Override
    public void onRequestSocialPersonSuccess(int i, SocialPerson socialPerson) {
        if (socialPerson == null) {
            Log.d("Social Details NULL", "Null details");
            return;
        }
        if (getApplicationContext() == null) {
            Log.d("Activity is NULL", "Null!!!!!");
        }
       RegisterData.getInstance().setName(socialPerson.name);
       RegisterData.getInstance().setEmail(socialPerson.email);
       RegisterData.getInstance().setId(socialPerson.id);
       RegisterData.getInstance().setImage(socialPerson.avatarURL);
       RegisterData.getInstance().setAboutMe(socialPerson.profileURL);
       pDialog.dismiss();
       startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));

    }

    @Override
    public void onError(int i, String s, String s1, Object o) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SNManager.onActivityResult(requestCode, resultCode, data);
        //gpSN.onActivityResult(requestCode,resultCode,data);
    }

            @Override
            public void onRequestPermissionsResult(int requestCode,
                                                   String permissions[], int[] grantResults) {
                switch (requestCode) {
                    case Constants.SNConstants.MY_PERMISSIONS_REQUEST_GET_ACCOUNTS: {
                        // If request is cancelled, the result arrays are empty.
                        if (grantResults.length > 0
                                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(),"Permission Not Granted",Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                }
            }
}
