package ru.hse.moms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.moms.entity.Family;
import ru.hse.moms.entity.User;
import ru.hse.moms.exception.FamilyNotFound;
import ru.hse.moms.exception.HostOneFamilyException;
import ru.hse.moms.exception.UserNotFoundException;
import ru.hse.moms.mapper.FamilyMapper;
import ru.hse.moms.repository.FamilyRepository;
import ru.hse.moms.repository.UserRepository;
import ru.hse.moms.request.CreateFamilyRequest;
import ru.hse.moms.request.UpdateFamilyRequest;
import ru.hse.moms.response.FamilyResponse;
import ru.hse.moms.security.AuthUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FamilyService {
    private final FamilyMapper familyMapper;
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;

    public FamilyResponse getFamily(Long familyId) {
        Family family = familyRepository.findById(familyId).orElseThrow(() ->
                new FamilyNotFound(String.format("Family with id %d not found", familyId)));
        return familyMapper.makeFamilyResponse(family);
    }

    public FamilyResponse addFamilyMemberHost(UpdateFamilyRequest familyRequest) {
        Long currentUserId = AuthUtils.getCurrentId();
        assert currentUserId != null;
        User user = userRepository.findById(currentUserId).orElseThrow(
                () -> new RuntimeException("User is logged but no info in database"));
        Family family = familyRepository.findByHostsContaining(user).orElseThrow(
                () -> new RuntimeException("No hosted families found for you"));
        if (!familyRequest.getMembersId().isEmpty()) {
            List<User> members = familyRequest.getMembersId().stream().map(
                    (userId) -> userRepository.findById(userId).orElseThrow(() ->
                            new UserNotFoundException(
                                    String.format("User with id: %s was not found", userId)))).toList();
            List<User> familyMembers = members.stream().filter(member -> !family.getMembers().contains(user)).toList();

            family.getMembers().addAll(familyMembers);
        }
        if (familyRequest.getHostsId() != null) {
            List<User> hosts = familyRequest.getHostsId().stream().map((hostId) ->
                    userRepository.findById(hostId).orElseThrow(() ->
                            new UserNotFoundException(
                                    String.format("User with id: %s was not found",
                                            familyRequest.getHostsId())))).toList();
            List<User> uniqueHosts = hosts.stream().filter(host -> !family.getHosts().contains(host)).toList();
            family.getHosts().addAll(uniqueHosts);
        }
        return familyMapper.makeFamilyResponse(familyRepository.saveAndFlush(family));
    }

    public FamilyResponse createFamily(CreateFamilyRequest createFamilyRequest) {
        List<User> members = createFamilyRequest.getMembersId().stream().map(
                (userId) -> userRepository.findById(userId).orElseThrow(() ->
                        new UserNotFoundException(
                                String.format("User with id: %s was not found", userId)))).toList();

        Long currentUserId = AuthUtils.getCurrentId();
        assert currentUserId != null;
        User host = userRepository.findById(currentUserId).orElseThrow(() ->
                new UserNotFoundException(
                        String.format("User with id: %s was not found", currentUserId)));
        if (familyRepository.existsByHostsContains(host)) {
            throw new HostOneFamilyException(String.format("User with id already hosts family with id: %s",
                    familyRepository.findByHostsContaining(host).get().getId()));
        }
        Family newFamily = Family.builder()
                .members(members)
                .build();
        newFamily.getHosts().add(host);
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

    public FamilyResponse deleteMembersHosts(UpdateFamilyRequest updateFamilyRequest) {

    }


}
