package io.hhplus.tdd.controller;

import io.hhplus.tdd.point.application.PointService;
import io.hhplus.tdd.point.domain.UserPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PointService pointService;

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


}
