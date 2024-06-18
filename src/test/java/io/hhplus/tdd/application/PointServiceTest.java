package io.hhplus.tdd.application;

import io.hhplus.tdd.point.application.PointService;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.infrastructure.PointHistoryRepository;
import io.hhplus.tdd.point.infrastructure.UserPointRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
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
        UserPoint userPoint = new UserPoint(1, 0, System.currentTimeMillis());
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
    @Test
    void 포인트_조회가_음수이면_예외를_던진다() {
        //given
        UserPoint userPoint = new UserPoint(1, -10, System.currentTimeMillis());
        given(userPointRepository.selectById(anyLong())).willReturn(userPoint);

        //when
        //then
        assertThatThrownBy(() -> {
            pointService.getPoint(1);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("포인트는 음수일 수 없다");
    }
}
