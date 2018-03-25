package com.backedrum.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class SourceCodeSnippet extends Entity {

    private String sourceCode;

    @Builder
    public SourceCodeSnippet(String title, LocalDateTime dateTime, String sourceCode) {
        super(title, dateTime);
        this.sourceCode = sourceCode;
    }
}
