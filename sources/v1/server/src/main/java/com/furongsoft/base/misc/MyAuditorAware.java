package com.furongsoft.base.misc;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * @author Alex
 */
@Configuration
public class MyAuditorAware implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        // TODO: get user from shiro
        return Optional.of(0L);
    }
}
