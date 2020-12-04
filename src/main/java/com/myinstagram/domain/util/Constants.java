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
    public static final String DEFAULT_PICTURE_SAVED = "User default picture Saved!";
    public static final String DEFAULT_PICTURE_SET_ERROR = "User default picture is not set!";
    public static final String CREATE_UUID_SUCCESS = "UUID was successfully generated!";
    public static final String ENCRYPT_PASSWORD_SUCCESS = "Password was successfully encrypted!";
    public static final String ENCRYPT_PASSWORD_MATCHES = "Try to validate that encrypted passwords is matching!";
    public static final String NEW_USER_EMAIL = "MyInstagram - New User Created";
    public static final String UPDATE_USER_EMAIL = "MyInstagram - Update User Profile";
    public static final String WEATHER_EMAIL = "MyInstagram - Whether email";
    public static final String UPDATE_USER_PASSWORD_EMAIL = "MyInstagram - Update User Password";
    public static final String RESET_USER_PASSWORD_EMAIL = "MyInstagram - Reset User Password";

    private Constants() {
    }
}
