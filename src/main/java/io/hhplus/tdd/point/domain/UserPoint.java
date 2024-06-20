package io.hhplus.tdd.point.domain;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    public UserPoint(long id, long point) {
        this(id, point, System.currentTimeMillis());
    }

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public UserPoint sum(long amount) {
        return new UserPoint(id, point+amount, System.currentTimeMillis());
    }

    public UserPoint use(long amount) {
        checkPoint(amount);
        return new UserPoint(id, point-amount, System.currentTimeMillis());
    }

    private void checkPoint(long amount) {
        if (point < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
    }

}
