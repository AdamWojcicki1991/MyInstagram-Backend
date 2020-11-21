package com.myinstagram.service;

import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import com.myinstagram.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class PostService implements PostRepository {

    @Override
    public Post createPost(User user, HashMap<String, String> request, String postName) {
        return null;
    }

    @Override
    public String createPostImage(MultipartFile multipartFile, String fileName) {
        return null;
    }
}
