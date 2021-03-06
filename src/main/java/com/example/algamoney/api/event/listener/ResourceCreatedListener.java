package com.example.algamoney.api.event.listener;

import com.example.algamoney.api.event.ResourceCreatedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

public class ResourceCreatedListener implements ApplicationListener<ResourceCreatedEvent> {

    @Override
    public void onApplicationEvent(ResourceCreatedEvent resourceCreatedEvent){
        HttpServletResponse response = resourceCreatedEvent.getResponse();
        Long code = resourceCreatedEvent.getCode();

        addHeaderLocation(response, code);

    }

    private void addHeaderLocation(HttpServletResponse response, Long code) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{code}")
                .buildAndExpand(code).toUri();
        response.setHeader("Location", uri.toASCIIString());
    }
}
