package com.anvar.saas.services;

import com.anvar.saas.entities.Tenant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

public interface ProvisioningService {
    void provisionTenant(final Tenant tenant);
}
