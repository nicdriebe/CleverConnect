package com.example.application.services;

import com.example.application.data.SamplePerson;
import com.example.application.data.SamplePersonExternal;
import com.example.application.data.SamplePersonExternalRepository;
import com.example.application.data.SamplePersonRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class SamplePersonExternalService {

    private final SamplePersonExternalRepository repository;

    // Autowire all the repositories we need
    public SamplePersonExternalService(SamplePersonExternalRepository repository) {
        this.repository = repository;
    }

    public Optional<SamplePersonExternal> get(Long id) {
        return repository.findById(id);
    }

    public SamplePersonExternal update(SamplePersonExternal entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<SamplePersonExternal> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<SamplePersonExternal> list(Pageable pageable, Specification<SamplePersonExternal> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<SamplePersonExternal> findAllContacts(String filterText) {
        if(filterText == null || filterText.isEmpty()) {
            // if we don't have a filter we return everything
            return repository.findAll();
        } else {
            // if we have a filter text we use a query to filter them
            return repository.search(filterText);
        }
    }

    public void saveExternal(SamplePersonExternal samplePersonExternal) {
        // make sure we have an external, if Binder messes up
        if(samplePersonExternal == null) {
            System.err.println("External is null.");
            return;
        }

        // we made sure we have an external --> save external
        repository.save(samplePersonExternal);
    }

    public void delete(String s) {
    }
}
