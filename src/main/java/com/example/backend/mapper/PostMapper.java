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

    @Select("select * from post where post_owner_id = #{userUID} and id < #{pageNum} and visible = 1 order by id desc limit 10")
    List<Post> listTenById(@Param("pageNum") int pageNum, @Param("userUID") int userUID);

    @Update("update post set visible = false where (id = #{id} and post_owner_id = #{userUID}) or (scrapping_id = #{id} and original_writer = #{userUID}) or (scrapping_id = #{scrapping_id}) or (id = #{scrapping_id} and  post_owner_id = #{userUID})")
    void deleteMyPost(@Param("id") int id,@Param("userUID") int userUID, @Param("scrapping_id")int scrapping_id);

    @Select("SELECT * FROM post WHERE (post_owner_id = " +
            "(SELECT f1.FRIEND_RECEIVER FROM friend f1 " +
            "JOIN user u ON f1.FRIEND_RECEIVER = u.ID " +
            "WHERE f1.FRIEND_SENDER = #{userUID} " +
            "AND f1.FRIEND_CHECKED = true " +
            "AND EXISTS " +
            "(SELECT #{userUID} FROM friend f2 " +
            "WHERE f2.FRIEND_SENDER = f1.FRIEND_RECEIVER " +
            "AND f2.FRIEND_RECEIVER = f1.FRIEND_SENDER " +
            "AND f2.FRIEND_CHECKED = true) " +
            "LIMIT 1) or post_owner_id = #{userUID})" +
            "AND id < #{pageNum} AND visible = true ORDER BY id DESC LIMIT 10")
    List<Post> listTenByIdWithFriend(@Param("pageNum")int pageNum, @Param("userUID") int userUID);


    @Select("select * from post where id = #{id}")
    Post selectPostByID(@Param("id") int id);


    @Insert(" insert into post(post_owner_id, post_title, post_content, upload_date, post_img_url, is_img_in, original_writer, is_post_scrapped, scrapping_id ) values (" +
            "#{postOwnerId}, #{postTitle}, #{postContent}, #{uploadDate}, #{postImgUrl}, #{isImgIn}, #{originalWriter}, true, #{scrappingId})")
    void saveScrappingPost(@Param("postOwnerId") int postOwnerId, @Param("postTitle") String postTitle, @Param("postContent") String postContent,@Param("uploadDate") String uploadDate,
                           @Param("postImgUrl") String postImgUrl, @Param("isImgIn") boolean isImgIn,@Param("originalWriter") int originalWriter,@Param("isPostScrapped") boolean isPostScrapped,@Param("scrappingId") int scrappingId);
}
