package com.example.backend.controller;

import com.example.backend.controller.status.SuccessResponse;
import com.example.backend.controller.status.SuccessType;
import com.example.backend.model.MyChannelsDTO;
import com.example.backend.service.ChannelService;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("${version}")
public class MyInfoController {
    private final ChannelService channelService;
    private final UserService userService;
//    private final FriendService friendService;

//    @PutMapping("/myInfo/init")
//    public ResponseEntity<?> initMyInfo() {
//        String email = UserUtil.getEmail();
//        resultDTO = ResultDTO.builder()
//                .result(email)
//                .build();
//        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
//    }

    @GetMapping("/myInfo/channelList")
    public ResponseEntity<?> getMyServerList(HttpServletRequest request){
        System.out.println("getMyServerList");
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        List<MyChannelsDTO> list = channelService.getMyChannels(userUID);
        return SuccessResponse.toResponseEntity(list, SuccessType.SUCCESS_GET_CHANNEL);
    }

//    @PostMapping("/myInfo/lobby")
//    public  ResponseEntity<?> getLobbyInfo(HttpServletRequest request){
//        int memberUID =(int) request.getAttribute(ResultDtoProperties.USER_UID);
//        Member member = userService.getLobbyInfoByMemberUID(memberUID);
//        resultDTO = ResultDTO.builder()
//                .result(UserUtil.memberToReturn(member))
//                .message("lobby Info callback")
//                .build();
//        return new ResponseEntity<>(resultDTO,HttpStatus.OK);
//    }
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
//    @GetMapping("/myInfo/friendList")
//    public ResponseEntity<?> getFriendList(HttpServletRequest request){
//        int memberUID =(int) request.getAttribute(ResultDtoProperties.USER_UID);
//        List<FriendDTO> friendsList = userService.myFriendList(memberUID);
//        List<ResultFriend> friendsListToReturn = new ArrayList<>();
//        for (FriendDTO friend: friendsList) {
//            friendsListToReturn.add(friendService.friendToReturn(friend));
//        }
//        System.out.println("getFriendList:"+friendsList);
//        return new ResponseEntity<>(friendsListToReturn,HttpStatus.OK);
//    }
}
