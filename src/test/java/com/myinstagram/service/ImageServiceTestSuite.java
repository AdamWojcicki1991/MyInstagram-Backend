package com.myinstagram.service;

import com.myinstagram.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.myinstagram.domain.util.Constants.*;
import static com.myinstagram.util.DataFixture.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ImageServiceTestSuite {
    @Autowired
    private ImageService imageService;

    @Test
    void shouldLoadUserImage() throws IOException {
        //GIVEN
        MockMultipartFile multipartFile = new MockMultipartFile("Test file", new byte[0]);
        //WHEN
        String result = imageService.loadUserImage(multipartFile, 1L);
        //THEN
        assertEquals(PICTURE_SAVED_TO_SERVER, result);
        //CLEANUP
        Files.deleteIfExists(Paths.get(USER_FOLDER + "/1.png"));
    }

    @Test
    void shouldLoadDefaultUserImage() throws IOException {
        //GIVEN
        User user = createUser("testLogin", "test@gmail.com");
        System.out.println(user.getId());
        //WHEN
        String result = imageService.loadDefaultUserImage(user);
        //THEN
        assertEquals(DEFAULT_PICTURE_SAVED, result);
        //CLEANUP
        Files.deleteIfExists(Paths.get(USER_FOLDER + "/" + user.getId() + ".png"));
    }

    @Test
    void shouldLoadPostImage() throws IOException {
        //GIVEN
        MockMultipartFile multipartFile = new MockMultipartFile("Test file", new byte[0]);
        //WHEN
        String result = imageService.loadPostImage(multipartFile, "testFile");
        //THEN
        assertEquals(CREATE_POST_IMAGE_SUCCESS, result);
        //CLEANUP
        Files.deleteIfExists(Paths.get(POST_FOLDER + "/testFile.png"));
    }
}
