package com.green.greengram.feed.like;

import com.green.greengram.TestUtils;
import com.green.greengram.feed.like.model.FeedLikeReq;
import com.green.greengram.feed.like.model.FeedLikeVo;
import org.junit.jupiter.api.*;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") //yaml 적용되는 파일 선택 (application-test.yml)
@MybatisTest //Mybatis Mapper Test이기 때문에 작성 >> Mapper 들이 전부 객체화
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//테스트를 기본적으로 메모리 데이터베이스 (H2)를 사용해서 하는데 메모리 데이터베이스로 교체하지 않겠다.
//즉, 우리가 원래 쓰는 데이터베이스로 테스트를 진행하겠다.
//@TestInstance(TestInstance.Lifecycle.PER_CLASS) //기본값은 PER_METHOD 메소드마다 객체를 만든다, static 삭제 가능
//PER_CLASS 설정이 있으면 static을 제거해도 된다.
//테스트 객체를 딱 하나만 만든다.
class FeedLikeMapperTest {

    @Autowired // TODO: 스프링 컨테이너가 DI해주는게 맞는지 확인, @Autowired는 필드 주입 방식이다.
    FeedLikeMapper feedLikeMapper; // @Autowired >> 필드 주입 방식의 DI가 된다.
    // DI 방법: 생성자/필드 주입/세터 주입이 있는데 Test는 생성자를 통한 DI가 안된다.

    @Autowired
    FeedLikeTestMapper feedLikeTestMapper;

    static final long FEED_ID_1 = 1L;
    static final long FEED_ID_5 = 5L;
    static final long USER_ID_2 = 2L;

    static final FeedLikeReq existedData = new FeedLikeReq();
    static final FeedLikeReq notExistedData = new FeedLikeReq();
    /*
        @BeforeAll - 모든 테스트 실행 전에 최초 한번 실행
        ---
        @BeforeEach - 각 테스트 실행 전에 실행
        @Test
        @AfterEach - 각 테스트 실행 후에 실행
        ---
        @AfterAll - 모든 테스트 실행 후에 최초 한번 실행
     */

    // @BeforeAll - 모든 테스트 메소드 실행되기 전에 최초 딱 한번만 실행되는 메소드
    // 테스트 메소드마다 테스트 객체가 만들어지면 BeforeAll 메소드는 무조건 static 메소드여야 한다.
    // 한 테스트 객체가 만들어지면 non-static 메소드일 수 있다.
    @BeforeAll //최초 딱 한번만 실행
    static void initData() {
        existedData.setFeedId(FEED_ID_1);
        existedData.setUserId(USER_ID_2);

        notExistedData.setFeedId(FEED_ID_5);
        notExistedData.setUserId(USER_ID_2);

    }

    // @BeforeEach - 테스트 메소드 마다 테스트 메소드 실행 전에 실행되는 before메소드 (각각 실행됨)
    // before메소드


    @Test
    @DisplayName("중복된 데이터 입력시 DuplicatedKeyException 발생 체크")
    void insFeedLikeDuplicateDataThrowDuplicateKeyException() {
        // 중복된 데이터 입력시 DuplicatedKeyException 발생 체크
        assertThrows(DuplicateKeyException.class, () -> {
            feedLikeMapper.insFeedLike(existedData);
        }, "데이터 중복시 에러 발생되지 않음 > Primary Key(feed_ud, user_id) 확인 바람");

    }

    @Test
    void insFeedLike() {

        //when
        List<FeedLikeVo> actualFeedLikeListBefore = feedLikeTestMapper.selFeedLikeAll(); // insert 전 기존 튜플 수
        FeedLikeVo actualFeedLikeVoBefore = feedLikeTestMapper.selFeedLikeByFeedIdAndUserId(notExistedData); //insert 전 WHERE절에 PK로 데이터를 가져옴
        int actualAffectedRows = feedLikeMapper.insFeedLike(notExistedData);
        List<FeedLikeVo> actualFeedLikeListAfter = feedLikeTestMapper.selFeedLikeAll(); //insert 후 WHERE절에 PK로 데이터를 가져옴
        FeedLikeVo actualFeedLikeVoAfter = feedLikeTestMapper.selFeedLikeByFeedIdAndUserId(notExistedData); //insert 후 튜플 수

        //then
        assertAll(
                  () -> TestUtils.assertCurrentTimestamp(actualFeedLikeVoAfter.getCreatedAt())
                , () -> assertEquals(actualFeedLikeListBefore.size() + 1, actualFeedLikeListAfter.size())
                , () -> assertNull(actualFeedLikeVoBefore) //내가 insert하려고 하는 데이터가 없었는지 단언
                , () -> assertNotNull(actualFeedLikeVoAfter) //실제 insert가 내가 원하는 데이터로 되었는지 단언
                , () -> assertEquals(1, actualAffectedRows)
                , () -> assertEquals(notExistedData.getFeedId(), actualFeedLikeVoAfter.getFeedId()) //내가 원하는 데이터로 insert 되었는지 더블 체크
                , () -> assertEquals(notExistedData.getUserId(), actualFeedLikeVoAfter.getUserId())
        );
    }

    @Test
    void delFeedLikeNoData() {

        int actualAffectedRows = feedLikeMapper.delFeedLike(notExistedData);
        assertEquals(0, actualAffectedRows);
    }

    @Test
    void delFeedLike() {
        FeedLikeVo actualFeedLikeVoBefore = feedLikeTestMapper.selFeedLikeByFeedIdAndUserId(existedData);
        int actualAffectedRows = feedLikeMapper.delFeedLike(existedData);
        FeedLikeVo actualFeedLikeVoAfter = feedLikeTestMapper.selFeedLikeByFeedIdAndUserId(existedData);

        // then
        assertAll(
                  () -> assertEquals(1, actualAffectedRows)
                , () -> assertNotNull(actualFeedLikeVoBefore)
                , () -> assertNull(actualFeedLikeVoAfter)
        );


    }
}



