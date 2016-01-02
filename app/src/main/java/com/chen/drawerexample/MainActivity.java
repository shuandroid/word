package com.chen.drawerexample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.chen.drawerexample.adapter.DictionaryAdapter;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnClickListener, TextWatcher {


    protected Toolbar toolbar;

    private final String DATABASE_PATH = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/dictionary";

    private AutoCompleteTextView word;

    private final String DATABASE_FILENAME = "dictionary.db";
    private SQLiteDatabase database;

    private Button searchWord;

    private TextView showResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView();

        setToolbar();
        setupDrawerLayout();
        initData();

        setupView();


    }

    private void setupDrawerLayout() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupView() {
        searchWord = (Button) findViewById(R.id.searchWord);
        word = (AutoCompleteTextView) findViewById(R.id.word);
        //绑定监听器
        searchWord.setOnClickListener(this);
        //绑定文字改变监听器
        word.addTextChangedListener(this);
        showResult = (TextView) findViewById(R.id.result);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void initData() {
        //打开数据库
        database = openDatabase();
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupContentView() {
        setContentView(R.layout.activity_main);
    }



    public void afterTextChanged(Editable s) {
        //  必须将english字段的别名设为_id
        Cursor cursor = database.rawQuery(
                "select english as _id from t_words where english like ?",
                new String[]
                        {s.toString() + "%"});
        //新建新的Adapter
        DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
                cursor, true);
        //绑定适配器
        word.setAdapter(dictionaryAdapter);

    }


    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }

    public void onClick(View view) {
        //查询指定的单词
        String sql = "select chinese from t_words where english=?";
        Cursor cursor = database.rawQuery(sql, new String[]
                {word.getText().toString()});
        String result = "未找到该单词.";
        //  如果查找单词，显示其中文的意思
        if (cursor.getCount() > 0) {
            //  必须使用moveToFirst方法将记录指针移动到第1条记录的位置
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("chinese")).replace("&amp;", "&");
        }
        //将结果显示到TextView中
        showResult.setText(word.getText() + "\n" + result.toString());
    }

    private SQLiteDatabase openDatabase() {

        try {
            // 获得dictionary.db文件的绝对路径
            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
            File dir = new File(DATABASE_PATH);
            // 如果/sdcard/dictionary目录中存在，创建这个目录
            if (!dir.exists()) {
                dir.mkdir();
            }


            // 如果在/sdcard/dictionary目录中不存在
            // dictionary.db文件，则从res\raw目录中复制这个文件到
            // SD卡的目录（/sdcard/dictionary）

            if (!(new File(databaseFilename)).exists()) {
                // 获得封装dictionary.db文件的InputStream对象
                InputStream is = getResources().openRawResource(
                        R.raw.dictionary);
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[8192];
                int count = 0;
                // 开始复制dictionary.db文件
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                //关闭文件流
                fos.close();
                is.close();
            }
            // 打开/sdcard/dictionary目录中的dictionary.db文件
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
                    databaseFilename, null);
            return database;
        } catch (Exception e) {
        }

        return null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
