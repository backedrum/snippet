package com.backedrum.service;

import com.backedrum.model.HowTo;
import com.backedrum.repository.HowToRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service("howtoService")
public class HowToServiceImpl implements ItemsService<HowTo> {

    private final HowToRepository repository;

    @Inject
    public HowToServiceImpl(HowToRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveItem(HowTo entity) {
        repository.save(entity);
    }

    @Override
    public void removeItem(Long id) {
        repository.delete(id);
    }

    @Override
    public List<HowTo> retrieveAllItems() {
        return repository.findAll();
    }
}
