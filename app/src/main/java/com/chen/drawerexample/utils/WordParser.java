package com.chen.drawerexample.utils;

import java.io.InputStream;
import java.util.List;

/**
 * Created by chen
 * Date : 16-1-3
 * Name : DrawerExample
 */
public interface WordParser {

    public List<WordModel> parse(InputStream inputStream) throws Exception;

    public String serialize(List<WordModel> wordModels) throws Exception;

}
