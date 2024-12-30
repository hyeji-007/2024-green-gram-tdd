package com.green.greengram.feed.like.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
/*
    immutable(불변성)하게 객체를 만들고 싶다. 그러면 setter을 빼야함.
    private한 멤버필드에 값 넣는 방법 2가지 (생성자, setter)
    setter를 빼기로 했기 때문에 남은 선택지는 생성자만 남았다.
    생성자를 이용해서 객체 생성을 해야 하는데 멤버필드값을 세팅하는
    경우의 수가 많을 수 있다.
    1. feedId만 세팅한다.
    2. userId만 세팅한다.
    3. createdAt만 세팅한다.
    4. feedId, userId만 세팅한다.
    5. feedId, createdAt만 세팅한다.
    6. userId, createdAt만 세팅한다.
    7. feedId, userId, createdAt를 세팅한다.
    8. 하나도 세팅안한다. >> 기본 생성자
 */

@Getter
@EqualsAndHashCode
public class FeedLikeVo { //immutable >> Vo, mutable >> Dto
    private long feedId;
    private long userId;
    private String createdAt;

    //오버라이딩은 부모가 가지고 있는 메소드 선언부 똑같이 적어야 한다. 상위 클래스가 가지고 있는 메소드를 하위 클래스가 재정의해서 사용

    //오버로딩은 메소드의 이름은 같고 매개변수의 유형과 개수가 다르도록 하는 것
}
