package com.kamuridesu.count.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kamuridesu.count.domain.model.User;
import com.kamuridesu.count.domain.respository.UserRepository;
import com.kamuridesu.count.domain.service.SVGMergerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class GenerateCounterController implements WebMvcConfigurer {

    private final SVGMergerService svgMergerService;
    private final UserRepository userRepository;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static")
                .addResourceLocations("file:/static");
    }

    @GetMapping("/")
    ModelAndView index(@RequestParam(name = "username", required = false) String username)
            throws IOException {
        ModelAndView modelAndView = new ModelAndView("count");
        if (Optional.ofNullable(username).isPresent()) {
            var user = userRepository.findByUsername(username)
                    .map(u -> {
                        u.setCounter(u.getCounter() + 1);
                        return userRepository.save(u);
                    })
                    .orElseGet(() -> {
                        var newUser = new User();
                        newUser.setUsername(username);
                        newUser.setCounter(0L);
                        return userRepository.save(newUser);
                    });
            modelAndView.addObject("svg", svgMergerService.merge(user.getCounter().intValue()));
            return modelAndView;
        }
        modelAndView.setViewName("error");
        modelAndView.addObject("message", "Error! Missing username param");
        return modelAndView;
    }
}
