package com.threadverse_backend.controller.address;

import com.threadverse_backend.dto.request.AddAddressRequest;
import com.threadverse_backend.dto.request.UpdateAddressRequest;
import com.threadverse_backend.dto.response.AddressResponse;
import com.threadverse_backend.service.address.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> addAddress(
            @RequestParam Long userId,
            @Valid @RequestBody AddAddressRequest request) {

        return ResponseEntity.ok(
                addressService.addAddress(userId, request)
        );
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAddresses(
            @RequestParam Long userId) {

        return ResponseEntity.ok(
                addressService.getAddresses(userId)
        );
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @RequestParam Long userId,
            @PathVariable Long addressId,
            @Valid @RequestBody UpdateAddressRequest request) {

        return ResponseEntity.ok(
                addressService.updateAddress(
                        userId,
                        addressId,
                        request
                )
        );
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @RequestParam Long userId,
            @PathVariable Long addressId) {

        addressService.deleteAddress(userId, addressId);

        return ResponseEntity.noContent().build();
    }
}