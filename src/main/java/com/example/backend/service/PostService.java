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


}
