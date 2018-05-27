package com.backedrum.service;

import com.backedrum.model.SourceCodeSnippet;
import com.backedrum.repository.SnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("snippetService")
public class SnippetServiceImpl implements ItemsService<SourceCodeSnippet> {

    private final SnippetRepository repository;

    @Autowired
    public SnippetServiceImpl(SnippetRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveItem(SourceCodeSnippet snippet) {
        repository.save(snippet);
    }

    @Override
    public void removeItem(Long id) {
        repository.delete(id);
    }

    @Override
    public List<SourceCodeSnippet> retrieveAllItems() {
        return repository.findAll();
    }

    @Override
    public List<String> getAllTags() {
        return repository.findAllTags();
    }

    @Override
    public List<SourceCodeSnippet> retrieveByTag(String tag) {
        return repository.findByTag(tag).sorted(Comparator.comparing(SourceCodeSnippet::getTitle)).collect(Collectors.toList());
    }
}
