package com.backedrum.service;

import com.backedrum.model.Screenshot;
import com.backedrum.repository.ScreenshotRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service("screenshotService")
public class ScreenshotServiceImpl implements ItemsService<Screenshot> {

    private final ScreenshotRepository repository;

    @Inject
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
}
