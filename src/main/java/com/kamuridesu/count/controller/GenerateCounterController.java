package com.kamuridesu.count.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kamuridesu.count.domain.service.SVGMergerService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
class GenerateCounterController {

    private final SVGMergerService svgMergerService;

    @GetMapping("/")
    ResponseEntity<String> index(@RequestParam(name = "username", required = false) String username) throws IOException {
        if (Optional.ofNullable(username).isPresent()) {
            return ResponseEntity.ok().body(svgMergerService.merge(10));
        }
        return ResponseEntity.badRequest().body("Username missing");
    }
}
