package com.example.algamoney.api.repository.launch;

import com.example.algamoney.api.model.Launch;
import com.example.algamoney.api.repository.filter.LaunchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LaunchRepositoryQuery {

    public Page<Launch> filter(LaunchFilter launchFilter, Pageable pageable);
}
