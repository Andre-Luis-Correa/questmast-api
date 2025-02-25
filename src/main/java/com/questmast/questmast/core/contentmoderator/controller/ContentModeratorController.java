package com.questmast.questmast.core.contentmoderator.controller;

import com.questmast.questmast.core.contentmoderator.domain.ContentModerator;
import com.questmast.questmast.core.contentmoderator.service.ContentModeratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/content-moderator")
@RequiredArgsConstructor
public class ContentModeratorController {

    private final ContentModeratorService contentModeratorService;

    @GetMapping
    public ResponseEntity<List<ContentModerator>> list() {
        return ResponseEntity.ok(contentModeratorService.findAll());
    }
}
