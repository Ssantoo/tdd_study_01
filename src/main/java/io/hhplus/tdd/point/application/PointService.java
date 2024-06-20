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
        //        if (userPoint.point() < 0) {
//            throw new IllegalArgumentException("포인트는 음수일 수 없다");
//        }
        return userPointRepository.selectById(id);
    }

    public List<PointHistory> getPointHistory(long userId) {
        return pointHistoryRepository.selectAllByUserId(userId);
    }

    public UserPoint chargePoint(long userId, long amount) {
        //불변 보장을 위한 재할당막기
//        final UserPoint userPoint = userPointRepository.selectById(userId).sum(amount);
        final UserPoint userPoint = getPoint(userId).sum(amount);
        //final UserPoint afterCharge = userPoint.sum(amount);
        final UserPoint updatedUserPoint = userPointRepository.insertOrUpdate(userPoint);

        PointHistory pointHistory = new PointHistory(0L, userId, amount, TransactionType.CHARGE, System.currentTimeMillis());
        pointHistoryRepository.insert(pointHistory);
        return updatedUserPoint;
    }

    public UserPoint usePoint(long userId, long amount) {
//        final UserPoint userPoint = userPointRepository.selectById(userId);
        final UserPoint userPoint = getPoint(userId).use(amount);
        //final UserPoint afterUse = userPoint.use(amount);
        final UserPoint updatedUserPoint = userPointRepository.insertOrUpdate(userPoint);

        PointHistory pointHistory = new PointHistory(0L, userId, amount, TransactionType.USE, System.currentTimeMillis());
        pointHistoryRepository.insert(pointHistory);
        return updatedUserPoint;
    }



}
