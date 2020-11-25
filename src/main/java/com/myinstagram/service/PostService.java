package com.myinstagram.service;

import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostService {
    private final PostServiceDb postServiceDb;

    public Post createPost(final User user, final HashMap<String, String> request, final String postName) {
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
}
