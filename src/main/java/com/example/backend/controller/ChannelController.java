package com.example.backend.controller;

import com.example.backend.controller.exception.CustomException;
import com.example.backend.controller.status.SuccessResponse;
import com.example.backend.controller.status.SuccessType;
import com.example.backend.model.MyChannelsDTO;
import com.example.backend.service.ChannelService;
import com.example.backend.util.ConvenienceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerProperties.API_VERSION)
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/channel/create")
    public ResponseEntity<?> createChannel(@RequestBody Map<String, String> params, HttpServletRequest request) throws IOException {
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        String fileURL = params.get("fileURL");
        String channelName = params.get("channelName");
        MyChannelsDTO myChannelDTO = channelService.createChannel(userUID, fileURL, channelName);
        return SuccessResponse.toResponseEntity(myChannelDTO, SuccessType.CREATE_CHANNEL);
    }

//    @GetMapping("/channel/attend/{inviteCode}")
//    public ResponseEntity<?> attendChannel(@PathVariable("inviteCode")String inviteCode, HttpServletRequest request){
//        int memberUID =(int) request.getAttribute(ResultDtoProperties.USER_UID);
//        return channelService.getAttendChannel(inviteCode,memberUID);
//    }
//    @DeleteMapping("/channel/leaveChannel/{channelUID}")
//    public ResponseEntity<?> leaveChannel(@PathVariable("channelUID") String channelUID,HttpServletRequest request){
//        int channel_UID = Integer.parseInt(channelUID);
//        int memberUID =(int) request.getAttribute(ResultDtoProperties.USER_UID);
//        channelService.leaveChannel(channel_UID,memberUID);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
