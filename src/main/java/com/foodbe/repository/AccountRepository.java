package com.foodbe.repository;

import com.foodbe.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    @Query(value = "select * from account where id =?1", nativeQuery = true)
    AccountEntity getAccountByID(Long id);
}
