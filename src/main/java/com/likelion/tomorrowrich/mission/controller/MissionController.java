package com.likelion.tomorrowrich.mission.controller;

import com.likelion.tomorrowrich.mission.dto.MissionCompleteRequestDTO;
import com.likelion.tomorrowrich.mission.dto.MissionCompleteResponseDTO;
import com.likelion.tomorrowrich.mission.dto.MissionListResponseDTO;
import com.likelion.tomorrowrich.mission.service.MissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping
    public ResponseEntity<MissionListResponseDTO> getMissions(
            @RequestParam String date
    ) {
        Long userId = getLoginUserId();

        MissionListResponseDTO response = missionService.getMissions(userId, date);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{missionId}/complete")
    public ResponseEntity<MissionCompleteResponseDTO> completeMission(
            @PathVariable Long missionId,
            @RequestBody MissionCompleteRequestDTO request
    ) {
        Long userId = getLoginUserId();

        MissionCompleteResponseDTO response = missionService.completeMission(userId, missionId, request);

        return ResponseEntity.ok(response);
    }

    private Long getLoginUserId() {
        /*
         * TODO:
         * 현재는 인증 모듈 연결 전 개발/테스트용 임시 사용자 id임
         */
        return 1L;
    }
}
