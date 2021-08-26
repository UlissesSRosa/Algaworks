package com.example.algamoney.api.service;

import com.example.algamoney.api.model.Person;
import com.example.algamoney.api.repository.PersonRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Person update(Long code, Person person){
        Person personSaved = findPersonByCode(code);
        BeanUtils.copyProperties(person, personSaved, "code");
        return personRepository.save(personSaved);

    }

    public void updatePropertieActive (Long code, Boolean active){
        Person personSaved = findPersonByCode(code);
        personSaved.setActive(active);
        personRepository.save(personSaved);
    }

    private Person findPersonByCode(Long code) {
        Person personSaved = personRepository.findById(code).get();
        if (personSaved == null){
            throw new NoSuchElementException();
        }
        return personSaved;
    }
}
