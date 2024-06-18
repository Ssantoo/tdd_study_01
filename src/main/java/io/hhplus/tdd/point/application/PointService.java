package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.infrastructure.PointHistoryRepository;
import io.hhplus.tdd.point.infrastructure.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public UserPoint getPoint(long id) {
        UserPoint userPoint = userPointRepository.selectById(id);
        if (userPoint.point() < 0) {
            throw new IllegalArgumentException("포인트는 음수일 수 없다");
        }
        return userPoint;
    }

    public List<PointHistory> getPointHistory(long userId) {
        return pointHistoryRepository.selectAllByUserId(userId);
    }

    public UserPoint chargePoint(long userId, long amount) {
        UserPoint userPoint = userPointRepository.selectById(userId);
        long afterCharge = userPoint.sum(amount);
        userPoint = userPointRepository.insertOrUpdate(userId, afterCharge);

        PointHistory pointHistory = new PointHistory(0L, userId, amount, TransactionType.CHARGE, System.currentTimeMillis());
        pointHistoryRepository.insert(pointHistory);

        return userPoint;
    }

    public UserPoint usePoint(long userId, long amount) {
        UserPoint userPoint = userPointRepository.selectById(userId);

        long afterUse = userPoint.use(amount);
        userPoint = userPointRepository.insertOrUpdate(userId, afterUse);

        PointHistory pointHistory = new PointHistory(0L, userId, amount, TransactionType.USE, System.currentTimeMillis());
        pointHistoryRepository.insert(pointHistory);
        return userPoint;
    }

}
