package com.chen.drawerexample.utils;

/**
 * Created by chen
 * Date : 16-1-3
 * Name : DrawerExample
 */
public class WordModel {

    private String word;

    private String translation;

    private String phonetic;

    private String tags;

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTags() {
        return tags;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

}
