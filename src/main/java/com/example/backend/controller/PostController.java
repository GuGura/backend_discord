package com.example.backend.controller;

import com.example.backend.model.entity.Post;
import com.example.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${version}")
public class PostController {

    private final PostService postService;

    @PostMapping("/post/savePost")
    public void savePost(@RequestBody Map<String,String> params, HttpServletRequest request) throws Exception{
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        postService.saveNewPost(params, userUID);
    }

    @PostMapping("/post/listByPage")
    public List<Post> listTenByID(@RequestBody Map<String,String> params, HttpServletRequest request){
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        int pageNum=Integer.parseInt(params.get("lastPosting"));
        if(pageNum==0){
            pageNum = 100000;
        }
        return postService.listTenById(pageNum,userUID);
    }

}
