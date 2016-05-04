package com.phrase.bit.api.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Joel on 5/4/2016.
 */
@Root(name = "download")
public class PhraseRootModel {
    @Element(name = "item", required = false)
    private PhraseModel model;

    public PhraseModel getModel() {
        return model;
    }

    public void setModel(PhraseModel model) {
        this.model = model;
    }
}
