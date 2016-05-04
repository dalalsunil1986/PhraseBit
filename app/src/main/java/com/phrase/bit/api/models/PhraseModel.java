package com.phrase.bit.api.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Joel on 5/4/2016.
 */
@Root(name = "downloads")
public class PhraseModel {
    @ElementList(name="item")
    List<String> items;
}
