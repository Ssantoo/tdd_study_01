package io.hhplus.tdd.point.domain;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public long sum(long amount) {
        return point + amount;
    }

    public long use(long amount) {
        checkPoint(amount);
        return point - amount;
    }

    private void checkPoint(long amount) {
        if (point < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
    }

}
