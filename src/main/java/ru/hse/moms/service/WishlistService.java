package ru.hse.moms.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.moms.entity.Item;
import ru.hse.moms.entity.User;
import ru.hse.moms.entity.Wishlist;
import ru.hse.moms.exception.UserNotFoundException;
import ru.hse.moms.exception.WishlistAlreadyExistException;
import ru.hse.moms.exception.WishlistNotFoundException;
import ru.hse.moms.repository.UserRepository;
import ru.hse.moms.repository.WishlistRepository;
import ru.hse.moms.request.ItemRequest;
import ru.hse.moms.response.ItemResponse;
import ru.hse.moms.response.WishlistResponse;
import ru.hse.moms.security.AuthUtils;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;

    @Autowired
    public WishlistService(WishlistRepository wishlistRepository, UserRepository userRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public WishlistResponse createWishlist() {
        User user = userRepository.findById(AuthUtils.getCurrentId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (wishlistRepository.findByUser(user).isPresent()) {
            throw new WishlistAlreadyExistException("User already has a wishlist");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);

        return toResponse(wishlistRepository.save(wishlist));
    }

    public WishlistResponse getUserWishlist() {
        Wishlist wishlist = wishlistRepository.findByUser(
                        userRepository.findById(
                                        Objects.requireNonNull(AuthUtils.getCurrentId()))
                                .orElseThrow(() -> new UserNotFoundException("User not found")))
                .orElseThrow(() -> new WishlistNotFoundException("Wishlist not found"));

        return toResponse(wishlist);
    }

    @Transactional
    public WishlistResponse addItemToWishlist(ItemRequest itemRequest) {
        User user = userRepository.findById(AuthUtils.getCurrentId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        Item item = new Item();
        item.setName(itemRequest.getName());
        item.setAllowAlternatives(itemRequest.isAllowAlternatives());
        item.setLink(itemRequest.getLink());
        item.setWishlist(wishlist);

        wishlist.getItems().add(item);
        wishlistRepository.save(wishlist);

        return toResponse(wishlist);
    }

    public WishlistResponse getWishlistByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        return toResponse(wishlist);
    }

    @Transactional
    public void removeItemFromWishlist(Long itemId) {
        Wishlist wishlist = wishlistRepository.findByUser(
                        userRepository.findById(AuthUtils.getCurrentId())
                                .orElseThrow(() -> new UserNotFoundException("User not found")))
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));
        Item itemToRemove = wishlist.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        wishlist.getItems().remove(itemToRemove);
        wishlistRepository.save(wishlist);
    }

    private WishlistResponse toResponse(Wishlist wishlist) {
        WishlistResponse response = new WishlistResponse();
        response.setId(wishlist.getId());
        response.setItems(wishlist.getItems().stream().map(WishlistService::toItemResponse).collect(Collectors.toList()));
        return response;
    }

    static ItemResponse toItemResponse(Item item) {
        ItemResponse response = new ItemResponse();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setAllowAlternatives(item.isAllowAlternatives());
        response.setReserved(item.getReservation() != null);
        response.setLink(item.getLink());
        return response;
    }
}
