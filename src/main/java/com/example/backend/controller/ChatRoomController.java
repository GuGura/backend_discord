package com.example.backend.controller;


import com.example.backend.controller.status.SuccessResponse;
import com.example.backend.controller.status.SuccessType;
import com.example.backend.model.entity.ChatRoom;
import com.example.backend.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(ControllerProperties.API_VERSION)
public class ChatRoomController {
    //    private final RedisToMariaDBMigrationMapper redisToMariaDBMigrationMapper;
//    private final ChatRoomRepository chatRoomRepository;
//    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    @PostMapping("/chatRoom/create")
    public ResponseEntity<?> createRoom(@RequestBody Map<String, String> params) {
        String room_type = params.get("room_type");
        String room_name = params.get("room_name");
        String channel_UID = params.get("channel_UID");

        ChatRoom chatRoom = ChatRoom.builder()
                .channel_uid(channel_UID)
                .room_name(room_name)
                .room_type(room_type)
                .build();
        chatRoomService.createChatRoom(chatRoom);
        return SuccessResponse.toResponseEntity(chatRoom, SuccessType.SUCCESS_CREATE_ROOM);
    }

//    @GetMapping("/room")
//    public String rooms(Model model) {
//        return "api/v1/chat/room";
//    }
//
//    @GetMapping("/rooms")
//    @ResponseBody
//    public List<ChatRoom> room(@RequestParam("channel_id") String channel_id) {
//        return chatRoomRepository.findAllRoom(channel_id);
//    }
//
//    @GetMapping("/room/enter/{roomId}")
//    public List<ChatMessage> roomDetail(@PathVariable String roomId) {
//        List<ChatMessage> chatMessages = redisToMariaDBMigrationMapper.getChatMessagesFromDB(roomId);
//        chatMessages.addAll(chatMessageRepository.getChatMessagesByRoomId(roomId));
//        return chatMessages;
//    }
//
//    @GetMapping("/room/{roomId}")
//    @ResponseBody
//    public ChatRoom roomInfo(@PathVariable String roomId) {
//
//        return chatRoomRepository.findRoomById(roomId);
//    }
}
