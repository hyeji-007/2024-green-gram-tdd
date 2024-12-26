package com.green.greengram.feed.comment.model;

import com.green.greengram.common.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.ToString;

import java.beans.ConstructorProperties;

@Getter
@Schema(title = "피드 댓글 리스트 요청")
@ToString
public class FeedCommentGetReq {
    private final static int FIRST_COMMENT_SIZE = 3;

    @Positive
    @Schema(title="피드 PK", description = "피드 PK", name="feed_id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long feedId;

    @PositiveOrZero
    @Schema(title="튜플 시작 index", description = "댓글 Element 갯수를 보내주면 된다.", name="start_idx", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private int startIdx;

    @Min(value = 21, message = "사이즈는 20이상이어야 합니다.")
    @Schema(title="페이지 당 아이템 수", description = "default: 20", example = "20")
    private int size;

    @ConstructorProperties({"feed_id", "start_idx", "size"})
    public FeedCommentGetReq(long feedId, int startIdx, Integer size) {
        this.feedId = feedId;
        this.startIdx = startIdx;
        this.size = (size == null ? Constants.getDefault_page_size() : size) + 1;
    }
}