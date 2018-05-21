package com.backedrum.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "snippets")
@NoArgsConstructor
public class SourceCodeSnippet extends BaseEntity {
    @Column
    private String sourceCode;

    @Builder
    public SourceCodeSnippet(String title, LocalDateTime dateTime, String tag, String sourceCode) {
        super(title, dateTime, tag);
        this.sourceCode = sourceCode;
    }
}
