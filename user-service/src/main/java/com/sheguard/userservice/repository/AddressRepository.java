package com.sheguard.userservice.repository;

import com.sheguard.userservice.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
