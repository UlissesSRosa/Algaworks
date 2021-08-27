package com.example.algamoney.api.resource;

import com.example.algamoney.api.event.ResourceCreatedEvent;
import com.example.algamoney.api.model.Category;
import com.example.algamoney.api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


@RestController
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequestMapping("/categories")
public class CategoryResource {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    public ResponseEntity<?> listCategories(){
        List<Category> categories = categoryRepository.findAll();
        return !categories.isEmpty() ? ResponseEntity.ok(categories) : ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> create(@Valid @RequestBody Category category, HttpServletResponse response){
        Category categorySaved = categoryRepository.save(category);
        publisher.publishEvent(new ResourceCreatedEvent(this, response , categorySaved.getCode()));
        return ResponseEntity.status(HttpStatus.CREATED).body(categorySaved);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Category> findByCode(@PathVariable Long code) {
        return this.categoryRepository.findById(code)
                .map(categoria -> ResponseEntity.ok(categoria))
                .orElse(ResponseEntity.notFound().build());
    }


}
