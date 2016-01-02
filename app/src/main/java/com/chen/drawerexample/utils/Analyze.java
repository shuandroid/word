package com.chen.drawerexample.utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen
 * Date : 16-1-3
 * Name : DrawerExample
 */
public class Analyze implements WordParser {

    @Override
    public List<WordModel> parse(InputStream inputStream) throws Exception {

        List<WordModel> models = null;
        WordModel model = null;

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    models = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("item")) {
                        model = new WordModel();
                    } else if (parser.getName().equals("word")) {

                    }
            }
        }

        return null;
    }

    @Override
    public String serialize(List<WordModel> wordModels) throws Exception {
        return null;
    }
}
