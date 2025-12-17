package com.tasktracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/endpoints")
public class AllEndpoints {

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping
    public List<String> getAllEndpoints() {
        RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        return handlerMapping.getHandlerMethods()
                .keySet()
                .stream()
                .flatMap(info -> {
                    if (info.getPathPatternsCondition() != null) {
                        return info.getPathPatternsCondition()
                                .getPatternValues()
                                .stream();
                    }
                    return Stream.empty();
                })
                .toList();
    }
}