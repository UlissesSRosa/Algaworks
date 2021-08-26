package com.example.algamoney.api.repository.filter;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class LaunchFilter {
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDateTo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDateUntil;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDateTo() {
        return dueDateTo;
    }

    public void setDueDateTo(LocalDate dueDateTo) {
        this.dueDateTo = dueDateTo;
    }

    public LocalDate getDueDateUntil() {
        return dueDateUntil;
    }

    public void setDueDateUntil(LocalDate dueDateUntil) {
        this.dueDateUntil = dueDateUntil;
    }
}
