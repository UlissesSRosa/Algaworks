package com.example.algamoney.api.repository;

import com.example.algamoney.api.repository.launch.LaunchRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.Launch;

public interface LaunchRepository extends JpaRepository<Launch, Long>, LaunchRepositoryQuery {

}