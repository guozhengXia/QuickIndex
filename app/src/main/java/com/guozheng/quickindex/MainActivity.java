package com.guozheng.quickindex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.guozheng.quickindex.adapter.AddressBookAdapter;
import com.guozheng.quickindex.bean.ContactPerson;
import com.guozheng.quickindex.util.Utils;
import com.guozheng.quickindex.view.QuickIndexBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView mExpandableListView;
    ArrayList<ArrayList<ContactPerson>> contactPersonsList;
    private QuickIndexBar mQuickIndexBar;
    private TextView mTextDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setListView();
        setIndexBar();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mExpandableListView = (ExpandableListView) findViewById(R.id.address_expandable_listview);
        mTextDialog = (TextView) findViewById(R.id.text_dialog);
        mQuickIndexBar = (QuickIndexBar) findViewById(R.id.quick_index_bar);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        contactPersonsList = Utils.getSortDataList(this,"person_name.txt");
    }

    /**
     * 设置ListView
     */
    private void setListView() {
        /**设置适配器*/
        mExpandableListView.setAdapter(new AddressBookAdapter(this, contactPersonsList));

        /**设置group不可点击*/
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;//注意：一定要返回true，返回true这个方法才有效。
            }
        });

        /**展开所有条目*/
        for (int i = 0; i < contactPersonsList.size(); i++){
            mExpandableListView.expandGroup(i);
        }
    }

    /**
     * 设置setIndexBar
     */
    private void setIndexBar() {
        mQuickIndexBar.setTextView(mTextDialog);//设置textDialog
        mQuickIndexBar.setPaddingTop(15);//设置顶部padding
        mQuickIndexBar.setPaddingBottom(15);//设置底部padding

        /**设置监听器*/
        mQuickIndexBar.setOnLetterChangedListener(new QuickIndexBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                for( int i = 0; i< contactPersonsList.size();i++){
                    if(letter.equals(contactPersonsList.get(i).get(0).firstLetter)){
                        mExpandableListView.setSelectedGroup(i);
                    }
                }
            }
            @Override
            public void onLetterGone() {}
        });
    }




}
