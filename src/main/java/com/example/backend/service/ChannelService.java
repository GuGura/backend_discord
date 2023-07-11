package com.example.backend.service;

import com.example.backend.mapper.ChannelMapper;
import com.example.backend.model.Channel;
import com.example.backend.model.MyChannelsDTO;
import com.example.backend.util.ConvenienceUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelMapper channelMapper;

    public List<MyChannelsDTO> getMyChannels(int userUID) {
        List<MyChannelsDTO> list = new ArrayList<MyChannelsDTO>(channelMapper.findChannelsByUserUID(userUID));
        System.out.println("getMyChannels: " + channelMapper.findChannelsByUserUID(userUID));
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

    //    public ResponseEntity<?> getAttendChannel(String inviteCode, int memberUID) {
//        Channel channel = channelMapper.findChannelByInviteCode(inviteCode).orElse(null);
//        if (channel == null)
//            return new ResponseEntity<>("채널을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
//        ChannelMember channelMember = channelMapper.findChannelMemberByMemberUID(memberUID, channel.getChannel_UID()).orElse(null);
//        if (channelMember != null)
//            return new ResponseEntity<>("이미 가입하신 채널입니다.", HttpStatus.NOT_FOUND);
//        channelMapper.saveChannelMember(channel.getChannel_UID(), memberUID, "ROLE_USER");
//        MyChannelsDTO myChannelsDTO = channelMapper.findLastChannelByMemberUID(memberUID);
//        return new ResponseEntity<>(myChannelsDTO, HttpStatus.CREATED);
//    }
//
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
        return channelMapper.findLastChannelByUserUID(userUID);
    }
//
//    public String imgURL(String fileURL, int memberUID) throws IOException {
//        String base64 = fileURL.substring(fileURL.lastIndexOf(",") + 1);
//        BufferedImage image = null;
//        byte[] imageByte;
//
//        Base64.Decoder decoder = Base64.getDecoder();
//        imageByte = decoder.decode(base64);
//        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
//        image = ImageIO.read(bis);
//        bis.close();
//
//        //파일명
//        String fileName = base64.substring(30, 50) + ".png";
//
//        String uploadFolder = "C:/upload/users/" + memberUID + "/channels/";
//        new File(uploadFolder).mkdirs();
//        File outputfile = new File(uploadFolder + fileName);
//        ImageIO.write(image, "png", outputfile);
//        //저장완료
//        return uploadFolder + fileName;
//    }
//
//    public void leaveChannel(int channelUID, int memberUID) {
//        channelMapper.deleteChannelMember(channelUID,memberUID);
//        if(channelMapper.findChannelMemberByChannelUID(channelUID).isEmpty()){
//            channelMapper.deleteChannel(channelUID);
//        }
//    }
}
