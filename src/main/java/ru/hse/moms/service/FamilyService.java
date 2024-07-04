package ru.hse.moms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.hse.moms.entity.Family;
import ru.hse.moms.entity.User;
import ru.hse.moms.exception.FamilyNotFound;
import ru.hse.moms.exception.HostOneFamilyException;
import ru.hse.moms.exception.UserNotFoundException;
import ru.hse.moms.mapper.FamilyMapper;
import ru.hse.moms.repository.FamilyRepository;
import ru.hse.moms.repository.UserRepository;
import ru.hse.moms.request.FamilyRequest;
import ru.hse.moms.response.FamilyResponse;
import ru.hse.moms.security.AuthUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FamilyService {
    private final FamilyMapper familyMapper;
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final TypeService typeService;

    public FamilyResponse getFamily(Long familyId) {
        Long currentUserId = AuthUtils.getCurrentId();
        User currentUser = userRepository.findById(currentUserId).orElseThrow(() ->
                new UserNotFoundException("User not found"));

        Family family = familyRepository.findById(familyId).orElseThrow(() ->
                new FamilyNotFound(String.format("Family with id %d not found", familyId)));

        boolean isMember = family.getMembers().stream()
                .anyMatch(member -> member.getId().equals(currentUser.getId()));

        if (!isMember && !family.getHost().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied: You are not a member of this family");
        }
        return familyMapper.makeFamilyResponse(family);
    }

    public FamilyResponse updateFamily(FamilyRequest familyRequest) {
        Long currentUserId = AuthUtils.getCurrentId();
        assert currentUserId != null;

        User user = userRepository.findById(currentUserId).orElseThrow(
                () -> new RuntimeException("User is logged but no info in database"));
        Family family = familyRepository.findByHost(user).orElseThrow(
                () -> new RuntimeException("No hosted families found for you"));

        if (!familyRequest.getMembers().isEmpty()) {
            List<User> members = familyRequest.getMembers().stream().map(
                            memberRequest -> {
                                User member = userRepository.findById(memberRequest.getMemberId())
                                        .orElseThrow(() -> new RuntimeException("User not found"));
                                member.setType(typeService.getTypeById(memberRequest.getTypeId()).get()); // Пример, если есть метод setTypeId в User
                                return member;
                            })
                    .collect(Collectors.toList());
        }

        if (familyRequest.getHostId() != null) {
            family.setHost(userRepository.findById(familyRequest.getHostId()).orElseThrow(() ->
                    new UserNotFoundException(
                            String.format("User with id: %s was not found", familyRequest.getHostId()))));
        }
        return familyMapper.makeFamilyResponse(familyRepository.saveAndFlush(family));
    }

    public FamilyResponse createFamily(FamilyRequest familyRequest) {
        List<User> members = familyRequest.getMembers().stream()
                .map(memberRequest -> {
                    User user = userRepository.findById(memberRequest.getMemberId())
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    user.setType(typeService.getTypeById(memberRequest.getTypeId()).get());
                    return user;
                })
                .collect(Collectors.toList());

        Long currentUserId = AuthUtils.getCurrentId();
        assert currentUserId != null;

        User host = userRepository.findById(currentUserId).orElseThrow(() ->
                new UserNotFoundException(
                        String.format("User with id: %s was not found", currentUserId)));

        if (familyRepository.existsByHost(host)) {
            throw new HostOneFamilyException(String.format("User with id already hosts family with id: %s",
                    familyRepository.findByHost(host).get().getId()));
        }

        Family newFamily = Family.builder()
                .members(members)
                .host(host)
                .build();
        return familyMapper.makeFamilyResponse(familyRepository.saveAndFlush(newFamily));
    }

    public boolean deleteFamily() {
        Long currentUserId = AuthUtils.getCurrentId();
        assert currentUserId != null;
        familyRepository.deleteById(
                familyRepository.findByHost(
                        userRepository.findById(currentUserId).orElseThrow(
                                () -> new UserNotFoundException("User is logged, but no info in database"))
                ).orElseThrow(
                        () -> new FamilyNotFound(
                                String.format(
                                        "For user with id: %s no hosted families were found", currentUserId))).getId()
        );
        return true;
    }
}
