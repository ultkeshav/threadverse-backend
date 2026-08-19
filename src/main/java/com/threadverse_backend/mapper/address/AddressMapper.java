package com.threadverse_backend.mapper.address;

import com.threadverse_backend.dto.response.AddressResponse;
import com.threadverse_backend.entity.Address;

public class AddressMapper {

    private AddressMapper() {
    }

    public static AddressResponse toResponse(Address address) {

        if (address == null) {
            return null;
        }

        return AddressResponse.builder()
                .addressId(address.getAddressId())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .isDefault(address.getIsDefault())
                .build();
    }
}