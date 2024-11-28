package com.dualsoft.banking.account.service.repository;

import com.dualsoft.banking.account.service.domain.AccountOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountOwnerRepository extends JpaRepository<AccountOwner, UUID> {

    Optional<AccountOwner> findByPersonalId(String personalId);
    Optional<AccountOwner> findByEmail(String email);
    Optional<AccountOwner> findByPhone(String phone);

    Optional<AccountOwner> findByPersonalIdOrEmailOrPhone(String personalId, String email, String phone);

    @Modifying
    @Query(value = "UPDATE account.account_owner SET ao_account_status = ?2 WHERE ao_id = ?1", nativeQuery = true)
    int setAccountStatus(UUID id, int status);
}
