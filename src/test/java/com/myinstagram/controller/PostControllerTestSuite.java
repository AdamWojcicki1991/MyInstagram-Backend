package com.myinstagram.controller;

import com.google.gson.Gson;
import com.myinstagram.domain.dto.PostDto;
import com.myinstagram.domain.dto.PostRequest;
import com.myinstagram.domain.dto.SimplePostRequest;
import com.myinstagram.domain.dto.UpdatePostRequest;
import com.myinstagram.facade.post.PostFacade;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.myinstagram.domain.util.Constants.CREATE_POST_IMAGE_SUCCESS;
import static com.myinstagram.util.DtoDataFixture.createPostDto;
import static com.myinstagram.util.DtoDataFixture.createUserDto;
import static com.myinstagram.util.RequestDataFixture.*;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
public class PostControllerTestSuite {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private PostFacade postFacade;
    @MockBean
    private RefreshTokenService refreshTokenService;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("/posts | GET")
    public void shouldGetPosts() throws Exception {
        //GIVEN
        PostDto firstPostDto = createPostDto(createUserDto("login1", "test1@gmail.com"),
                                             now().truncatedTo(SECONDS)).toBuilder().id(1L).build();
        PostDto secondPostDto = createPostDto(createUserDto("login2", "test2@gmail.com"),
                                              now().truncatedTo(SECONDS)).toBuilder().id(2L).build();
        PostDto thirdPostDto = createPostDto(createUserDto("login3", "test3@gmail.com"),
                                             now().truncatedTo(SECONDS)).toBuilder().id(3L).build();
        ResponseEntity<List<PostDto>> responseEntities = new ResponseEntity<>(
                List.of(firstPostDto, secondPostDto, thirdPostDto), OK);
        given(postFacade.getPosts()).willReturn(responseEntities);
        //WHEN & THEN
        mockMvc.perform(get("/posts"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].postName", is("Post")))
                .andExpect(jsonPath("$[0].caption", is("Sign")))
                .andExpect(jsonPath("$[0].url", is("URL")))
                .andExpect(jsonPath("$[0].imageSerialNumber", is(0)))
                .andExpect(jsonPath("$[0].likesCount", is(0)))
                .andExpect(jsonPath("$[0].postDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$[0].updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].login", is("login")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].postName", is("Post")))
                .andExpect(jsonPath("$[1].caption", is("Sign")))
                .andExpect(jsonPath("$[1].url", is("URL")))
                .andExpect(jsonPath("$[1].imageSerialNumber", is(0)))
                .andExpect(jsonPath("$[1].likesCount", is(0)))
                .andExpect(jsonPath("$[1].postDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$[1].updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$[1].userId", is(1)))
                .andExpect(jsonPath("$[1].login", is("login")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].postName", is("Post")))
                .andExpect(jsonPath("$[2].caption", is("Sign")))
                .andExpect(jsonPath("$[2].url", is("URL")))
                .andExpect(jsonPath("$[2].imageSerialNumber", is(0)))
                .andExpect(jsonPath("$[2].likesCount", is(0)))
                .andExpect(jsonPath("$[2].postDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$[2].updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$[2].userId", is(1)))
                .andExpect(jsonPath("$[2].login", is("login")));
    }

    @Test
    @DisplayName("/posts | GET")
    public void shouldGetEmptyPosts() throws Exception {
        //GIVEN
        ResponseEntity<List<PostDto>> responseEntities = new ResponseEntity<>(List.of(), OK);
        given(postFacade.getPosts()).willReturn(responseEntities);
        //WHEN & THEN
        mockMvc.perform(get("/posts"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("/posts/{login} | GET")
    public void shouldGetPostsByLogin() throws Exception {
        //GIVEN
        PostDto firstPostDto = createPostDto(createUserDto("login1", "test1@gmail.com"),
                                             now().truncatedTo(SECONDS)).toBuilder().id(1L).build();
        PostDto secondPostDto = createPostDto(createUserDto("login2", "test2@gmail.com"),
                                              now().truncatedTo(SECONDS)).toBuilder().id(2L).build();
        PostDto thirdPostDto = createPostDto(createUserDto("login3", "test3@gmail.com"),
                                             now().truncatedTo(SECONDS)).toBuilder().id(3L).build();
        ResponseEntity<List<PostDto>> responseEntities = new ResponseEntity<>(List.of(firstPostDto), OK);
        given(postFacade.getPostsByLogin("login1")).willReturn(responseEntities);
        //WHEN & THEN
        mockMvc.perform(get("/posts/login1"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].postName", is("Post")))
                .andExpect(jsonPath("$[0].caption", is("Sign")))
                .andExpect(jsonPath("$[0].url", is("URL")))
                .andExpect(jsonPath("$[0].imageSerialNumber", is(0)))
                .andExpect(jsonPath("$[0].likesCount", is(0)))
                .andExpect(jsonPath("$[0].postDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$[0].updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].login", is("login")));
    }

    @Test
    @DisplayName("/posts/{login} | GET")
    public void shouldGetEmptyPostsByLogin() throws Exception {
        //GIVEN
        ResponseEntity<List<PostDto>> responseEntities = new ResponseEntity<>(List.of(), OK);
        given(postFacade.getPostsByLogin("login")).willReturn(responseEntities);
        //WHEN & THEN
        mockMvc.perform(get("/posts/login"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("/posts/id/{id} | GET")
    public void shouldGetPostById() throws Exception {
        //GIVEN
        PostDto postDto = createPostDto(createUserDto("login", "mail"),
                                        now().truncatedTo(SECONDS)).toBuilder().id(1L).build();
        ResponseEntity<PostDto> responseEntity = new ResponseEntity<>(postDto, OK);
        given(postFacade.getPostById(1L)).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(get("/posts/id/1"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.postName", is("Post")))
                .andExpect(jsonPath("$.caption", is("Sign")))
                .andExpect(jsonPath("$.url", is("URL")))
                .andExpect(jsonPath("$.imageSerialNumber", is(0)))
                .andExpect(jsonPath("$.likesCount", is(0)))
                .andExpect(jsonPath("$.postDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.login", is("login")));
    }

    @Test
    @DisplayName("/posts | POST")
    public void shouldPublishPost() throws Exception {
        //GIVEN
        PostRequest postRequest = createPostRequest("Name", "Caption");
        PostDto postDto = createPostDto(createUserDto("login", "mail"),
                                        now().truncatedTo(SECONDS)).toBuilder()
                .id(1L)
                .postName(postRequest.getPostName())
                .caption(postRequest.getCaption())
                .url(postRequest.getUrl())
                .build();
        ResponseEntity<PostDto> responseEntity = new ResponseEntity<>(postDto, OK);
        String jsonContent = gson.toJson(postRequest);
        given(postFacade.publishPost(postRequest)).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(post("/posts")
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8")
                                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.postName", is("Name")))
                .andExpect(jsonPath("$.caption", is("Caption")))
                .andExpect(jsonPath("$.url", is("Test url")))
                .andExpect(jsonPath("$.imageSerialNumber", is(0)))
                .andExpect(jsonPath("$.likesCount", is(0)))
                .andExpect(jsonPath("$.postDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.login", is("login")));
    }

    @Test
    @DisplayName("/posts/upload/{postImageName} | POST")
    public void shouldUploadPostImage() throws Exception {
        //GIVEN
        MockMultipartFile multipartFile = new MockMultipartFile("image",
                                                                "hello.txt",
                                                                APPLICATION_JSON_VALUE,
                                                                "MyInstagram!".getBytes());
        ResponseEntity<String> responseEntity = new ResponseEntity<>(CREATE_POST_IMAGE_SUCCESS, OK);
        given(postFacade.uploadPostImage(multipartFile, "image")).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(multipart("/posts/upload/image")
                                .file(multipartFile)
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8"))
                .andExpect(status().is(200))
                .andExpect(content().string(CREATE_POST_IMAGE_SUCCESS));
    }

    @Test
    @DisplayName("/posts | PUT")
    public void shouldUpdatePost() throws Exception {
        //GIVEN
        UpdatePostRequest updatePostRequest = createUpdatePostRequest();
        PostDto postDto = createPostDto(createUserDto("login", "mail"),
                                        now().truncatedTo(SECONDS)).toBuilder()
                .id(1L)
                .postName(updatePostRequest.getPostName())
                .caption(updatePostRequest.getCaption())
                .build();
        ResponseEntity<PostDto> responseEntity = new ResponseEntity<>(postDto, OK);
        String jsonContent = gson.toJson(updatePostRequest);
        given(postFacade.updatePost(updatePostRequest)).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(put("/posts")
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8")
                                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.postName", is("Test Post")))
                .andExpect(jsonPath("$.caption", is("Test caption")))
                .andExpect(jsonPath("$.url", is("URL")))
                .andExpect(jsonPath("$.imageSerialNumber", is(0)))
                .andExpect(jsonPath("$.likesCount", is(0)))
                .andExpect(jsonPath("$.postDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.login", is("login")));
    }

    @Test
    @DisplayName("/posts/like | PUT")
    public void shouldLikePost() throws Exception {
        //GIVEN
        SimplePostRequest simplePostRequest = createSimplePostRequest();
        PostDto postDto = createPostDto(createUserDto("login", "mail"),
                                        now().truncatedTo(SECONDS)).toBuilder().id(1L).likesCount(1L).build();
        ResponseEntity<PostDto> responseEntity = new ResponseEntity<>(postDto, OK);
        String jsonContent = gson.toJson(simplePostRequest);
        given(postFacade.likePost(simplePostRequest)).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(put("/posts/like")
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8")
                                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.postName", is("Post")))
                .andExpect(jsonPath("$.caption", is("Sign")))
                .andExpect(jsonPath("$.url", is("URL")))
                .andExpect(jsonPath("$.imageSerialNumber", is(0)))
                .andExpect(jsonPath("$.likesCount", is(1)))
                .andExpect(jsonPath("$.postDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.login", is("login")));
    }

    @Test
    @DisplayName("/posts/unlike | PUT")
    public void shouldUnlikePost() throws Exception {
        //GIVEN
        SimplePostRequest simplePostRequest = createSimplePostRequest();
        PostDto postDto = createPostDto(createUserDto("login", "mail"),
                                        now().truncatedTo(SECONDS)).toBuilder().id(1L).likesCount(0L).build();
        ResponseEntity<PostDto> responseEntity = new ResponseEntity<>(postDto, OK);
        String jsonContent = gson.toJson(simplePostRequest);
        given(postFacade.unlikePost(simplePostRequest)).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(put("/posts/unlike")
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8")
                                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.postName", is("Post")))
                .andExpect(jsonPath("$.caption", is("Sign")))
                .andExpect(jsonPath("$.url", is("URL")))
                .andExpect(jsonPath("$.imageSerialNumber", is(0)))
                .andExpect(jsonPath("$.likesCount", is(0)))
                .andExpect(jsonPath("$.postDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.login", is("login")));
    }

    @Test
    @DisplayName("/posts/{id} | DELETE")
    public void shouldDeletePost() throws Exception {
        //GIVEN
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Post Deleted Successfully!", OK);
        given(postFacade.deletePostById(1L)).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(delete("/posts/1"))
                .andExpect(status().is(200))
                .andExpect(content().string("Post Deleted Successfully!"));
    }
}
