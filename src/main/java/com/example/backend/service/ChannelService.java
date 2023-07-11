package com.example.backend.service;

import com.example.backend.controller.exception.CustomException;
import com.example.backend.controller.exception.ErrorType;
import com.example.backend.mapper.ChannelMapper;
import com.example.backend.model.Channel;
import com.example.backend.model.MyChannelsDTO;
import com.example.backend.util.ConvenienceUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelMapper channelMapper;

    public List<MyChannelsDTO> getMyChannels(int userUID) {
        List<MyChannelsDTO> list = new ArrayList<MyChannelsDTO>(channelMapper.findChannelsByUserUID(userUID));
        List.of(list).forEach(items -> {
            for (MyChannelsDTO item : items) {
                if (item.getChannel_icon_url() != null) {
                    String imgURL = item.getChannel_icon_url().substring(ConvenienceUtil.getImgPath().length());
                    imgURL = imgURL.replace("\\","/");
                    item.setChannel_icon_url(imgURL);
                }
            }
        });
        list.add(getChannelType(userUID, "addServer", "/img/channelList/add_channel.png", "addServer"));
        list.add(getChannelType(userUID, "public", "/img/channelList/public_icon.png", "public"));
        return list;
    }

    private MyChannelsDTO getChannelType(int userUID, String channel_title, String channel_iconURL, String channel_type) {
        return MyChannelsDTO.builder()
                .channel_UID(0)
                .user_UID(userUID)
                .channel_title(channel_title)
                .channel_icon_url(channel_iconURL)
                .channel_type(channel_type)
                .build();
    }

    public MyChannelsDTO getAttendChannel(String inviteCode, int userUID){
        channelMapper.findChannelByInviteCode(inviteCode).orElseThrow(() -> new CustomException(ErrorType.CHANNEL_NOT_FOUND));
        channelMapper.findChannelByInviteCode(inviteCode).ifPresent(channelDTO ->{
            int channelUID = channelDTO.getChannel_uid();
            boolean isMyChannel = channelMapper.findChannelUserByUserUID(channelUID,userUID).isPresent();
            if (isMyChannel)
                throw new CustomException(ErrorType.USER_ALREADY_EXISTS);
            else
                channelMapper.saveChannelUser(channelUID,userUID,"ROLE_USER");
        });
        MyChannelsDTO list =  channelMapper.findLastChannelByUserUID(userUID);
        if (list.getChannel_icon_url() != null) {
            String imgURL = list.getChannel_icon_url().substring(ConvenienceUtil.getImgPath().length());
            imgURL = imgURL.replace("\\","/");
            list.setChannel_icon_url(imgURL);
        }
        return list;
    }

    public MyChannelsDTO createChannel(int userUID, String fileURL, String channelName) throws IOException {
        channelMapper.saveChannel(channelName, userUID);
        int channel_UID = channelMapper.findChannelUIDByUserUID(channelName, userUID);

        if (!fileURL.equals("/img/sidebar/choose.png")) {
            String base64 = fileURL.substring(fileURL.lastIndexOf(",") + 1);
            String fileName = base64.substring(30, 50) + ".png";
            BufferedImage image = ConvenienceUtil.base64DecoderToImg(base64);
            String folderPath = ConvenienceUtil.makeOrGetChannelFolderURL(channel_UID);
            Path imgPath = Paths.get(folderPath, fileName);
            ImageIO.write(image, "png", imgPath.toFile());
            fileURL = imgPath.toString();
            channelMapper.updateChannelIcon(fileURL, channel_UID);
        }
        channelMapper.saveChannelUser(channel_UID, userUID, "ROLE_ADMIN");
        MyChannelsDTO myChannelsDTO = channelMapper.findLastChannelByUserUID(userUID);
        Collections.singletonList(myChannelsDTO).forEach(item -> {
            if (item.getChannel_icon_url() != null) {
                String base64Img = ConvenienceUtil.base64EncoderToImg(item.getChannel_icon_url());
                item.setChannel_icon_url(base64Img);
            }
        });
        return myChannelsDTO;
    }

//
//    public void leaveChannel(int channelUID, int memberUID) {
//        channelMapper.deleteChannelMember(channelUID,memberUID);
//        if(channelMapper.findChannelMemberByChannelUID(channelUID).isEmpty()){
//            channelMapper.deleteChannel(channelUID);
//        }
//    }
}