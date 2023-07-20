package com.example.backend.service;

import com.example.backend.controller.exception.CustomException;
import com.example.backend.controller.exception.ErrorType;
import com.example.backend.mapper.ChannelMapper;
import com.example.backend.mapper.ChatRoomMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.MyChannelsDTO;
import com.example.backend.model.entity.ChatRoom;
import com.example.backend.model.entity.User;
import com.example.backend.util.CodeGenerator;
import com.example.backend.util.ConvenienceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelMapper channelMapper;
    private final ChatRoomMapper chatRoomMapper;
    private final UserMapper userMapper;

    public List<MyChannelsDTO> getMyChannels(int userUID) {
        List<MyChannelsDTO> list = new ArrayList<MyChannelsDTO>(channelMapper.findChannelsByUserUID(userUID));
        List.of(list).forEach(items -> {
            for (MyChannelsDTO item : items) {
                if (item.getChannel_icon_url() != null) {
                    String imgURL = item.getChannel_icon_url().substring(ConvenienceUtil.getImgPath().length());
                    imgURL = imgURL.replace("\\", "/");
                    item.setChannel_icon_url(imgURL);
                }
            }
        });
        list.add(getChannelType(userUID, "addServer", "/img/channelList/add_channel.png", "addServer"));
        list.add(getChannelType(userUID, "public", "/img/channelList/public_icon.png", "public"));
        return list;
    }

    public Map<String,Object> getAttendChannel(String inviteCode, int userUID) {
        channelMapper.findChannelByInviteCode(inviteCode).orElseThrow(() -> new CustomException(ErrorType.CHANNEL_NOT_FOUND));
        channelMapper.findChannelByInviteCode(inviteCode).ifPresent(channelDTO -> {
            System.out.println("1");
            int channelUID = channelDTO.getChannel_uid();
            boolean isMyChannel = channelMapper.findChannelUserByUserUID(userUID,channelUID).isPresent();
            if (isMyChannel)
                throw new CustomException(ErrorType.USER_ALREADY_EXISTS);
            else
                channelMapper.saveChannelUser(channelUID, userUID, "ROLE_USER");
        });
        MyChannelsDTO list = channelMapper.findLastChannelByUserUID(userUID);
        if (list.getChannel_icon_url() != null) {
            String imgURL = list.getChannel_icon_url().substring(ConvenienceUtil.getImgPath().length());
            imgURL = imgURL.replace("\\", "/");
            list.setChannel_icon_url(imgURL);
        }
        List<ChatRoom> textRoom = chatRoomMapper.findTextRoomList(list.getChannel_UID());
        List<ChatRoom> voiceRoom = chatRoomMapper.findVoiceRoomList(list.getChannel_UID());
        Map<String,Object> data = new LinkedHashMap<>();
        data.put("channel",list);
        data.put("textRoom",textRoom);
        data.put("voiceRoom",voiceRoom);
        return data;
    }


    public Map<String,Object> createChannel(int userUID, String fileURL, String channelName) throws IOException {
        channelMapper.saveChannel(channelName, userUID);
        int channel_UID = channelMapper.findChannelUIDByUserUID(channelName, userUID);

        if (!fileURL.equals("/img/sidebar/choose.png")) {
            String base64 = fileURL.substring(fileURL.lastIndexOf(",") + 1);
            String fileName = base64.substring(30, 50) + ".png";
            String folderPath = ConvenienceUtil.makeOrGetChannelFolderURL(channel_UID);
            Path imgPath = Paths.get(folderPath, fileName);
            createImg(base64, imgPath);
            fileURL = imgPath.toString();
            channelMapper.updateChannelIcon(fileURL, channel_UID);
        }
        channelMapper.saveChannelUser(channel_UID, userUID, "ROLE_ADMIN");
        MyChannelsDTO myChannelsDTO = channelMapper.findLastChannelByUserUID(userUID);
        Collections.singletonList(myChannelsDTO).forEach(item -> {
            if (item.getChannel_icon_url() != null) {
                String imgURL = item.getChannel_icon_url().substring(ConvenienceUtil.getImgPath().length());
                imgURL = imgURL.replace("\\", "/");
                item.setChannel_icon_url(imgURL);
            }
        });
        ChatRoom.initRoom(chatRoomMapper,channel_UID);
        ChatRoom textRoom = chatRoomMapper.findTextRoom(channel_UID).orElseThrow(()->new CustomException(ErrorType.ROOM_NOT_FOUND));
        ChatRoom voiceRoom = chatRoomMapper.findVoiceRoom(channel_UID).orElseThrow(()->new CustomException(ErrorType.ROOM_NOT_FOUND));
        Map<String,Object> data = new LinkedHashMap<>();
        data.put("channel",myChannelsDTO);
        data.put("textRoom",textRoom);
        data.put("voiceRoom",voiceRoom);
        return data;
    }


    public String createInviteCode(int channelUID) {
        String randomCode = CodeGenerator.createCode();
        channelMapper.updateInViteCode(channelUID, randomCode);
        return channelMapper.findInviteCodeByChannelUID(channelUID).orElseThrow(() -> new CustomException(ErrorType.IMG_GENERATE_INVITE_CODE_FAIL));
    }

    public void leaveChannel(int channelUID, int userUID) {
        channelMapper.deleteChannelMember(channelUID, userUID);
        if (channelMapper.findChannelMemberByChannelUID(channelUID).isEmpty()) {
            channelMapper.deleteChannel(channelUID);
            chatRoomMapper.deleteRooms(channelUID);
        }
    }

    @Async("File")
    public void createImg(String base64, Path imgPath) throws IOException {
        BufferedImage image = ConvenienceUtil.base64DecoderToImg(base64);
        ImageIO.write(image, "png", imgPath.toFile());
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

    public List<User> getUserResources() {
        return userMapper.onlineUser();
    }
}
