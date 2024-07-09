package ru.hse.moms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.hse.moms.entity.Page;
import ru.hse.moms.repository.PageRepository;
import ru.hse.moms.request.CreatePageRequest;
import ru.hse.moms.request.UpdatePageRequest;
import ru.hse.moms.response.PageResponse;
import ru.hse.moms.response.UserResponse;
import ru.hse.moms.service.PageService;

@RestController
@RequestMapping("/api/page")
@RequiredArgsConstructor
public class PageController {
    private final PageService pageService;
    @PostMapping()
    public PageResponse createPage(@RequestBody CreatePageRequest createPageRequest) {
        return pageService.createPage(createPageRequest);
    }
    @PutMapping
    public PageResponse updatePage(@RequestBody UpdatePageRequest updatePageRequest) {
        return pageService.updatePage(updatePageRequest);
    }
    @DeleteMapping
    public void deletePage(@RequestParam(name = "page_id") Long pageId) {
        pageService.deletePage(pageId);
    }
    @GetMapping
    public PageResponse getPage(@RequestParam(name = "page_id") Long pageId) {
        return pageService.getPage(pageId);
    }
}
