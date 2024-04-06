package org.schoolPicks.api.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schoolPicks.api.service.MockMvcService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MockMvcController {

    private final MockMvcService mockMvcService;

    @GetMapping("/mockMvc")
    public ResponseEntity<Object> mockMvc(@RequestParam(value = "user", required = false) String user){
        if(user == null){
            return ResponseEntity.ok(mockMvcService.message());
        }

        if(user.equals("mock")){
            throw new IllegalArgumentException("적절하지 않은 회원입니다.");
        }
        if(user.equals("response")){
            return ResponseEntity.ok(new responseDto("Request", "response"));
        }
        return ResponseEntity.ok(mockMvcService.messageWithName(user));
    }

    @Getter
    static class responseDto{

        String status;
        String user;

        public responseDto(String status, String user) {
            this.status = status;
            this.user = user;
        }
    }
}
