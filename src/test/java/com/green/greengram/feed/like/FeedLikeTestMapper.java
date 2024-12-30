package com.green.greengram.feed.like;

import com.green.greengram.feed.like.model.FeedLikeReq;
import com.green.greengram.feed.like.model.FeedLikeVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FeedLikeTestMapper {
    @Select("SELECT * FROM feed_like WHERE feed_id = #{feedId} AND user_id = #{userId}") //객체가 하나일 때는 null값 넘어올 수 있다.
    FeedLikeVo selFeedLikeByFeedIdAndUserId(FeedLikeReq p);

    @Select("SELECT * FROM feed_like") //List때에는 null값 넘어오지 않는다. 0개짜리 ArrayList가 넘어온다.
    List<FeedLikeVo> selFeedLikeAll();

    @Delete("DELETE FROM feed_like WHERE feed_id = #{feedId} AND user_id = #{userId}")
    int delFeedLikeByFeedIdAndUserId(FeedLikeReq p);

}
