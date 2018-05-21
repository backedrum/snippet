package com.backedrum.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.wicket.util.io.IClusterable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@MappedSuperclass
@NoArgsConstructor
public class BaseEntity implements IClusterable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String tag;

    @Column
    private LocalDateTime dateTime;

    public BaseEntity(String title, LocalDateTime dateTime, String tag) {
        this.title = title;
        this.dateTime = dateTime;
        this.tag = tag;
    }
}
