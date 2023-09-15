package com.example.backend.service;

import com.example.backend.controller.exception.CustomException;
import com.example.backend.controller.exception.ErrorType;
import com.example.backend.mapper.EmailMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.entity.User;
import com.example.backend.model.UserDTO;
import com.example.backend.util.CodeGenerator;
import com.example.backend.util.ConvenienceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final EmailMapper emailMapper;
    private final BCryptPasswordEncoder encoder;

    public void SignUp(UserDTO userDTO) {
        boolean userExist = findUserByUsername(userDTO.getUsername()).isPresent();
        if (userExist) throw new CustomException(ErrorType.USER_ALREADY_EXISTS);
        String bCryptPassword = encoder.encode(userDTO.getPassword());
        userDTO.setPassword(bCryptPassword);
        userResourceSave(userDTO);
    }

    private void userResourceSave(UserDTO userDTO) {
        emailMapper.findAuth(userDTO.getUsername(), userDTO.getEmailAuthCode()).orElseThrow(() -> new CustomException(ErrorType.MAIL_AUTHENTICATE_CODE_FAIL));
        userMapper.save(userDTO);
        userMapper.findUserByUsernameO(userDTO.getUsername()).ifPresent(user -> {
            userMapper.saveResource(user.getId(), userDTO.getNickname());
        });
    }
    public Map<String,String> updateUserResource(Map<String, String> params, int userUID)throws IOException {
        if (!params.get("icon_url").equals("")){
            String base64 = params.get("icon_url").substring(params.get("icon_url").lastIndexOf(",") + 1);
            String fileName = CodeGenerator.createCode();
            String folderPath = ConvenienceUtil.makeOrGetLobbyFolderURL(userUID);
            Path imgPath = Paths.get(folderPath, fileName);
            createImg(base64,imgPath);
            params.put("icon_url",imgPath.toString());
        }userMapper.updateUserResource(params,userUID);
        Map<String,String> list =  userMapper.findUserResourceByUserUIDM(userUID);
        if (!list.get("icon_url").equals("")){
            String imgURL = list.get("icon_url").substring(ConvenienceUtil.getImgPath().length());
            imgURL = imgURL.replace("\\","/");
            list.put("icon_url",imgURL);
        }
        return list;
    }
    @Async("File")
    public void createImg(String base64,Path imgPath) throws IOException{
        BufferedImage image = ConvenienceUtil.base64DecoderToImg(base64);
        ImageIO.write(image, "png", imgPath.toFile());
    }
    public UserDTO getUserBasicInfo(int userUID) {
        System.out.println(userUID);
        UserDTO userDTO = userMapper.findUserBasicInfoByUserUID(userUID).orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        if (!userDTO.getIcon_url().equals("")){
            String imgURL = userDTO.getIcon_url().substring(ConvenienceUtil.getImgPath().length());
            imgURL = imgURL.replace("\\","/");
            userDTO.setIcon_url(imgURL);
        }
        return userDTO;
    }


    public Optional<User> findUserByUsername(String username) {
        return userMapper.findUserByUsernameO(username);
    }

    public Optional<User> findUserByUID(int userUID) {
        return userMapper.findUserByUserUIDO(userUID);
    }

    public boolean matchesPassword(String input, String db) {
        return encoder.matches(input, db);
    }
}
