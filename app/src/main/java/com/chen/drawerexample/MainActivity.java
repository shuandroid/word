package com.chen.drawerexample;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.InputStream;
import java.util.ArrayList;

import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;

import com.chen.drawerexample.adapter.WordAdapter;
import com.chen.drawerexample.utils.Analyze;
import com.chen.drawerexample.utils.WordModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnClickListener {


    private PopupWindow popupWindow;
    private View popView;
    LayoutInflater inflater = null;

    private ArrayList<String> words = new ArrayList<>();

    private ArrayList<String> tags = new ArrayList<>();

    private ArrayList<String> translations = new ArrayList<>();

    private ArrayList<String> phonetic = new ArrayList<>();

    private ArrayList<WordModel> wordModels;

    protected TextView mResult;


    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;

    @Bind(R.id.search_view)
    protected SearchView mSearchView;

    @Bind(R.id.word_list)
    protected ListView mWordList;

//    @Bind(R.id.result)
//    protected TextView mResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView();

        setToolbar();
        setupDrawerLayout();

        setupView();

        initPopupWindow();
        initData();


    }

    private void setupDrawerLayout() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupView() {

        ButterKnife.bind(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void initData() {


        try {
            InputStream inputStream = getAssets().open("cet4.xml");
            Analyze analyze = new Analyze();
            wordModels = (ArrayList<WordModel>) analyze.parse(inputStream);
            for (WordModel model : wordModels) {
                words.add(model.getWord());
                tags.add(model.getTags());
                translations.add(model.getTranslation());
                phonetic.add(model.getPhonetic());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        WordAdapter adapter = new WordAdapter(this, words, tags);

        mWordList.setAdapter(adapter);
        mWordList.setTextFilterEnabled(true);

        mWordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPop(view);
                mResult.setText("Translation:" + translations.get(position) + "\n\n"
                        + "Phonetic:" + phonetic.get(position) + "\n\n");
            }
        });


        mSearchView.setIconifiedByDefault(false);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!TextUtils.isEmpty(query)) {
                    words.clear();
                    tags.clear();
                    phonetic.clear();
                    translations.clear();
                    for (WordModel mModel : wordModels) {
                        if (mModel.getWord().equals(query)) {
                            words.add(mModel.getWord());
                            tags.add(mModel.getTags());
                            translations.add(mModel.getTranslation());
                            phonetic.add(mModel.getPhonetic());
                        }
                    }
                    mWordList.setAdapter(new WordAdapter(MainActivity.this, words, tags));
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                if (TextUtils.isEmpty(newText)) {
                    words.clear();
                    tags.clear();
                    phonetic.clear();
                    translations.clear();
                    for (WordModel mModel : wordModels) {
                        words.add(mModel.getWord());
                        tags.add(mModel.getTags());
                        translations.add(mModel.getTranslation());
                        phonetic.add(mModel.getPhonetic());
                    }
                    mWordList.setAdapter(new WordAdapter(MainActivity.this, words, tags));
                }
                if (!TextUtils.isEmpty(newText)) {
                    words.clear();
                    tags.clear();
                    phonetic.clear();
                    translations.clear();
                    for (WordModel mModel : wordModels) {
                        if (mModel.getWord().compareTo(newText) >= 0) {
                            words.add(mModel.getWord());
                            tags.add(mModel.getTags());
                            translations.add(mModel.getTranslation());
                            phonetic.add(mModel.getPhonetic());
                        }
                    }
                    mWordList.setAdapter(new WordAdapter(MainActivity.this, words, tags));
                }
                return true;
            }
        });

    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void setupContentView() {
        setContentView(R.layout.activity_main);
    }


    public void onClick(View view) {

    }

    public void initPopupWindow() {

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        popView = inflater.inflate(R.layout.result_pop, null);
        popupWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setAnimationStyle(R.style.PopupWindow);

        mResult = (TextView) popView.findViewById(R.id.result);

    }


    private void showPop(View view) {

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();

    }

    private void hidePopupWindow() {
        popupWindow.dismiss();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (popupWindow != null && popupWindow.isShowing()) {
            hidePopupWindow();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
