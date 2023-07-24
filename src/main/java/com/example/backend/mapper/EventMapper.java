package com.example.backend.mapper;


import com.example.backend.model.EventDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EventMapper {


    @Insert("insert into event(estart, end, title, memberId, groupName, groupId) values(#{start},#{end},#{title}, #{memberId}, #{groupName}, #{groupId})")
    void saveEvent(EventDTO event);

    @Select("SELECT last_insert_id();")
    int selectLastInserted();

    @Select("SELECT id, estart, end, title, memberId, groupName, groupId from event where id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "estart", property = "start"),
            @Result(column = "end", property = "end"),
            @Result(column = "title", property = "title"),
            @Result(column = "memberId", property = "memberId"),
            @Result(column = "groupId", property = "groupId")
    })
    EventDTO selectEventById(@Param("id") int id);

    @Select("SELECT id, estart, end, title, memberId, groupName from event WHERE (month(estart) = #{year} OR month(end) = #{year}) and memberId=#{memberId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "estart", property = "start"),
            @Result(column = "end", property = "end"),
            @Result(column = "title", property = "title"),
            @Result(column = "memberId", property = "memberId"),
            @Result(column = "groupId", property = "groupId")
    })
    List<EventDTO> eventsByMonth(@Param("year") int year, @Param("memberId") int memberId);


    @Select("SELECT * from EVENT WHERE (TO_CHAR(estart,'yyyy-mm-dd') = #{date} || TO_CHAR(DATE_SUB(end, INTERVAL 1 day),'yyyy-mm-dd') =  #{date}) and memberId=#{memberId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "estart", property = "start"),
            @Result(column = "end", property = "end"),
            @Result(column = "title", property = "title"),
            @Result(column = "memberId", property = "memberId"),
            @Result(column = "groupName", property = "groupName"),
            @Result(column = "groupId", property = "groupId")
    })
    List<EventDTO> eventsByDate(@Param("date") String date, @Param("memberId") int memberId);

    @Delete("delete from event where (id = #{id} or groupId = #{id}) and memberId = #{memberId}")
    void deleteEvent(@Param("id") int id, @Param("memberId") int memberId);
}
