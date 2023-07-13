package com.example.backend.controller;

import com.example.backend.controller.status.SuccessResponse;
import com.example.backend.controller.status.SuccessType;
import com.example.backend.model.MyChannelsDTO;
import com.example.backend.model.UserDTO;
import com.example.backend.model.entity.Friend;
import com.example.backend.service.ChannelService;
import com.example.backend.service.FriendService;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${version}")
public class MyInfoController {
    private final ChannelService channelService;
    private final UserService userService;
    private final FriendService friendService;

//    @PutMapping("/myInfo/init")
//    public ResponseEntity<?> initMyInfo() {
//        String email = UserUtil.getEmail();
//        resultDTO = ResultDTO.builder()
//                .result(email)
//                .build();
//        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
//    }

    @PostMapping("/myInfo/channelList")
    public ResponseEntity<?> getMyServerList(HttpServletRequest request) {
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        List<MyChannelsDTO> list = channelService.getMyChannels(userUID);
        return SuccessResponse.toResponseEntity(list, SuccessType.SUCCESS_GET_CHANNEL);
    }

    @PostMapping("/myInfo/basic")
    public ResponseEntity<?> getLobbyInfo(HttpServletRequest request) {
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        UserDTO userDTO = userService.getUserBasicInfo(userUID);
        return SuccessResponse.toResponseEntity(userDTO, SuccessType.SUCCESS_GET_BASIC_USERINFO);
    }

    //
//    @PostMapping("/myInfo/friend")
//    public  ResponseEntity<?> getFriendInfo(@RequestBody Map<String,String> params, HttpServletRequest request){
//        Member member = userService.getLobbyInfoByMemberUID(Integer.parseInt(params.get("friendId")));
//        ResultMember memberToReturn = UserUtil.memberToReturn(member);
//        memberToReturn.setId(Integer.parseInt(params.get("friendId")));
//        resultDTO = ResultDTO.builder()
//                .result(memberToReturn)
//                .message("lobby Info callback")
//                .build();
//        return new ResponseEntity<>(resultDTO,HttpStatus.OK);
//    }
    @PostMapping("/myInfo/friendList")
    public ResponseEntity<?> getFriendList(HttpServletRequest request) {
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        List<UserDTO> friendsList = friendService.myFriendList(userUID);
        if (friendsList == null)
            return SuccessResponse.toResponseEntity(SuccessType.SUCCESS_GET_BASIC_USERINFO);
        return SuccessResponse.toResponseEntity(friendsList, SuccessType.SUCCESS_GET_BASIC_USERINFO);
    }

    @PutMapping("/myInfo/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String,String> params, HttpServletRequest request) throws IOException {
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        Map<String,String> list = userService.updateUserResource(params,userUID);
        return SuccessResponse.toResponseEntity(list,SuccessType.SUCCESS_UPDATE_PROFILE);
    }
}
