package com.myinstagram.service;

import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.entity.VerificationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static com.myinstagram.util.DataFixture.createUser;
import static com.myinstagram.util.DataFixture.createVerificationToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
public class VerificationTokenServiceDbTestSuite {
    @Autowired
    private UserServiceDb userServiceDb;
    @Autowired
    private VerificationTokenServiceDb verificationTokenServiceDb;

    @BeforeEach
    public void setUp() {
        //GIVEN
        User user = userServiceDb.saveUser(createUser("login_1", "email1@gmail.com"));
        User user1 = userServiceDb.saveUser(createUser("login_2", "email2@gmail.com"));
        verificationTokenServiceDb.saveVerificationToken(createVerificationToken(user, "123"));
        verificationTokenServiceDb.saveVerificationToken(createVerificationToken(user1, "423"));
    }

    @Test
    public void shouldGetAllVerificationTokens() {
        //WHEN
        List<VerificationToken> tokens = verificationTokenServiceDb.getAllVerificationTokens();
        //THEN
        assertEquals(2, tokens.size());
    }

    @Test
    public void shouldGetVerificationTokenByToken() {
        //WHEN
        VerificationToken token = verificationTokenServiceDb.getVerificationTokenByToken("123").get();
        //THEN
        assertEquals("login_1", token.getUser().getLogin());
        assertEquals("email1@gmail.com", token.getUser().getEmail());
    }

    @Test
    public void shouldGetUserValidVerificationToken() {
        //GIVEN
        User user = verificationTokenServiceDb.getVerificationTokenByToken("423").get().getUser();
        //WHEN
        List<VerificationToken> tokens = verificationTokenServiceDb.getUserValidVerificationToken(user);
        //THEN
        assertEquals(1, tokens.size());
        assertEquals(user, tokens.get(0).getUser());
    }

    @Test
    public void shouldSaveVerificationToken() {
        //GIVEN
        User user = userServiceDb.saveUser(createUser("login_3", "email3@gmail.com"));
        //WHEN
        VerificationToken token = verificationTokenServiceDb.saveVerificationToken(createVerificationToken(user, "666"));
        //THEN
        assertEquals(3, verificationTokenServiceDb.getAllVerificationTokens().size());
        assertNotNull(token.getId());
    }

    @Test
    public void shouldDeleteVerificationToken() {
        //GIVEN
        VerificationToken token = verificationTokenServiceDb.getVerificationTokenByToken("123").get();
        //WHEN
        verificationTokenServiceDb.deleteVerificationToken(token);
        //THEN
        assertEquals(1, verificationTokenServiceDb.getAllVerificationTokens().size());
    }
}
