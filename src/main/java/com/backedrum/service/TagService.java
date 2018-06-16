package com.backedrum.service;

import com.backedrum.model.HowTo;
import com.backedrum.model.Screenshot;
import com.backedrum.model.SourceCodeSnippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service("tagService")
public class TagService {

    private final ItemsService<HowTo> howtoService;

    private final ItemsService<SourceCodeSnippet> snippetsService;

    private final ItemsService<Screenshot> screenshotsService;

    @Autowired
    public TagService(@Qualifier("howtoService") ItemsService<HowTo> howtoService,
                      @Qualifier("snippetService") ItemsService<SourceCodeSnippet> snippetsService,
                      @Qualifier("screenshotService") ItemsService<Screenshot> screenshotsService) {
        this.howtoService = howtoService;
        this.snippetsService = snippetsService;
        this.screenshotsService = screenshotsService;
    }

    public List<String> getAllTags() {
        List<String> allTags = new ArrayList<>(howtoService.getAllTags());
        allTags.addAll(snippetsService.getAllTags());
        allTags.addAll(screenshotsService.getAllTags());

        return allTags.stream().distinct().sorted().collect(Collectors.toList());
    }
}
