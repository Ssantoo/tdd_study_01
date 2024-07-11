package io.hhplus.tdd.point.infrastructure;

import io.hhplus.tdd.point.domain.UserPoint;

public interface UserPointRepository {
    UserPoint selectById(long id);

    UserPoint insertOrUpdate(UserPoint userPoint);
}
