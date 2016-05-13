package com.sportzcourt.booking.model.events;

/**
 * Created by Narasimha.HS on 12/7/2015.
 *
 * To be posted as a sticky event after successful login. The user will land at Main Screen if he is already logged in.
 * If the user clears the app from Recent Apps Screen/app's process is killed by the system, event will be cleared away
 * and the user will land at the Login Screen when the app is opened later.
 */
public class UserCredsEvent {

    public final boolean loggedIn;
    public final String userName;

    public UserCredsEvent(boolean loggedIn, String userName){
        this.loggedIn = loggedIn;
        this.userName = userName;
    }
}
