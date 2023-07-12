package com.example.backend.service;

import com.example.backend.mapper.FriendMapper;
import com.example.backend.model.UserDTO;
import com.example.backend.model.entity.Friend;
import com.example.backend.util.ConvenienceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendMapper friendMapper;
    public List<UserDTO> myFriendList(int userUID) {
        List<UserDTO> list = friendMapper.findMyFriendByUserUID(userUID);
        list.forEach(user ->{
            if (user.getIcon_url() != null){
                String imgURL = user.getIcon_url().substring(ConvenienceUtil.getImgPath().length());
                imgURL = imgURL.replace("\\","/");
                user.setIcon_url(imgURL);
            }
        });
        return list;
    }


//    public List<ResultFriend> findSearchUsers(String username, int memberUID) {
//        List<FriendDTO2> list = friendMapper.findSearchUsers(username, memberUID);
//        List<ResultFriend> listToReturn = new ArrayList<>();
//        for (FriendDTO2 friend : list) {
//            listToReturn.add(friendToReturn(friend));
//        }
//        return listToReturn;
//    }
//
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
//    public ResponseEntity<?> save(int memberUID, int sendMemberUID) {
//        if (friendMapper.findData(memberUID, sendMemberUID) == 1)
//            return new ResponseEntity<>("이미 친구추가 신청한 상대입니다.", HttpStatus.PAYMENT_REQUIRED);
//        friendMapper.saveRequest(memberUID, sendMemberUID);
//        if (friendMapper.findData(memberUID, sendMemberUID) != 1)
//            return new ResponseEntity<>("친구신청 실패", HttpStatus.PAYMENT_REQUIRED);
//        friendMapper.saveResponse(sendMemberUID, memberUID);
//        return new ResponseEntity<>("친구신청 완료", HttpStatus.CREATED);
//    }
//
//    public List<ResultFriend> findRequestUsers(int memberUID) {
//        List<FriendDTO2> list = friendMapper.fineRequestUsers(memberUID);
//        List<ResultFriend> listToReturn = new ArrayList<>();
//        for (FriendDTO2 friend : list) {
//            listToReturn.add(friendToReturn(friend));
//        }
//        return listToReturn;
//    }
//
//    public ResponseEntity<?> responseFriend(int memberUID, int friendUID) {
//        if (friendMapper.findData(memberUID, friendUID) == 1)
//            return new ResponseEntity<>("이미 친구추가 신청한 상대입니다.", HttpStatus.PAYMENT_REQUIRED);
//        friendMapper.updateFriend(memberUID, friendUID);
//        return new ResponseEntity<>("친구수락", HttpStatus.ACCEPTED);
//    }
}
