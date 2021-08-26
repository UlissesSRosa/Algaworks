package com.example.algamoney.api.resource;

import com.example.algamoney.api.event.ResourceCreatedEvent;
import com.example.algamoney.api.exceptionhandler.AlgamoneyExceptionHandler;
import com.example.algamoney.api.model.Launch;
import com.example.algamoney.api.repository.LaunchRepository;
import com.example.algamoney.api.repository.filter.LaunchFilter;
import com.example.algamoney.api.service.LaunchService;
import com.example.algamoney.api.service.exception.PersonInexistentOrInactiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/launch")
public class LaunchResource {

    @Autowired
    private LaunchRepository launchRepository;

    @Autowired
    private LaunchService launchService;

    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public Page<Launch> research(LaunchFilter launchFilter, Pageable pageable){
        return launchRepository.filter(launchFilter, pageable);
    }

//    @GetMapping
//    public List<Launch> list() {
//        return launchRepository.findAll();
//    }

    @GetMapping("/{code}")
    public ResponseEntity<Launch> buscarPeloCodigo(@PathVariable Long code) {
        Launch launch = launchRepository.findById(code).get();
        return launch != null ? ResponseEntity.ok(launch) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Launch> create(@Valid @RequestBody Launch launch, HttpServletResponse response){
        Launch launchSaved = launchService.save(launch);
        publisher.publishEvent(new ResourceCreatedEvent(this, response , launchSaved.getCode()));
        return ResponseEntity.status(HttpStatus.CREATED).body(launchSaved);
    }

    @ExceptionHandler({ PersonInexistentOrInactiveException.class })
    public ResponseEntity<Object> handlePersonInexistentOrInactiveException(PersonInexistentOrInactiveException ex){
        String userMessage = messageSource.getMessage("inactive.person", null, LocaleContextHolder.getLocale());
        String devMessage = ex.toString();
        List<AlgamoneyExceptionHandler.Erro> errors = Arrays.asList(new AlgamoneyExceptionHandler.Erro(userMessage, devMessage));
        return ResponseEntity.badRequest().body(errors);
    }
}