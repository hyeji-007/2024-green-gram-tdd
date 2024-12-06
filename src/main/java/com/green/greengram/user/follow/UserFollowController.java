package com.green.greengram.user.follow;

import com.green.greengram.common.model.ResultResponse;
import com.green.greengram.user.follow.model.UserFollowReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("user/follow")
public class UserFollowController {
    private final UserFollowService service;

    //팔로우 신청
    //@requestBody를 쓴다는 것은 요청을 보내는 자가 body에 json형태의 데이터를 담아서 보낸다는 뜻
    @PostMapping
    public ResultResponse<Integer> postUserFollow(@RequestBody UserFollowReq p) {
        log.info("UserFollowController > postUserFollow > p: {}", p);
        int result = service.postUserFollow(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("팔로우 완료")
                .resultData(result)
                .build();
    }

    //팔로우 취소
    //요청을 보내는 자가 데이터를 어떻게 보내나? Query-String
    @DeleteMapping
    public ResultResponse<Integer> deleteUserFollow(@ParameterObject @ModelAttribute UserFollowReq p) {
        log.info("UserFollowController > deleteUserFollow > p:{}", p);
        int result = service.delUserFollow(p);

        return ResultResponse.<Integer>builder()
                .resultMessage("팔로우 취소")
                .resultData(result)
                .build();
    }

    /*
    @DeleteMapping
    public ResultResponse<Integer> delFeedComment(@ParameterObject @ModelAttribute FeedCommentDelReq p) {
        log.info("FeedCommentController > delFeedComment > p: {}", p);
        int res = service.delFeedComment(p);
        return ResultResponse.<Integer>builder()
                .resultMessage("댓글 삭제가 완료되었습니다.")
                .resultData(res)
                .build();
    }
     */
}