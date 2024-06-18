package io.hhplus.tdd.point.infrastructure;

import io.hhplus.tdd.point.domain.PointHistory;

import java.util.List;

public interface PointHistoryRepository {
    List<PointHistory> selectAllByUserId(long userId);
}
