package com.threadverse_backend.service.address;

import com.threadverse_backend.dto.request.AddAddressRequest;
import com.threadverse_backend.dto.request.UpdateAddressRequest;
import com.threadverse_backend.dto.response.AddressResponse;
import com.threadverse_backend.entity.Address;
import com.threadverse_backend.entity.User;
import com.threadverse_backend.exception.BadRequestException;
import com.threadverse_backend.exception.ResourceNotFoundException;
import com.threadverse_backend.mapper.address.AddressMapper;
import com.threadverse_backend.repository.address.AddressRepository;
import com.threadverse_backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public AddressResponse addAddress(
            Long userId,
            AddAddressRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        if (Boolean.TRUE.equals(request.getIsDefault())) {

            List<Address> existingAddresses =
                    addressRepository.findByUserUserId(userId);

            existingAddresses.forEach(address ->
                    address.setIsDefault(false)
            );

            addressRepository.saveAll(existingAddresses);
        }

        Address address = Address.builder()
                .user(user)
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .isDefault(
                        Boolean.TRUE.equals(
                                request.getIsDefault()
                        )
                )
                .build();

        Address savedAddress =
                addressRepository.save(address);

        return AddressMapper.toResponse(savedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAddresses(
            Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found"
            );
        }

        return addressRepository
                .findByUserUserId(userId)
                .stream()
                .map(AddressMapper::toResponse)
                .toList();
    }

    @Override
    public AddressResponse updateAddress(
            Long userId,
            Long addressId,
            UpdateAddressRequest request) {

        Address address = addressRepository
                .findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found"
                        )
                );

        if (!address.getUser()
                .getUserId()
                .equals(userId)) {

            throw new BadRequestException(
                    "Address does not belong to this user"
            );
        }

        if (request.getAddressLine1() != null) {
            address.setAddressLine1(
                    request.getAddressLine1()
            );
        }

        if (request.getAddressLine2() != null) {
            address.setAddressLine2(
                    request.getAddressLine2()
            );
        }

        if (request.getCity() != null) {
            address.setCity(request.getCity());
        }

        if (request.getState() != null) {
            address.setState(request.getState());
        }

        if (request.getPostalCode() != null) {
            address.setPostalCode(
                    request.getPostalCode()
            );
        }

        if (request.getCountry() != null) {
            address.setCountry(
                    request.getCountry()
            );
        }

        if (request.getIsDefault() != null) {

            if (request.getIsDefault()) {

                List<Address> existingAddresses =
                        addressRepository
                                .findByUserUserId(userId);

                existingAddresses.forEach(existing ->
                        existing.setIsDefault(false)
                );

                addressRepository.saveAll(
                        existingAddresses
                );

                address.setIsDefault(true);

            } else {
                address.setIsDefault(false);
            }
        }

        Address updatedAddress =
                addressRepository.save(address);

        return AddressMapper.toResponse(updatedAddress);
    }

    @Override
    public void deleteAddress(
            Long userId,
            Long addressId) {

        Address address = addressRepository
                .findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found"
                        )
                );

        if (!address.getUser()
                .getUserId()
                .equals(userId)) {

            throw new BadRequestException(
                    "Address does not belong to this user"
            );
        }

        addressRepository.delete(address);
    }
}