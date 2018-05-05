package com.backedrum.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.wicket.util.io.IClusterable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class Image implements IClusterable {

    @Column
    private byte[] image;

    @Builder
    public Image(byte[] image) {
        this.image = image;
    }
}
