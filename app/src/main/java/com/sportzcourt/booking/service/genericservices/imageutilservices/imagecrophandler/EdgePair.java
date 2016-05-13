package com.sportzcourt.booking.service.genericservices.imageutilservices.imagecrophandler;

/**
 * Created by Barun.Gupta on 1/11/2016.
 */
public class EdgePair {
    // Member Variables ////////////////////////////////////////////////////////

    public Edge primary;

    public Edge secondary;

    // Constructor /////////////////////////////////////////////////////////////

    public EdgePair(Edge edge1, Edge edge2) {
        primary = edge1;
        secondary = edge2;
    }
}
