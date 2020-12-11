package com.myinstagram.controller;

import com.google.gson.Gson;
import com.myinstagram.domain.dto.CommentDto;
import com.myinstagram.domain.dto.CommentRequest;
import com.myinstagram.domain.dto.UpdateCommentRequest;
import com.myinstagram.facade.comment.CommentFacade;
import com.myinstagram.security.jwt.JwtProvider;
import com.myinstagram.security.service.AuthenticationService;
import com.myinstagram.security.service.CustomUserDetailsService;
import com.myinstagram.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.myinstagram.util.DtoDataFixture.*;
import static com.myinstagram.util.EntityDataFixture.*;
import static com.myinstagram.util.RequestDataFixture.createCommentRequest;
import static com.myinstagram.util.RequestDataFixture.createUpdateCommentRequest;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(username = "user", password = "password")
@WebMvcTest(CommentController.class)
public class CommentControllerTestSuite {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private CommentFacade commentFacade;
    @MockBean
    private RefreshTokenService refreshTokenService;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("/comments | GET")
    public void shouldGetComments() throws Exception {
        //GIVEN
        CommentDto firstCommentDto = createCommentDto(createPostDto(createUserDto(
                "login_1", "test1@gmail.com"), now().truncatedTo(SECONDS)))
                .toBuilder().id(1L).postId(1L).userId(1L).build();
        CommentDto secondCommentDto = createCommentDto(createPostDto(createUserDto(
                "login_2", "test2@gmail.com"), now().truncatedTo(SECONDS)))
                .toBuilder().id(2L).postId(2L).userId(2L).build();
        CommentDto thirdCommentDto = createCommentDto(createPostDto(createUserDto(
                "login_3", "test3@gmail.com"), now().truncatedTo(SECONDS)))
                .toBuilder().id(3L).postId(3L).userId(3L).build();
        ResponseEntity<List<CommentDto>> responseEntities = new ResponseEntity<>(
                List.of(firstCommentDto, secondCommentDto, thirdCommentDto), OK);
        given(commentFacade.getComments()).willReturn(responseEntities);
        //WHEN & THEN
        mockMvc.perform(get("/comments"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].commentName", is("Comment")))
                .andExpect(jsonPath("$[0].content", is("Content")))
                .andExpect(jsonPath("$[0].commentDate", is(firstCommentDto.getCommentDate().toString())))
                .andExpect(jsonPath("$[0].updateDate", is(firstCommentDto.getUpdateDate().toString())))
                .andExpect(jsonPath("$[0].postId", is(1)))
                .andExpect(jsonPath("$[0].postName", is("Post")))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].commentName", is("Comment")))
                .andExpect(jsonPath("$[1].content", is("Content")))
                .andExpect(jsonPath("$[1].commentDate", is(secondCommentDto.getCommentDate().toString())))
                .andExpect(jsonPath("$[1].updateDate", is(secondCommentDto.getUpdateDate().toString())))
                .andExpect(jsonPath("$[1].postId", is(2)))
                .andExpect(jsonPath("$[1].postName", is("Post")))
                .andExpect(jsonPath("$[1].userId", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].commentName", is("Comment")))
                .andExpect(jsonPath("$[2].content", is("Content")))
                .andExpect(jsonPath("$[2].commentDate", is(thirdCommentDto.getCommentDate().toString())))
                .andExpect(jsonPath("$[2].updateDate", is(thirdCommentDto.getUpdateDate().toString())))
                .andExpect(jsonPath("$[2].postId", is(3)))
                .andExpect(jsonPath("$[2].postName", is("Post")))
                .andExpect(jsonPath("$[2].userId", is(3)));
    }

    @Test
    @DisplayName("/comments | GET")
    public void shouldGetEmptyComments() throws Exception {
        //GIVEN
        ResponseEntity<List<CommentDto>> responseEntities = new ResponseEntity<>(List.of(), OK);
        given(commentFacade.getComments()).willReturn(responseEntities);
        //WHEN & THEN
        mockMvc.perform(get("/comments"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("/comments/{id} | GET")
    public void shouldGetComment() throws Exception {
        //GIVEN
        CommentDto commentDto = createCommentDto(createPostDto(createUserDto(
                "login_1", "test1@gmail.com"), now().truncatedTo(SECONDS)))
                .toBuilder().id(1L).postId(1L).userId(1L).build();
        ResponseEntity<CommentDto> responseEntity = new ResponseEntity<>(commentDto, OK);
        given(commentFacade.getComment(1L)).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(get("/comments/1"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.commentName", is("Comment")))
                .andExpect(jsonPath("$.content", is("Content")))
                .andExpect(jsonPath("$.commentDate", is(commentDto.getCommentDate().toString())))
                .andExpect(jsonPath("$.updateDate", is(commentDto.getUpdateDate().toString())))
                .andExpect(jsonPath("$.postId", is(1)))
                .andExpect(jsonPath("$.postName", is("Post")))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    @DisplayName("/comments | POST")
    public void shouldPublishComment() throws Exception {
        //GIVEN
        CommentRequest commentRequest = createCommentRequest().toBuilder().postId(2L).build();
        CommentDto commentDto = createCommentDto(createPostDto(createUserDto(
                "login_1", "test1@gmail.com"), now().truncatedTo(SECONDS))).toBuilder()
                .commentName(commentRequest.getLogin())
                .content(commentRequest.getContent())
                .postId(commentRequest.getPostId())
                .build();
        ResponseEntity<CommentDto> responseEntity = new ResponseEntity<>(commentDto, OK);
        String jsonContent = gson.toJson(commentRequest);
        given(commentFacade.publishComment(commentRequest)).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(post("/comments")
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8")
                                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.commentName", is("Test Comment")))
                .andExpect(jsonPath("$.content", is("Test Content")))
                .andExpect(jsonPath("$.commentDate", is(commentDto.getCommentDate().toString())))
                .andExpect(jsonPath("$.updateDate", is(commentDto.getUpdateDate().toString())))
                .andExpect(jsonPath("$.postId", is(2)))
                .andExpect(jsonPath("$.postName", is("Post")))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    @DisplayName("/comments | PUT")
    public void shouldUpdateComment() throws Exception {
        //GIVEN
        UpdateCommentRequest updateCommentRequest = createUpdateCommentRequest(
                createComment(
                        createPost(
                                createUser("login", "test@gmail.com"),
                                now().truncatedTo(SECONDS))).toBuilder().id(2L).build());
        CommentDto commentDto = createCommentDto(createPostDto(createUserDto(
                "login_1", "test1@gmail.com"), now().truncatedTo(SECONDS))).toBuilder()
                .id(updateCommentRequest.getCommentId())
                .commentName(updateCommentRequest.getCommentName())
                .content(updateCommentRequest.getContent())
                .build();
        ResponseEntity<CommentDto> responseEntity = new ResponseEntity<>(commentDto, OK);
        String jsonContent = gson.toJson(updateCommentRequest);
        given(commentFacade.updateComment(updateCommentRequest)).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(put("/comments")
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8")
                                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.commentName", is("Test Comment")))
                .andExpect(jsonPath("$.content", is("Test Content")))
                .andExpect(jsonPath("$.commentDate", is(commentDto.getCommentDate().toString())))
                .andExpect(jsonPath("$.updateDate", is(commentDto.getUpdateDate().toString())))
                .andExpect(jsonPath("$.postId", is(1)))
                .andExpect(jsonPath("$.postName", is("Post")))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    @DisplayName("/comments/{id} | DELETE")
    public void shouldDeleteCommentById() throws Exception {
        //GIVEN
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Comment Deleted Successfully!", OK);
        given(commentFacade.deleteCommentById(1L)).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(delete("/comments/1"))
                .andExpect(status().is(200))
                .andExpect(content().string("Comment Deleted Successfully!"));
    }
}
