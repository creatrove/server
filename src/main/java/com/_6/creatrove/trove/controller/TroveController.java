package com._6.creatrove.trove.controller;

import com._6.creatrove.trove.dto.TroveRecentResponse;
import com._6.creatrove.trove.dto.TroveSearchResponse;
import com._6.creatrove.trove.service.TroveService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TroveController {

    private final TroveService troveService;

    @GetMapping("/trove/recent")
    public TroveRecentResponse recent(@AuthenticationPrincipal Long userId) {
        return new TroveRecentResponse(troveService.getRecent(userId));
    }

    @GetMapping("/trove/search")
    public TroveSearchResponse search(@AuthenticationPrincipal Long userId,
                                      @RequestParam String keyword) {
        return new TroveSearchResponse(keyword, troveService.search(userId, keyword));
    }
}