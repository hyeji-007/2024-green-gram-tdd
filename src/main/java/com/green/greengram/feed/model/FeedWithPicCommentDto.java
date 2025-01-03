package com.green.greengram.feed.model;

import com.green.greengram.feed.comment.model.FeedCommentDto;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor //기본 생성자(파라미터가 없는 생성자)를 자동으로 생성 >> 객체 생성해야 함
@AllArgsConstructor //모든 필드를 포함하는 생성자를 자동으로 생성
// 생성자 or @Setter로 값을 넣을 경우 멤버필드에 적힌 순서가 상관있으나 No(All)ArgsConstructor을 사용하면 순서가 상관 없다.
@EqualsAndHashCode
public class FeedWithPicCommentDto {
    private long feedId;
    private String contents;
    private String location;
    private String createdAt;
    private long writerUserId;
    private String writerNm;
    private String writerPic;
    private int isLike;
    private List<String> pics;
    private List<FeedCommentDto> commentList;
}