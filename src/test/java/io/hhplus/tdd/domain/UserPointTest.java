package io.hhplus.tdd.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.hhplus.tdd.point.domain.UserPoint;
import org.junit.jupiter.api.Test;

public class UserPointTest {

    //sum, use test
    @Test
    void 포인트를_충전한다() {
        //given
        UserPoint userPoint = new UserPoint(1L, 100L);

        //when
        UserPoint updatedUserPoint = userPoint.sum(50L);

        //then
        assertThat(updatedUserPoint.point()).isEqualTo(150L);
        assertThat(updatedUserPoint.id()).isEqualTo(1L);
    }

    @Test
    void 포인트를_사용한다() {
        //given
        UserPoint userPoint = new UserPoint(1L, 100L);

        //when
        UserPoint updatedUserPoint = userPoint.use(50L);

        //then
        assertThat(updatedUserPoint.point()).isEqualTo(50L);
        assertThat(updatedUserPoint.id()).isEqualTo(1L);
    }

    @Test
    void 포인트가_부족하면_예외를_던진다() {
        //given
        UserPoint userPoint = new UserPoint(1L, 50L);

        //when
        //then
        assertThatThrownBy(() -> userPoint.use(100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("포인트가 부족합니다.");
    }

    @Test
    void empty_메서드는_포인트가_0인_UserPoint_객체를_반환한다() {
        //given
        long userId = 1L;

        //when
        UserPoint emptyUserPoint = UserPoint.empty(userId);

        //then
        assertThat(emptyUserPoint.point()).isEqualTo(0L);
        assertThat(emptyUserPoint.id()).isEqualTo(userId);
        assertThat(emptyUserPoint.updateMillis()).isGreaterThan(0L);
    }

}
