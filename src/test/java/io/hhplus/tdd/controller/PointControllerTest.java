package io.hhplus.tdd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.tdd.point.application.PointService;
import io.hhplus.tdd.point.controller.PointController;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;


@AutoConfigureMockMvc
@WebMvcTest(PointController.class)
@ExtendWith(MockitoExtension.class)
public class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PointService pointService;

    @Autowired
    private ObjectMapper objectMapper;

    //- GET `/point/{id}` : 포인트를 조회한다.
    @Test
    void 포인트를_조회한다() throws Exception {
        //given
        long id = 1L;
        UserPoint userPoint = new UserPoint(id, 0, System.currentTimeMillis());
        given(pointService.getPoint(id)).willReturn(userPoint);
        //when
        //then
        mockMvc.perform(get("/point/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.point").value(0))
                .andExpect(jsonPath("$.updateMillis").exists());
    }

    //유저가 아닐 경우
    @Test
    void 유저가_아닐_경우_포인트를_조회할_수_없다()throws Exception {
        //given
        long id = 0L;
        given(pointService.getPoint(id)).willThrow(new RuntimeException("유저가 존재하지 않습니다."));

        //when
        //then
        mockMvc.perform(get("/point/{id}", id))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("500"))
                .andExpect(jsonPath("$.message").value("에러가 발생했습니다."));
    }

    //포인트 히스토리를 조회한다
    @Test
    void 포인트_히스토리를_조회한다() throws Exception {
        //given
        long userId = 1L;
        List<PointHistory> histories = List.of(
                new PointHistory(1L, userId, 100, TransactionType.CHARGE, System.currentTimeMillis()),
                new PointHistory(2L, userId, -100, TransactionType.USE, System.currentTimeMillis())
        );
        given(pointService.getPointHistory(userId)).willReturn(histories);

        //when
        //then
        mockMvc.perform(get("/point/{id}/histories", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].amount").value(100))
                .andExpect(jsonPath("$[0].type").value("CHARGE"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].userId").value(userId))
                .andExpect(jsonPath("$[1].amount").value(-100))
                .andExpect(jsonPath("$[1].type").value("USE"));
    }

    //유저가 아닐 경우
    @Test
    void 유저가_아닐_경우_포인트히스토리를_조회할_수_없다()throws Exception {
        //given
        long userId = 0L;
        given(pointService.getPointHistory(userId)).willThrow(new RuntimeException("유저가 존재하지 않습니다."));

        //when
        //then
        mockMvc.perform(get("/point/{id}/histories", userId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("500"))
                .andExpect(jsonPath("$.message").value("에러가 발생했습니다."));
    }

    //유저의 포인트를 충전할 수 있다
    //100 충전하고 나면 300이 된다
    @Test
    void 특정_유저의_포인트를_충전한다() throws Exception {
        // Given
        UserPoint userPoint = new UserPoint(1L, 300, System.currentTimeMillis());
        given(pointService.chargePoint(1L, 100)).willReturn(userPoint);

        // when
        // then
        mockMvc.perform(patch("/point/{id}/charge", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.point").value(300));
    }

    //포인트가 음수일 수 없다.
    @Test
    void 충전_값이_음수일_수_없다() throws Exception {
        // given
        long userId = 1L;
        long amount = -100;

        // when
        // then
        mockMvc.perform(patch("/point/{id}/charge", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amount)))
                .andExpect(jsonPath("$.code").value("500"))
                .andExpect(jsonPath("$.message").value("에러가 발생했습니다."));
    }

    //유저가 존재하지 않는다면 충전할 수 없다
    @Test
    void 유저가_존재하지_않는다면_충전할_수_없다() throws Exception {
        // given
        long userId = 0L;
        long amount = 100L;
        given(pointService.chargePoint(userId, amount)).willThrow(new RuntimeException("유저가 존재하지 않습니다."));

        // when
        // then
        mockMvc.perform(patch("/point/{id}/charge", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amount)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("에러가 발생했습니다."));
    }

    //유저의 포인트를 사용할 수 있다
    //100 사용하고 나면 200이 된다
    @Test
    void 특정_유저의_포인트를_사용한다() throws Exception {
        // Given
        UserPoint userPoint = new UserPoint(1L, 300, System.currentTimeMillis());
        given(pointService.usePoint(1L, 100)).willReturn(userPoint);

        // when
        // then
        mockMvc.perform(patch("/point/{id}/use", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.point").value(300));
    }

    //포인트가 음수일 수 없다.
    @Test
    void 사용_값이_음수일_수_없다() throws Exception {
        // given
        long userId = 1L;
        long amount = -100;

        // when
        // then
        mockMvc.perform(patch("/point/{id}/use", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amount)))
                .andExpect(jsonPath("$.code").value("500"))
                .andExpect(jsonPath("$.message").value("에러가 발생했습니다."));
    }

    //유저가 존재하지 않는다면 사용할 수 없다
    @Test
    void 유저가_존재하지_않는다면_사용_할_수_없다() throws Exception {
        // given
        long userId = 0L;
        long amount = 100L;
        given(pointService.usePoint(userId, amount)).willThrow(new RuntimeException("유저가 존재하지 않습니다."));

        // when
        // then
        mockMvc.perform(patch("/point/{id}/use", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amount)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("에러가 발생했습니다."));
    }



}
