package io.hhplus.tdd.point.infrastructure;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPointRepositoryImpl implements UserPointRepository {

    private final UserPointTable userPointTable;

    @Override
    public UserPoint selectById(long id) {
        return userPointTable.selectById(id);
    }

    @Override
    public UserPoint insertOrUpdate(long userId, long afterCharge) {
        return userPointTable.insertOrUpdate(userId, afterCharge);
    }
}
