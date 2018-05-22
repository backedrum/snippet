package com.backedrum.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.wicket.util.io.IClusterable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TagGroup implements IClusterable {

    private String tag;

    private List<Image> images = new ArrayList<>();

    private List<SourceCodeSnippet> snippets = new ArrayList<>();

    private List<Screenshot> screenshots = new ArrayList<>();
}
