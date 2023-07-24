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
        String upload_date = params.get("upload_date");
        upload_date = upload_date.substring(0,10);
        params.put("upload_date",upload_date);
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

    @PostMapping("/post/deletePost")
    public void deletePost(@RequestBody Map<String,String> params, HttpServletRequest request){
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        int id = Integer.parseInt(params.get("id"));
        int scrapping_id = Integer.parseInt(params.get("scrapping_id"));
        postService.deleteMyPost(id,userUID,scrapping_id);
    }

    @PostMapping("/post/listByFeedPage")
    public List<Post> listTenByIDWithFriend(@RequestBody Map<String,String> params, HttpServletRequest request){
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        int pageNum=Integer.parseInt(params.get("lastPosting"));
        if(pageNum==0){
            pageNum = 100000;
        }
        return postService.listTenByIdWithFriend(pageNum,userUID);
    }

    @PostMapping("/post/scrappingPost")
    public void scrappingPost(@RequestBody Map<String,String> params, HttpServletRequest request){
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        int id = 0;
        if(params.get("scrapped").equals("1")){
            id = Integer.parseInt(params.get("scrapping_id"));
        }else {
            id = Integer.parseInt(params.get("id"));
        }
        postService.scrappingPost(id,userUID);

    }

}
