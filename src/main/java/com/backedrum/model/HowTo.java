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
@Table(name = "howtos")
@NoArgsConstructor
public class HowTo extends BaseEntity {

    @Column
    private String text;

    @Builder
    public HowTo(String title, LocalDateTime dateTime, String tag, String text) {
        super(title, dateTime, tag);
        this.text = text;
    }
}
