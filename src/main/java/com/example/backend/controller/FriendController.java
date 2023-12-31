package com.example.backend.controller;


import com.example.backend.controller.status.SuccessResponse;
import com.example.backend.controller.status.SuccessType;
import com.example.backend.model.UserDTO;
import com.example.backend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(ControllerProperties.API_VERSION)
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/friend/{id}")
    public int friendTest(@PathVariable("id") int id){
        return id;
    }

    @PostMapping("/friend/search/{nickname}")
    public ResponseEntity<?> getSearchList(@PathVariable("nickname") String nickname, HttpServletRequest request) {
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        List<UserDTO> list = friendService.findSearchUsers(nickname, userUID);
        return SuccessResponse.toResponseEntity(list, SuccessType.SUCCESS_FIND_USER);
    }

    @PostMapping("/friend/send/{friendUID}")
    public ResponseEntity<?> sendFriendRequest(@PathVariable("friendUID") int sendUserUID, HttpServletRequest request) {
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        friendService.save(userUID, sendUserUID);
        return SuccessResponse.toResponseEntity(SuccessType.SUCCESS_SEND_FRIEND_REQUEST);
    }

    @PostMapping("/friend/requestUser")
    public ResponseEntity<?> requestUserList(HttpServletRequest request){
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        List<UserDTO> list = friendService.findRequestUsers(userUID);
        return SuccessResponse.toResponseEntity(list,SuccessType.SUCCESS_FIND_USER);
    }

    @PutMapping("/friend/response/{friendUID}")
    public ResponseEntity<?> responseFriend(@PathVariable("friendUID") int friendUID,HttpServletRequest request){
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        UserDTO user = friendService.responseFriend(userUID,friendUID);
        return SuccessResponse.toResponseEntity(user,SuccessType.SUCCESS_FIND_USER);
    }

}