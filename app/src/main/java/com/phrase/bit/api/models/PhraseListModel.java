package com.phrase.bit.api.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Joel on 5/4/2016.
 */
@Root(name = "downloads")
public class PhraseListModel {
    @ElementList(entry = "item", inline = true)
    private List<String> items;

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
