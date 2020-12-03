package com.myinstagram.service;

import com.myinstagram.domain.dto.PostRequest;
import com.myinstagram.domain.dto.UpdatePostRequest;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {
    private final PostServiceDb postServiceDb;

    public Post createPost(final User user, final PostRequest postRequest) {
        return postServiceDb.savePost(Post.builder()
                                              .postName(postRequest.getPostName())
                                              .caption(postRequest.getCaption())
                                              .url(postRequest.getUrl())
                                              .imageSerialNumber(user.getId())
                                              .likesCount(0L)
                                              .postDate(Instant.now())
                                              .user(user)
                                              .comments(new ArrayList<>())
                                              .build());
    }

    public Post updatePost(final Post post, final UpdatePostRequest updatePostRequest) {
        return postServiceDb.savePost(post.toBuilder()
                                              .postName(updatePostRequest.getPostName())
                                              .caption(updatePostRequest.getCaption())
                                              .updateDate(Instant.now())
                                              .build());
    }
}
