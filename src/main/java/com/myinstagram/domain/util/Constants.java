package com.myinstagram.domain.util;

import java.io.File;

public final class Constants {
    public static final File TEMP_USER = new File("src/main/resources/static/image/user/temp/profile.png");
    public static final String USER_FOLDER = "src/main/resources/static/image/user//";
    public static final String POST_FOLDER = "src/main/resources/static/image/post//";
    public static final String CREATE_POST_IMAGE_ERROR = "Error occurred. Photo not saved!";
    public static final String CREATE_POST_IMAGE_SUCCESS = "Photo saved successfully!";
    public static final String PICTURE_SAVED_TO_SERVER = "User picture saved in server!";
    public static final String PICTURE_SAVED = "User picture Saved!";

    private Constants() {
    }
}
