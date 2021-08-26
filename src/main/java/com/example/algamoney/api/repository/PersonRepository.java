package com.example.algamoney.api.repository;

import com.example.algamoney.api.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
