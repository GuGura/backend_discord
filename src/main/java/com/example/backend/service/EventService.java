package com.example.backend.service;


import com.example.backend.mapper.EventMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventMapper eventMapper;
    private final UserMapper userMapper;


    public List<EventDTO> eventsByMonth(int year, int memberId) {
        return eventMapper.eventsByMonth(year, memberId);
    }

    public int saveEvent(EventDTO event,int userUID) {
        event.setGroupName(userMapper.findUserBasicInfoByUserUID(userUID).get().getNickname());
        eventMapper.saveEvent(event);

        return eventMapper.selectLastInserted();
    }

    public EventDTO selectEventById(int id){
        return eventMapper.selectEventById(id);
    }

    public void deleteEvent(int id, int memberId) {
        eventMapper.deleteEvent(id,memberId);
    }

    public List<EventDTO> eventsByDate(int year,int month, int date, int memberId) {
        String sMonth = "";
        if(month<10){
            sMonth = "0"+Integer.toString(month);
        }else{
            sMonth = Integer.toString(month);
        }
        String sDate = "";
        if(date<10){
            sDate = "0"+Integer.toString(date);
        }else{
            sDate = Integer.toString(date);
        }
        String fullDate = Integer.toString(year)+"-"+sMonth+"-"+sDate;
        return eventMapper.eventsByDate(fullDate,memberId);
    }
}
