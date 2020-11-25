package com.myinstagram.service;

import com.myinstagram.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static com.myinstagram.domain.util.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    public String loadUserImage(final MultipartFile multipartFile, final Long userImageId) {
        try {
            Files.deleteIfExists(Paths.get(USER_FOLDER + "/" + userImageId + ".png"));
            Path path = Paths.get(USER_FOLDER + userImageId + ".png");
            Files.write(path, multipartFile.getBytes());
        } catch (IOException e) {
            log.error(PICTURE_SAVED + " " + e);
            return PICTURE_SAVED;
        }
        log.info(PICTURE_SAVED_TO_SERVER);
        return PICTURE_SAVED_TO_SERVER;
    }

    public String loadDefaultUserImage(final User user) {
        try {
            byte[] bytes = Files.readAllBytes(TEMP_USER.toPath());
            Path path = Paths.get(USER_FOLDER + user.getId() + ".png");
            Files.write(path, bytes);
        } catch (IOException e) {
            log.error(DEFAULT_PICTURE_SET_ERROR + " " + e);
            return DEFAULT_PICTURE_SET_ERROR;
        }
        log.info(DEFAULT_PICTURE_SAVED);
        return DEFAULT_PICTURE_SAVED;
    }

    public String loadPostImage(final MultipartFile multipartFile, final String fileName) {
        try {
            Path path = Paths.get(POST_FOLDER + fileName + ".png");
            Files.write(path, multipartFile.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            log.error(CREATE_POST_IMAGE_ERROR + " " + e);
            return CREATE_POST_IMAGE_ERROR;
        }
        log.info(CREATE_POST_IMAGE_SUCCESS);
        return CREATE_POST_IMAGE_SUCCESS;
    }
}
