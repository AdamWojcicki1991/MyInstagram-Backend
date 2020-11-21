package com.myinstagram.service;

import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.util.Constants;
import com.myinstagram.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static com.myinstagram.domain.util.Constants.CREATE_POST_IMAGE_ERROR;
import static com.myinstagram.domain.util.Constants.CREATE_POST_IMAGE_SUCCESS;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostService implements PostRepository {
    private final PostServiceDb postServiceDb;

    @Override
    public Post createPost(User user, HashMap<String, String> request, String postName) {
        return postServiceDb.savePost(Post.builder()
                                              .postName(postName)
                                              .caption(request.get("caption"))
                                              .url(request.get("url"))
                                              .imageSerialNumber(user.getId())
                                              .likesCount(0L)
                                              .postDate(LocalDate.now())
                                              .user(user)
                                              .comments(new ArrayList<>())
                                              .build());
    }

    @Override
    public String createPostImage(MultipartFile multipartFile, String fileName) {
        try {
            Path path = Paths.get(Constants.POST_FOLDER + fileName + ".png");
            Files.write(path, multipartFile.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            log.error(CREATE_POST_IMAGE_ERROR);
            return CREATE_POST_IMAGE_ERROR;
        }
        log.info(CREATE_POST_IMAGE_SUCCESS);
        return CREATE_POST_IMAGE_SUCCESS;
    }
}
