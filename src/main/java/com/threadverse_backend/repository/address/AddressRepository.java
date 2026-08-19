package com.threadverse_backend.repository.address;

import com.threadverse_backend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserUserId(Long userId);

    List<Address> findByUserUserIdAndIsDefaultTrue(Long userId);
}