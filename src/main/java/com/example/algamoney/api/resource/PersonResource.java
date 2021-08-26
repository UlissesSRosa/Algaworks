package com.example.algamoney.api.resource;

import com.example.algamoney.api.event.ResourceCreatedEvent;
import com.example.algamoney.api.model.Person;
import com.example.algamoney.api.repository.PersonRepository;
import com.example.algamoney.api.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonResource {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    ApplicationEventPublisher publisher;

    @GetMapping
    public ResponseEntity<?> listPersons(){
        List<Person> persons = personRepository.findAll();
        return !persons.isEmpty() ? ResponseEntity.ok(persons) : ResponseEntity.noContent().build(); // ternário (condição ? true : false)
    }

    @PostMapping
    public ResponseEntity<Person> create(@Valid @RequestBody Person person, HttpServletResponse response){
        Person personSaved = personRepository.save(person);
        publisher.publishEvent(new ResourceCreatedEvent(this, response , personSaved.getCode()));
        return ResponseEntity.status(HttpStatus.CREATED).body(personSaved);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Person> findByCode(@PathVariable Long code) {
        return this.personRepository.findById(code)
                .map(person -> ResponseEntity.ok(person))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long code){
        personRepository.deleteById(code);
    }

    @PutMapping("/{code}")
    public ResponseEntity<Person> update(@PathVariable Long code, @Valid @RequestBody Person person){
        Person personSaved = personService.update(code, person);
        return ResponseEntity.ok(personSaved);
    }

    @PutMapping("/{code}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePropertieActive(@PathVariable Long code, @RequestBody Boolean active){
        personService.updatePropertieActive(code, active);
    }
}
