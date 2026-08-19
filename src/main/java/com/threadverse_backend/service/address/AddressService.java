package com.threadverse_backend.service.address;

import com.threadverse_backend.dto.request.AddAddressRequest;
import com.threadverse_backend.dto.request.UpdateAddressRequest;
import com.threadverse_backend.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse addAddress(
            Long userId,
            AddAddressRequest request
    );

    List<AddressResponse> getAddresses(
            Long userId
    );

    AddressResponse updateAddress(
            Long userId,
            Long addressId,
            UpdateAddressRequest request
    );

    void deleteAddress(
            Long userId,
            Long addressId
    );
}