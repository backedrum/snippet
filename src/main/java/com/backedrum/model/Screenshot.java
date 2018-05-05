package com.backedrum.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "screenshots")
@NoArgsConstructor
public class Screenshot extends BaseEntity {

    @Embedded
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Image> images;

    @Builder
    public Screenshot(String title, LocalDateTime dateTime, List<Image> images) {
        super(title, dateTime);
        this.images = images;
    }
}
