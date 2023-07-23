package com.example.backend.controller;


import com.example.backend.mapper.UserMapper;
import com.example.backend.model.EventDTO;
import com.example.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ControllerProperties.API_VERSION)
public class EventController {

    private final EventService eventService;
    private final UserMapper userMapper;

    @Autowired
    public EventController(EventService eventService, UserMapper userMapper) {
        this.eventService = eventService;
        this.userMapper = userMapper;

    }

    @GetMapping("/friend/{id}")
    public int friendTest(@PathVariable("id") int id){
        return id;
    }

    @PostMapping("/event/saveFriendEvent")
    public int saveFriend(@RequestBody EventDTO eventOrigin, HttpServletRequest request) throws Exception {
        EventDTO event = eventService.viewEventById(eventOrigin.getId());
        int memberUID = (int) request.getAttribute(ControllerProperties.userUID);
        int memberId = event.getMemberId();
        event.setMemberId(memberUID);
        event.setGroupId(memberId);
        int id = eventService.saveEvent(event);
        return id;
    }

    @PostMapping("/event/saveEvent")
    public int save(@RequestBody EventDTO event, HttpServletRequest request) throws Exception {
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        event.setGroupName(userMapper.findUserBasicInfoByUserUID(userUID).get().getNickname());
        event.setMemberId(userUID);
        int id = eventService.saveEvent(event);
        return id;
    }

    @PostMapping("/event/deleteEvent")
    public void deleteEvent(@RequestBody EventDTO event,HttpServletRequest request) throws Exception {
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        eventService.deleteEvent(event.getId(),userUID);
    }

    @ResponseBody
    @PostMapping("/event/listMonthly")
    public ResponseEntity<List<EventDTO>> listMonthly(@RequestBody String date, HttpServletRequest request){
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        String year = date.substring(14,16);
        List<EventDTO> events= eventService.listMonthly(Integer.parseInt(year), userUID);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/event/listMonthlyFriend")
    public ResponseEntity<List<EventDTO>> listMonthlyFriend(@RequestBody Map<String,String> params, HttpServletRequest request){
        int memberUID =Integer.parseInt(params.get("id"));
        String year = params.get("date");
        String date = year.substring(5,7);
        System.out.println(date);
        List<EventDTO> events= eventService.listMonthly(Integer.parseInt(date), memberUID);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/event/listByDate")
    public ResponseEntity<List<EventDTO>> listByDate(@RequestBody Map<String,String> params, HttpServletRequest request){
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        List<EventDTO> events= eventService.listDaily(Integer.parseInt(params.get("year")),Integer.parseInt(params.get("month")), Integer.parseInt(params.get("date")), userUID);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/event/listByDateFriend")
    public ResponseEntity<List<EventDTO>> listByDateFriend(@RequestBody Map<String,String> params, HttpServletRequest request){
        int memberUID = Integer.parseInt(params.get("id"));
        List<EventDTO> events= eventService.listDaily(Integer.parseInt(params.get("year")),Integer.parseInt(params.get("month")), Integer.parseInt(params.get("date")), memberUID);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/event/listMonthlyBtn")
    public ResponseEntity<List<EventDTO>> listMonthlyNext(@RequestBody String date, HttpServletRequest request){
        int userUID = (int) request.getAttribute(ControllerProperties.userUID);
        String year = date.substring(14,16);
        System.out.println(Integer.parseInt(year)+1);
        List<EventDTO> events= eventService.listMonthly(Integer.parseInt(year)+1, userUID);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/event/listMonthlyBtnFriend")
    public ResponseEntity<List<EventDTO>> listMonthlyNextFriend(@RequestBody Map<String,String> params, HttpServletRequest request){
        int memberUID =Integer.parseInt(params.get("id"));
        String year = params.get("date");
        String date = year.substring(5,7);
        List<EventDTO> events= eventService.listMonthly(Integer.parseInt(date)+1, memberUID);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }


}
