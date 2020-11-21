package com.myinstagram.repository;

import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@Repository
public interface PostRepository {
    Post createPost(User user, HashMap<String, String> request, String postName);

    String createPostImage(MultipartFile multipartFile, String fileName);
}
