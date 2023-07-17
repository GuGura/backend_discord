package com.example.backend.service;

import com.example.backend.controller.exception.CustomException;
import com.example.backend.controller.exception.ErrorType;
import com.example.backend.mapper.FriendMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.UserDTO;
import com.example.backend.model.entity.Friend;
import com.example.backend.util.ConvenienceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendMapper friendMapper;
    private final UserMapper userMapper;

    public List<UserDTO> myFriendList(int userUID) {
        List<UserDTO> list = friendMapper.findMyFriendByUserUID(userUID);
        return getUserDTOS(list);
    }

    public List<UserDTO> findSearchUsers(String nickname, int userUID) {
        List<UserDTO> list = friendMapper.findSearchUsers(nickname, userUID);
        return getUserDTOS(list);
    }

    private List<UserDTO> getUserDTOS(List<UserDTO> list) {
        list.forEach(user -> {
            if (!user.getIcon_url().equals("")) {
                String imgURL = user.getIcon_url().substring(ConvenienceUtil.getImgPath().length());
                imgURL = imgURL.replace("\\", "/");
                user.setIcon_url(imgURL);
            }
        });
        return list;
    }

    public void save(int userUID, int sendUserUID) {
        if (friendMapper.findData(userUID, sendUserUID) == 1)
            throw new CustomException(ErrorType.USER_ALREADY_FRIEND);
        friendMapper.saveRequest(userUID, sendUserUID);
        if (friendMapper.findData(userUID, sendUserUID) != 1)
            throw new CustomException(ErrorType.FRIEND_REQUEST_FAIL);
        friendMapper.saveResponse(userUID, sendUserUID);
    }

    public List<UserDTO> findRequestUsers(int userUID) {
        List<UserDTO> list = friendMapper.fineRequestUsers(userUID);
        return getUserDTOS(list);
    }

    public UserDTO responseFriend(int userUID, int friendUID) {
        if (friendMapper.findData(userUID, friendUID) == 1)
            throw new CustomException(ErrorType.USER_ALREADY_FRIEND);
        friendMapper.updateFriend(userUID, friendUID);
        UserDTO userDTO = userMapper.findUserResourceByUserUID(friendUID);
        if (userDTO.getIcon_url() != null && !userDTO.getIcon_url().equals("")) {
            String imgURL = userDTO.getIcon_url().substring(ConvenienceUtil.getImgPath().length());
            imgURL = imgURL.replace("\\", "/");
            userDTO.setIcon_url(imgURL);
        }
        return userDTO;
    }


//    public ResultFriend friendToReturn(FriendDTO2 friend) {
//        ResultFriend Rfriend = new ResultFriend();
//        Rfriend.setID(friend.getID());
//        Rfriend.setUSER_ICON_URL(UserUtil.pathToBytes(friend.getUSER_ICON_URL()));
//        Rfriend.setUSERNAME(friend.getUSERNAME());
//        return Rfriend;
//    }
//
//    public ResultFriend friendToReturn(FriendDTO friend) {
//        ResultFriend Rfriend = new ResultFriend();
//        Rfriend.setID(friend.getFRIEND_RECEIVER());
//        Rfriend.setUSER_ICON_URL(UserUtil.pathToBytes(friend.getUSER_ICON_URL()));
//        Rfriend.setUSERNAME(friend.getUSERNAME());
//        return Rfriend;
//    }
//
}
