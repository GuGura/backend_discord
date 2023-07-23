package com.example.backend.mapper;

import com.example.backend.model.entity.Post;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Map;

@Mapper
public interface PostMapper {

    @Insert("insert into post(post_owner_id, post_title, post_content, upload_date, post_img_url, is_img_in, is_post_scrapped) values(#{userUID}, #{params.post_title} , #{params.post_content}, #{params.upload_date}, #{params.post_img_url}, #{is_img_in}, false)")
    void saveNewPost(@Param("params") Map<String, String> params,@Param("userUID") int userUID, @Param("is_img_in")boolean is_img_in);

    @Select("select * from post where post_owner_id = #{userUID} and id < #{pageNum} order by id desc limit 10")
    List<Post> listTenById(@Param("pageNum") int pageNum, @Param("userUID") int userUID);

}
