package com.example.backend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.security.core.parameters.P;

import java.util.Map;

@Mapper
public interface PostMapper {

    @Insert("insert into post(post_owner_id, post_title, post_content, upload_date, post_img_url, is_img_in, is_post_scrapped) values(#{userUID}, #{params.post_title} , #{params.post_content}, #{params.upload_date}, #{params.post_img_url}, #{is_img_in}, false)")
    void saveNewPost(@Param("params") Map<String, String> params,@Param("userUID") int userUID, @Param("is_img_in")boolean is_img_in);
}
