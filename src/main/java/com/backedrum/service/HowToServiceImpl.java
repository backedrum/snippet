package com.backedrum.service;

import com.backedrum.model.HowTo;
import com.backedrum.repository.HowToRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("howtoService")
public class HowToServiceImpl implements ItemsService<HowTo> {

    private final HowToRepository repository;

    @Autowired
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

    @Override
    public List<String> getAllTags() {
        return repository.findAllTags();
    }

    @Override
    public List<HowTo> retrieveByTag(String tag) {
        return repository.findByTag(tag).sorted(Comparator.comparing(HowTo::getTitle)).collect(Collectors.toList());
    }
}
