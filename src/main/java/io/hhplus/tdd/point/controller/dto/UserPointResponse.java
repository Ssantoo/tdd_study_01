package io.hhplus.tdd.point.controller.dto;

import io.hhplus.tdd.point.domain.UserPoint;

public record UserPointResponse(long id, long point, long updateMillis) {

    public static UserPointResponse from(UserPoint userPoint) {
        return new UserPointResponse(userPoint.id(), userPoint.point(), userPoint.updateMillis());
    }
}
