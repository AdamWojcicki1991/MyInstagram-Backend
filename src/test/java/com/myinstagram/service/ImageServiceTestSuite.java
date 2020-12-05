package com.myinstagram.service;

import com.myinstagram.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.myinstagram.domain.util.Constants.*;
import static com.myinstagram.util.DataFixture.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest
public class ImageServiceTestSuite {
    @Autowired
    private ImageService imageService;

    @Test
    public void shouldLoadUserImage() throws IOException {
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
    public void shouldLoadDefaultUserImage() throws IOException {
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
    public void shouldLoadPostImage() throws IOException {
        //GIVEN
        MockMultipartFile multipartFile = new MockMultipartFile("Test file", new byte[0]);
        //WHEN
        String result = imageService.loadPostImage(multipartFile, "testFile");
        //THEN
        assertEquals(CREATE_POST_IMAGE_SUCCESS, result);
        //CLEANUP
        Files.deleteIfExists(Paths.get(POST_FOLDER + "/testFile.png"));
    }

    @Test
    public void shouldGetResponseIfImageUploaded() {
        //GIVEN
        String validTest = "Photo saved successfully!";
        //WHEN
        ResponseEntity<String> response = imageService.getResponseIfImageUploaded(validTest, CREATE_POST_IMAGE_SUCCESS);
        //THEN
        assertEquals(new ResponseEntity<>("Photo saved successfully!", OK), response);
    }

    @Test
    public void shouldGetResponseBadRequestIfImageUploadFailed() {
        //GIVEN
        String validTest = "Not string value that we have in constant!";
        //WHEN
        ResponseEntity<String> response = imageService.getResponseIfImageUploaded(validTest, CREATE_POST_IMAGE_ERROR);
        //THEN
        assertEquals(new ResponseEntity<>("Not string value that we have in constant!", BAD_REQUEST), response);
    }
}
