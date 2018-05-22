package com.backedrum.service;

import com.backedrum.model.Screenshot;
import com.backedrum.repository.ScreenshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("screenshotService")
public class ScreenshotServiceImpl implements ItemsService<Screenshot> {

    private final ScreenshotRepository repository;

    @Autowired
    public ScreenshotServiceImpl(ScreenshotRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveItem(Screenshot entity) {
        repository.save(entity);
    }

    @Override
    public void removeItem(Long id) {
        repository.delete(id);
    }

    @Override
    public List<Screenshot> retrieveAllItems() {
        return repository.findAll();
    }

    @Override
    public List<String> getAllTags() {
        return repository.findAllTags();
    }
}
