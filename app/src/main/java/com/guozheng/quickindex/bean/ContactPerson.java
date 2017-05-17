package com.guozheng.quickindex.bean;

import android.text.TextUtils;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * Created by fighting on 2017/5/17.
 */

public class ContactPerson implements Comparable<ContactPerson> {

    public String mName;
    public String mPinyin;
    public String firstLetter;

    public String getPinyin() {
        return mPinyin;
    }

    public ContactPerson(String name) {
        this.mName = name;

        if (!TextUtils.isEmpty(name)) {
            String firstChar = name.charAt(0)+"";
            if(firstChar.matches("^[0-9]+") || firstChar.matches("#")){
                /**以数字和#开头的name都归于#*/
                mPinyin = "zzzzzzzzzzzzzzzzzzzz";
                firstLetter = "#";
            }else {
                try {
                    this.mPinyin = PinyinHelper.convertToPinyinString(name.trim(), "", PinyinFormat.WITHOUT_TONE);
                } catch (PinyinException e) {
                    e.printStackTrace();
                }
                if(!TextUtils.isEmpty(mPinyin)){
                    char firstLetterChar = this.mPinyin.toUpperCase().charAt(0);
                    firstLetter = firstLetterChar + "";
                    if (firstLetterChar < 'A' || firstLetterChar > 'Z') {
                        mPinyin = "zzzzzzzzzzzzzzzzzzzz";
                        firstLetter = "#";
                    }
                }
            }

        } else {
            mPinyin = "zzzzzzzzzzzzzzzzzzzz";
            firstLetter = "#";
        }
    }

    @Override
    public int compareTo(ContactPerson o) {
        return this.mPinyin.compareTo(o.getPinyin());
    }
}
