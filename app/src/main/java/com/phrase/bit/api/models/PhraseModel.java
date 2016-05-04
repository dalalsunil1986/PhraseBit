package com.phrase.bit.api.models;

import org.simpleframework.xml.Element;

/**
 * Created by Joel on 5/4/2016.
 */
public class PhraseModel {
    @Element(name = "value", required = false)
    private String value;
    @Element(name = "key", required = false)
    private String key;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
