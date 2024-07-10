package ru.hse.moms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.moms.request.ItemRequest;
import ru.hse.moms.response.WishlistResponse;
import ru.hse.moms.service.WishlistService;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/my")
    public ResponseEntity<WishlistResponse> createWishlist() {
        WishlistResponse wishlistResponse = wishlistService.createWishlist();
        return ResponseEntity.ok(wishlistResponse);
    }

    @GetMapping("/my")
    public ResponseEntity<WishlistResponse> getUserWishlist() {
        WishlistResponse wishlist = wishlistService.getUserWishlist();
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/my/item")
    public ResponseEntity<WishlistResponse> addItemToMyWishlist(@RequestBody ItemRequest itemRequest) {
        WishlistResponse wishlistResponse = wishlistService.addItemToWishlist(itemRequest);
        return ResponseEntity.ok(wishlistResponse);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<WishlistResponse> getWishlistByUserId(@PathVariable Long userId) {
        WishlistResponse wishlist = wishlistService.getWishlistByUserId(userId);
        return ResponseEntity.ok(wishlist);
    }

    @DeleteMapping("/my/item/{itemId}")
    public ResponseEntity<Void> removeItemFromWishlist(@PathVariable Long itemId) {
        wishlistService.removeItemFromWishlist(itemId);
        return ResponseEntity.noContent().build();
    }
}
