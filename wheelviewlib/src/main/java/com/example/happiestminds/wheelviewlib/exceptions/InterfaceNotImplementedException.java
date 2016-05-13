package com.example.happiestminds.wheelviewlib.exceptions;

import android.util.Log;

/**
 * Created by Narasimha.HS on 1/6/2016.
 *
 * Exception class indicating that the host activity has not implemented required interfaces
 */
public class InterfaceNotImplementedException extends Exception {

    public InterfaceNotImplementedException(String message){
        Log.d("COMPOSITE_WHEEL_VIEW", message);
    }
}
