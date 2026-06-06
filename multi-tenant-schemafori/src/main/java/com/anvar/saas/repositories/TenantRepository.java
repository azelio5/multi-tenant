package com.anvar.saas.repositories;

import com.anvar.saas.entities.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, String> {
    boolean existsByEmail(String email);
    boolean existsByCompanyCode(String companyCode);
}
