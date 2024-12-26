package com.green.greengram.feed.comment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Schema(title = "피드 댓글 등록 요청")
@ToString
public class FeedCommentPostReq {
    @JsonIgnore
    private long feedCommentId;

    @Schema(title = "피드 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId;
    @JsonIgnore
    private long userId;
    @Schema(title = "댓글 내용", example = "댓글입니다.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String comment;
}