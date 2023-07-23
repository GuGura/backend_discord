package com.example.backend.service;

import com.example.backend.mapper.PostMapper;
import com.example.backend.util.ConvenienceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;

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
}
