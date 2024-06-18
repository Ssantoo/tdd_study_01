package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.infrastructure.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserPointRepository userPointRepository;

    public UserPoint getPoint(long id) {
        UserPoint userPoint = userPointRepository.selectById(id);
        if (userPoint.point() < 0) {
            throw new IllegalArgumentException("포인트는 음수일 수 없다");
        }
        return userPoint;
    }
}
