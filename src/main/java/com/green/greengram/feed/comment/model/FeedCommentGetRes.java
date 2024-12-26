package com.green.greengram.feed.comment.model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(title = "피드 댓글")
public class FeedCommentGetRes {
    @Schema(title = "피드 댓글 더보기 여부")
    private boolean moreComment;
    @Schema(title = "피드 댓글 리스트")
    private List<FeedCommentDto> commentList;
}