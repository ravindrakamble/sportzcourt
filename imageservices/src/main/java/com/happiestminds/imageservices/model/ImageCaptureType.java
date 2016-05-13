package com.happiestminds.imageservices.model;

/**
 * Copyright 2016 (C) Happiest Minds Pvt Ltd..
 *
 * <P> Enum to hold the tpye of image need to be captured from device camera/gallery etc
 *
 * <P>Notes:
 * <P>Dependency:
 *
 * @authors Venkatesh Guddanti (Venkatesh.Guddanti@happiestminds.com)
 *
 *
 * @created on: 4-Jan-2016
 */
public enum ImageCaptureType {

    CAPTURE_BY_CAMERA(0),CAPTURE_BY_CAMERA_WITHCROP(1),CAPTURE_BY_GALLERY(3),CAPTURE_BY_GALLERY_WITHCROP(4),CAPTURE_BY_CAMERA_ORIGINAL(5);

    private final int typeCode;

    ImageCaptureType(int typeCode)
    {
        this.typeCode = typeCode;
    }

    public int getImageCaptureType()
    {
        return this.typeCode;
    }
}
