package com.happiestminds.imageservices.model;

/**
 * Copyright 2016 (C) Happiest Minds Pvt Ltd..
 *
 * <P> Enum to hold the tpye of Video need to be captured from device camera/gallery etc
 *
 * <P>Notes:
 * <P>Dependency:
 *
 * @authors Venkatesh Guddanti (Venkatesh.Guddanti@happiestminds.com)
 *
 *
 * @created on: 4-Jan-2016
 */
public enum VideoCaptureType {

    CAPTURE_BY_CAMERA(0),CAPTURE_BY_GALLERY(1),CAPTURE_WITH_COMPRESS(2),CAPTURE_BY_DOWNLOAD(3);

    private final int typeCode;

    VideoCaptureType(int typeCode)
    {
        this.typeCode = typeCode;
    }

    public int getVideoCaptureType()
    {
        return this.typeCode;
    }
}
