package io.hhplus.tdd.point.controller;

import io.hhplus.tdd.point.application.PointService;
import io.hhplus.tdd.point.controller.dto.PointHistoryResponse;
import io.hhplus.tdd.point.controller.dto.UserPointResponse;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.UserPoint;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);
    private final PointService pointService;

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
//    @GetMapping("{id}")
//    public UserPoint point(
//            @PathVariable long id
//    ) {
//        return new UserPoint(0, 0, 0);
//    }
    @GetMapping("/{id}")
    public ResponseEntity<UserPointResponse> getPoint(@PathVariable long id) {
        UserPoint userPoint = pointService.getPoint(id);
        UserPointResponse response = UserPointResponse.from(userPoint);
        return ResponseEntity.ok(response);
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
//    @GetMapping("{id}/histories")
//    public List<PointHistory> history(
//            @PathVariable long id
//    ) {
//        return List.of();
//    }

    @GetMapping("/{id}/histories")
    public ResponseEntity<List<PointHistoryResponse>> history(@PathVariable long id) {
        List<PointHistory> pointHistory = pointService.getPointHistory(id);
        List<PointHistoryResponse> response = pointHistory.stream()
                .map(PointHistoryResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    //    @PatchMapping("{id}/charge")
//    public UserPoint charge(
//            @PathVariable long id,
//            @RequestBody long amount
//    ) {
//        return new UserPoint(0, 0, 0);
//    }

    @PatchMapping("{id}/charge")
    public ResponseEntity<UserPointResponse> charge(
            @PathVariable long id,
            @RequestBody long amount
    ) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전금액은 음수일 수 없습니다.");
        }
        UserPoint userPoint = pointService.chargePoint(id, amount);
        UserPointResponse response = UserPointResponse.from(userPoint);
        return ResponseEntity.ok(response);
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
//    @PatchMapping("{id}/use")
//    public UserPoint use(
//            @PathVariable long id,
//            @RequestBody long amount
//    ) {
//        return new UserPoint(0, 0, 0);
//    }
    @PatchMapping("{id}/use")
    public ResponseEntity<UserPointResponse> use(
            @PathVariable long id,
            @RequestBody long amount
    ) {
        UserPoint userPoint = pointService.usePoint(id, amount);
        UserPointResponse response = UserPointResponse.from(userPoint);
        return ResponseEntity.ok(response);
    }
}
