//package com.example.backend.handler;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptorAdapter;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class StompHandler extends ChannelInterceptorAdapter {
//
//    private final ChatService chatService;
//    private Map<String, String> sessionIdUsernameMap = new ConcurrentHashMap<>();
//
//    @Override
//    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        String sessionId = accessor.getSessionId();
//        String roomId = "UserList";
//        String username;
//
//        switch (accessor.getCommand()) {
//            case CONNECT:
//                // 유저가 Websocket으로 connect()를 한 뒤 호출됨
//                System.out.println("User Connect");
//                username = accessor.getFirstNativeHeader("username");
//                sessionIdUsernameMap.put(sessionId, username);
//                chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).sender(username).build());
//                System.out.println("username : "+ username);
//                log.info("SUBSCRIBED {}, {}", username, roomId);
//                break;
//            case DISCONNECT:
//                // 유저가 Websocket으로 disconnect() 를 한 뒤 호출됨 or 세션이 끊어졌을 때 발생함(페이지 이동~ 브라우저 닫기 등)
//                System.out.println("User DisConnect");
//                username = sessionIdUsernameMap.get(sessionId);
//                chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.QUIT).roomId(roomId).sender(username).build());
//                sessionIdUsernameMap.remove(sessionId); // Remember to clean up after disconnect
//                log.info("DISCONNECTED {}, {}", username, roomId);
//                break;
//            default:
//                break;
//        }
//    }
//}
