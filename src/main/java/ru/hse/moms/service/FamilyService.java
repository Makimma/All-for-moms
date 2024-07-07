package ru.hse.moms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.moms.entity.Family;
import ru.hse.moms.entity.User;
import ru.hse.moms.exception.*;
import ru.hse.moms.mapper.FamilyMapper;
import ru.hse.moms.repository.FamilyRepository;
import ru.hse.moms.repository.TypeRepository;
import ru.hse.moms.repository.UserRepository;
import ru.hse.moms.request.CreateFamilyRequest;
import ru.hse.moms.request.FamilyMember;
import ru.hse.moms.request.UpdateFamilyRequest;
import ru.hse.moms.response.FamilyResponse;
import ru.hse.moms.security.AuthUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FamilyService {
    private final FamilyMapper familyMapper;
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final TypeRepository typeRepository;

    private User getCurrentUser() {

        Long currentUserId = AuthUtils.getCurrentId();
        assert currentUserId != null;
        return userRepository.findById(currentUserId).orElseThrow(
                () -> new RuntimeException("User is logged but no info in database"));
    }

    public FamilyResponse getFamily(Long familyId) throws AccessDeniedException {
        User user = getCurrentUser();
        Family family = familyRepository.findById(familyId).orElseThrow(() ->
                new FamilyNotFound(String.format("Family with id %d not found", familyId)));
        if (!family.getMembers().contains(user)) {
            throw new AccessDeniedException("Access denied: You are not a member of this family");
        }
        return familyMapper.makeFamilyResponse(family);
    }

    public FamilyResponse addFamilyMemberOrHost(UpdateFamilyRequest familyRequest) {
        User user = getCurrentUser();
        Family family = familyRepository.findByHostsContaining(user).orElseThrow(
                () -> new AccessDeniedException("No hosted families found for you"));
        if (!familyRequest.getMembers().isEmpty()) {
            List<User> members = familyRequest.getMembers().stream().map(this::getUserWithType).toList();
            List<User> familyMembers = members.stream().filter(member -> !family.getMembers().contains(member)).toList();
            family.getMembers().addAll(familyMembers);
        }
        if (familyRequest.getHosts() != null) {
            List<User> hosts = familyRequest.getHosts().stream().map(this::getUserWithType).toList();
            List<User> uniqueHosts = hosts.stream().filter(host -> !family.getHosts().contains(host)).toList();
            family.getMembers().addAll(uniqueHosts);
            family.getHosts().addAll(uniqueHosts);
        }
        return familyMapper.makeFamilyResponse(familyRepository.saveAndFlush(family));
    }

    public FamilyResponse createFamily(CreateFamilyRequest createFamilyRequest) {
        List<User> members = createFamilyRequest.getMembersId().stream().map(this::getUserWithType)
                .collect(Collectors.toList());
        User host = getCurrentUser();

        host.setType(typeRepository.findById(createFamilyRequest.getTypeIdForHost()).orElseThrow(() ->
                new TypeNotFoundException(
                        String.format("Type with id %d not found", createFamilyRequest.getTypeIdForHost()))));

        if (familyRepository.existsByHostsContains(host)) {
            throw new HostOneFamilyException(String.format("User with id already hosts family with id: %s",
                    familyRepository.findByHostsContaining(host).get().getId()));
        }
        members.add(host);
        Family newFamily = Family.builder()
                .members(members)
                .hosts(List.of(host))
                .build();
        return familyMapper.makeFamilyResponse(familyRepository.saveAndFlush(newFamily));
    }

    public boolean deleteFamily() {
        Long currentUserId = AuthUtils.getCurrentId();
        assert currentUserId != null;
        familyRepository.deleteById(
                familyRepository.findByHostsContaining(
                        userRepository.findById(currentUserId).orElseThrow(
                                () -> new UserNotFoundException("User is logged, but no info in database"))
                ).orElseThrow(
                        () -> new FamilyNotFound(
                                String.format(
                                        "For user with id: %s no hosted families were found", currentUserId))).getId()
        );
        return true;
    }

    private User getUserWithType(FamilyMember member) {
        User userToAddType = userRepository.findById(member.getUserId()).orElseThrow(() ->
                new UserNotFoundException(
                        String.format("User with id: %s was not found", member.getUserId())));
        userToAddType.setType(typeRepository.findById(member.getTypeId()).orElseThrow(() ->
                new TypeNotFoundException(
                        String.format("Type with id %d not found", member.getTypeId()))));
        return userToAddType;
    }

    public FamilyResponse deleteFamilyMembersOrHosts(UpdateFamilyRequest familyRequest) {
        User user = getCurrentUser();
        Family family = familyRepository.findByHostsContaining(user).orElseThrow(
                () -> new AccessDeniedException("No hosted families found for you"));
        if (!familyRequest.getMembers().isEmpty()) {
            List<User> members = familyRequest.getMembers().stream().map(this::getUserWithType).toList();
            // check whether there are host members in list to delete members
            List<User> membersNoHosts = members.stream()
                    .filter(member -> !family.getHosts().contains(member)).toList();
            List<User> familyMembersToDelete = membersNoHosts.stream()
                    .filter(member -> family.getMembers().contains(member)).toList();
            family.getMembers().removeAll(familyMembersToDelete);
        }
        if (familyRequest.getHosts() != null) {
            List<User> hosts = familyRequest.getHosts().stream().map(this::getUserWithType).toList();
            List<User> hostsToDelete = hosts.stream().filter(host -> family.getHosts().contains(host)).toList();
            family.getMembers().removeAll(hostsToDelete);
            family.getHosts().removeAll(hostsToDelete);
        }
        return familyMapper.makeFamilyResponse(familyRepository.saveAndFlush(family));
    }
}


