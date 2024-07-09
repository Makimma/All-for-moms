package ru.hse.moms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.moms.entity.Page;
import ru.hse.moms.entity.User;
import ru.hse.moms.exception.AccessDeniedException;
import ru.hse.moms.exception.PageNotFoundException;
import ru.hse.moms.mapper.FamilyMapper;
import ru.hse.moms.mapper.PageMapper;
import ru.hse.moms.repository.PageRepository;
import ru.hse.moms.request.CreatePageRequest;
import ru.hse.moms.request.UpdatePageRequest;
import ru.hse.moms.response.PageResponse;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PageService {
    private final PageMapper pageMapper;
    private final PageRepository pageRepository;
    private final FamilyService familyService;
    private final UserService userService;

    private boolean checkPageForUser(Long pageId) {
        User user = familyService.getCurrentUser();
        return user.getDiary().
                getPages().stream().anyMatch(page -> Objects.equals(page.getId(), pageId));
    }

    public PageResponse getPage(Long pageId) {
        if (checkPageForUser(pageId)) {
            throw new AccessDeniedException("No access to this page");
        }
        return pageMapper.makePageResponse(
                pageRepository.findById(pageId).orElseThrow(
                        () -> new PageNotFoundException(String.format("Page with id %s not found", pageId))
                )
        );
    }
    public PageResponse updatePage(UpdatePageRequest updatePageRequest) {
        Page page = pageRepository.findById(updatePageRequest.getPageId()).orElseThrow(
                () -> new PageNotFoundException(String.format("Page with id %s not found",
                        updatePageRequest.getPageId()))
        );
        if (checkPageForUser(updatePageRequest.getPageId())) {
            throw new AccessDeniedException("No access to this page");
        }
        if (updatePageRequest.getContent() != null) {
            page.setContent(updatePageRequest.getContent());
        }
        if (updatePageRequest.getTitle() != null) {
            page.setTitle(updatePageRequest.getTitle());
        }
        return pageMapper.makePageResponse(page);
    }
    public void deletePage(Long pageId) {
        pageRepository.findById(pageId).orElseThrow(
                () -> new PageNotFoundException(String.format("Page with id %s not found",
                        pageId))
        );
        if (checkPageForUser(pageId)) {
            throw new AccessDeniedException("No access to this page");
        }
        pageRepository.deleteById(pageId);
    }
    public PageResponse createPage(CreatePageRequest createPageRequest) {
        Page page = Page.builder()
                .title(createPageRequest.getTitle())
                .date(new Date())
                .content(createPageRequest.getContent())
                .title(createPageRequest.getTitle())
                .build();
        Page savedPage = pageRepository.saveAndFlush(page);
        userService.addPageToDiary(savedPage);
        return pageMapper.makePageResponse(savedPage);
    }
}
