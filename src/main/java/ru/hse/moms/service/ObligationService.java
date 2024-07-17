package ru.hse.moms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.moms.entity.Obligation;
import ru.hse.moms.entity.User;
import ru.hse.moms.exception.ObligationNotFoundException;
import ru.hse.moms.exception.UserNotFoundException;
import ru.hse.moms.mapper.ObligationMapper;
import ru.hse.moms.repository.ObligationRepository;
import ru.hse.moms.repository.UserRepository;
import ru.hse.moms.response.ObligationResponse;
import ru.hse.moms.security.AuthUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObligationService {
    private final ObligationRepository obligationRepository;
    private final ObligationMapper obligationMapper;
    private final UserRepository userRepository;

    public ObligationResponse getObligation(Long obligationId) {
        Obligation obligation = obligationRepository.findById(obligationId).orElseThrow(() ->
                new ObligationNotFoundException(String.format("Obligation with id %s not found", obligationId))
        );
        return obligationMapper.makeObligationResponse(obligation);
    }

    public ObligationResponse changeStateOfObligation(Long obligationId, boolean state) {
        Obligation obligation = obligationRepository.findById(obligationId).orElseThrow(() ->
                new ObligationNotFoundException(String.format("Obligation with id %s not found", obligationId))
        );
        obligation.setTransferred(state);
        return obligationMapper.makeObligationResponse(obligationRepository.saveAndFlush(obligation));
    }

    public List<ObligationResponse> getAllCreditors() {
        Long userId = AuthUtils.getCurrentId();
        assert userId != null;
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                String.format("User with id %d is registered but not found in database", userId)));
        List<Obligation> obligationsCreditors =  obligationRepository.findAllFromUser(user);
        return obligationsCreditors.stream().map(obligationMapper::makeObligationResponse).toList();
    }

    public List<ObligationResponse> getAllDebts() {
        Long userId = AuthUtils.getCurrentId();
        assert userId != null;
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                String.format("User with id %d is registered but not found in database", userId)));
        List<Obligation> obligationsDebts =  obligationRepository.findAllToUser(user);
        return obligationsDebts.stream().map(obligationMapper::makeObligationResponse).toList();
    }


}
