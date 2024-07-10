package ru.hse.moms.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.moms.entity.Item;
import ru.hse.moms.entity.Reservation;
import ru.hse.moms.entity.User;
import ru.hse.moms.exception.UserNotFoundException;
import ru.hse.moms.repository.ReservationRepository;
import ru.hse.moms.repository.ItemRepository;
import ru.hse.moms.repository.UserRepository;
import ru.hse.moms.response.ReservationResponse;
import ru.hse.moms.security.AuthUtils;

import java.util.List;
import java.util.stream.Collectors;

//import static ru.hse.moms.service.WishlistService.toItemResponse;
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              ItemRepository itemRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public ReservationResponse reserveItem(Long itemId) {
        Long userId = AuthUtils.getCurrentId();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (item.getReservation() != null) {
            throw new RuntimeException("Item already reserved");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reservation reservation = new Reservation();
        reservation.setItem(item);
        reservation.setUser(user);
        reservation = reservationRepository.save(reservation);

        item.setReservation(reservation);
        itemRepository.save(item);

        return toResponse(reservation);
    }

    public List<ReservationResponse> getUserReservations() {
        User user = userRepository.findById(AuthUtils.getCurrentId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Reservation> reservations = reservationRepository.findAllByUser(user);

        return reservations.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private ReservationResponse toResponse(Reservation reservation) {
        ReservationResponse response = new ReservationResponse();
        response.setItems(List.of(WishlistService.toItemResponse(reservation.getItem())));
        return response;
    }
}
