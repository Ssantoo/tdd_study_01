package io.hhplus.tdd.application;

import io.hhplus.tdd.point.application.PointService;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.infrastructure.PointHistoryRepository;
import io.hhplus.tdd.point.infrastructure.UserPointRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

    @InjectMocks
    private PointService pointService;

    @Mock
    private UserPointRepository userPointRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;


    //사용자가 존재하면 포인트를 조회한다
    //UserPoint.empty(id)
    @Test
    void 사용자의_포인트를_조회_한다(){

        //given
        //when
        UserPoint userPoint = new UserPoint(1, 0);
        given(userPointRepository.selectById(anyLong())).willReturn(userPoint);

        UserPoint result = pointService.getPoint(1);
        assertThat(result.id()).isEqualTo(1);
    }

    //사용자가 존재하지 않는다면 예외를 발생
    @Test
    void 사용자가_존재하지않는다면_예외를_던진다() {
        //given
        long id = 0L;
        given(userPointRepository.selectById(id)).willThrow(new RuntimeException("유저가 존재하지 않습니다."));

        //when
        //then
        assertThatThrownBy(() -> {
            pointService.getPoint(id);
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("유저가 존재하지 않습니다.");
    }

    //포인트 조회가 음수일 수 없다.
//    @Test
//    void 포인트_조회가_음수이면_예외를_던진다() {
//        //given
//        UserPoint userPoint = new UserPoint(1, -10);
//        given(userPointRepository.selectById(anyLong())).willReturn(userPoint);
//
//        //when
//        //then
//        assertThatThrownBy(() -> {
//            pointService.getPoint(1);
//        }).isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("포인트는 음수일 수 없다");
//    }

    //히스토리 조회
    @Test
    void 사용자의_포인트히스토리를_조회_한다(){
        //given
        List<PointHistory> histories = List.of(
                new PointHistory(1L, 4L, 100, TransactionType.CHARGE, System.currentTimeMillis()),
                new PointHistory(2L, 4L, -100, TransactionType.USE, System.currentTimeMillis())
        );
        given(pointHistoryRepository.selectAllByUserId(anyLong())).willReturn(histories);

        //when
        List<PointHistory> result = pointService.getPointHistory(4L);

        //then
        assertEquals(histories, result);
    }

    //charge
    //service에서도 amount가 음수인거 체크?
    @Test
    void 포인트를_충전한다() {
        //given
        UserPoint userPoint = new UserPoint(1L, 100L);
        given(userPointRepository.selectById(anyLong())).willReturn(userPoint);
        long amount = 100L;

        //서비스단에서 sum은 패스?!
//        UserPoint afterCharge = userPoint.sum(amount);
//        UserPoint updatedUserPoint = new UserPoint(afterCharge.id(), afterCharge.point(), System.currentTimeMillis());

        UserPoint updatedUserPoint = new UserPoint(1L, 200L);
        given(userPointRepository.insertOrUpdate(any(UserPoint.class))).willReturn(updatedUserPoint);

        //when
        UserPoint result = pointService.chargePoint(1L, amount);

        //then
        assertThat(result.point()).isEqualTo(200L);

        //히스토리 체크도? 히스토리가 1개 생긴다?
    }

    //use
    //포인트 사용
    @Test
    void 포인트를_사용한다() {
        //given
        UserPoint userPoint = new UserPoint(1L, 100L);
        given(userPointRepository.selectById(anyLong())).willReturn(userPoint);
        long amount = 50L;

        //UserPoint afterUse = userPoint.use(amount);
        UserPoint updatedUserPoint = new UserPoint(1L, 50L);

        given(userPointRepository.insertOrUpdate(any(UserPoint.class))).willReturn(updatedUserPoint);

        //when
        UserPoint result = pointService.usePoint(1L, amount);

        //then
        assertThat(result.point()).isEqualTo(50L);

    }

    //히스토리 체크도? 히스토리가 1개 생긴다?

    //point가 amount보다 작을 수 없다
    @Test
    void 포인트가_부족하면_예외를_던진다() {
        //given
        UserPoint userPoint = new UserPoint(1L, 50L);
        given(userPointRepository.selectById(anyLong())).willReturn(userPoint);
        long amount = 100L;

        //when
        //then
        assertThatThrownBy(()-> pointService.usePoint(1L, amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("포인트가 부족합니다.");

    }


}
