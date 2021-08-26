package com.example.algamoney.api.service;
import com.example.algamoney.api.model.Launch;
import com.example.algamoney.api.model.Person;
import com.example.algamoney.api.repository.LaunchRepository;
import com.example.algamoney.api.repository.PersonRepository;
import com.example.algamoney.api.service.exception.PersonInexistentOrInactiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LaunchService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private LaunchRepository launchRepository;

    public Launch save(Launch launch){
        Optional<Person> person = personRepository.findById(launch.getPerson().getCode());
        if(person == null || person.get().isInactive()){
            throw new PersonInexistentOrInactiveException();
        }
        return launchRepository.save(launch);
    }


}
