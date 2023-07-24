package com.example.backend.service;

import com.example.backend.mapper.PostMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.entity.Post;
import com.example.backend.util.ConvenienceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private final UserMapper userMapper;

    public void saveNewPost(Map<String, String> params, int userUID)throws IOException {
        boolean is_img_in;
        if(params.get("is_img_in").equals("true")){
            is_img_in = true;
        }else {
            is_img_in = false;
        }
        if (!params.get("post_img_url").equals("none")) {
            String base64 = params.get("post_img_url").substring(params.get("post_img_url").lastIndexOf(",") + 1);
            String fileName = base64.substring(30, 50) + ".png";
            String folderPath = ConvenienceUtil.makeOrGetLobbyFolderURL(userUID);
            Path imgPath = Paths.get(folderPath, fileName);
            createImg(base64, imgPath);
            params.put("post_img_url", imgPath.toString());
            }postMapper.saveNewPost(params,userUID,is_img_in);

        }

    @Async("File")
    public void createImg(String base64,Path imgPath) throws IOException{
        BufferedImage image = ConvenienceUtil.base64DecoderToImg(base64);
        ImageIO.write(image, "png", imgPath.toFile());
    }

    public List<Post> listTenById(int pageNum,int userUID) {
        List<Post> posts = postMapper.listTenById(pageNum, userUID);
        for (Post post : posts) {
            if (!post.is_post_scrapped()&&userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).isPresent()) {
                post.setUserName(userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getNickname());
                if (!userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getIcon_url().equals("")) {
                    post.setUserIcon(userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getIcon_url());
                    String imgURL = userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getIcon_url().substring(ConvenienceUtil.getImgPath().length());
                    imgURL = imgURL.replace("\\", "/");
                    post.setUserIcon(imgURL);
                }
            } else if(userMapper.findUserBasicInfoByUserUID(post.getOriginal_writer()).isPresent()){
                post.setIs_post_scrapped_int(1);
                post.setPost_owner_name(userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getNickname());
                post.setUserName(userMapper.findUserBasicInfoByUserUID(post.getOriginal_writer()).get().getNickname());
                if (!userMapper.findUserBasicInfoByUserUID(post.getOriginal_writer()).get().getIcon_url().equals("")) {
                    post.setUserIcon(userMapper.findUserBasicInfoByUserUID(post.getOriginal_writer()).get().getIcon_url());
                    String imgURL = userMapper.findUserBasicInfoByUserUID(post.getOriginal_writer()).get().getIcon_url().substring(ConvenienceUtil.getImgPath().length());
                    imgURL = imgURL.replace("\\", "/");
                    post.setUserIcon(imgURL);
                }
                post.setPost_owner_name(userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getNickname());
            }
            if (!post.getPost_img_url().equals("none")) {
                String imgURL = post.getPost_img_url().substring(ConvenienceUtil.getImgPath().length());
                imgURL = imgURL.replace("\\", "/");
                post.setPost_img_url(imgURL);
            }
            post.setIs_post_mine(1);

        }
        return posts;
    }

    public List<Post> listTenByIdFriend(int pageNum, int id, int userUID) {
        List<Post> posts = postMapper.listTenById(pageNum, id);
        for (Post post : posts) {
            if (!post.is_post_scrapped()&&userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).isPresent()) {
                post.setUserName(userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getNickname());
                if (!userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getIcon_url().equals("")) {
                    post.setUserIcon(userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getIcon_url());
                    String imgURL = userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getIcon_url().substring(ConvenienceUtil.getImgPath().length());
                    imgURL = imgURL.replace("\\", "/");
                    post.setUserIcon(imgURL);
                }
            } else if(userMapper.findUserBasicInfoByUserUID(post.getOriginal_writer()).isPresent()){
                post.setIs_post_scrapped_int(1);
                post.setPost_owner_name(userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getNickname());
                post.setUserName(userMapper.findUserBasicInfoByUserUID(post.getOriginal_writer()).get().getNickname());
                if (!userMapper.findUserBasicInfoByUserUID(post.getOriginal_writer()).get().getIcon_url().equals("")) {
                    post.setUserIcon(userMapper.findUserBasicInfoByUserUID(post.getOriginal_writer()).get().getIcon_url());
                    String imgURL = userMapper.findUserBasicInfoByUserUID(post.getOriginal_writer()).get().getIcon_url().substring(ConvenienceUtil.getImgPath().length());
                    imgURL = imgURL.replace("\\", "/");
                    post.setUserIcon(imgURL);
                }
                post.setPost_owner_name(userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getNickname());
            }
            if (!post.getPost_img_url().equals("none")) {
                String imgURL = post.getPost_img_url().substring(ConvenienceUtil.getImgPath().length());
                imgURL = imgURL.replace("\\", "/");
                post.setPost_img_url(imgURL);
            }
            if(post.getOriginal_writer() == userUID){
                post.setIs_post_mine(1);
            }
        }
        return posts;
    }


    public void deleteMyPost(int id, int userUID, int scrapping_id) {
        postMapper.deleteMyPost(id,userUID,scrapping_id);
    }

    public List<Post> listTenByIdWithFriend(int pageNum, int userUID){
        List<Post> posts = postMapper.listTenByIdWithFriend(pageNum, userUID);
        for (Post post : posts) {
            if (!post.is_post_scrapped()&&userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).isPresent()) {
                if(post.getPost_owner_id() == userUID){
                    post.setIs_post_mine(1);
                }
                post.setUserName(userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getNickname());
                if (!userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getIcon_url().equals("")) {
                    post.setUserIcon(userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getIcon_url());
                    String imgURL = userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getIcon_url().substring(ConvenienceUtil.getImgPath().length());
                    imgURL = imgURL.replace("\\", "/");
                    post.setUserIcon(imgURL);
                }
            } else if(userMapper.findUserBasicInfoByUserUID(post.getOriginal_writer()).isPresent()){
                if(post.getOriginal_writer() == userUID || post.getPost_owner_id() == userUID){
                    post.setIs_post_mine(1);
                }
                post.setIs_post_scrapped_int(1);
                post.setPost_owner_name(userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getNickname());
                post.setUserName(userMapper.findUserBasicInfoByUserUID(post.getOriginal_writer()).get().getNickname());
                if (!userMapper.findUserBasicInfoByUserUID(post.getOriginal_writer()).get().getIcon_url().equals("")) {
                    post.setUserIcon(userMapper.findUserBasicInfoByUserUID(post.getOriginal_writer()).get().getIcon_url());
                    String imgURL = userMapper.findUserBasicInfoByUserUID(post.getOriginal_writer()).get().getIcon_url().substring(ConvenienceUtil.getImgPath().length());
                    imgURL = imgURL.replace("\\", "/");
                    post.setUserIcon(imgURL);
                }
                post.setPost_owner_name(userMapper.findUserBasicInfoByUserUID(post.getPost_owner_id()).get().getNickname());
            }
            if (!post.getPost_img_url().equals("none")) {
                String imgURL = post.getPost_img_url().substring(ConvenienceUtil.getImgPath().length());
                imgURL = imgURL.replace("\\", "/");
                post.setPost_img_url(imgURL);
            }


        }
        return posts;
    }

    public void scrappingPost(int id, int userUID) {
        Post originalPost = postMapper.selectPostByID(id);
        int post_owner_id = originalPost.getPost_owner_id();
        String post_title = originalPost.getPost_title();
        String post_content = originalPost.getPost_content();
        String upload_date = originalPost.getUpload_date();
        String post_img_url = originalPost.getPost_img_url();
        boolean is_img_in = originalPost.is_img_in();
        int original_writer = post_owner_id;
        boolean is_post_scrapped = true;
        int scrapping_id = originalPost.getId();
        post_owner_id = userUID;
        postMapper.saveScrappingPost(post_owner_id,post_title,post_content,upload_date,post_img_url,is_img_in,original_writer,is_post_scrapped,scrapping_id);

    }
}
