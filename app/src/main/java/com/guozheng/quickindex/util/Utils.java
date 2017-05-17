package com.guozheng.quickindex.util;

import android.content.Context;

import com.guozheng.quickindex.bean.ContactPerson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by fighting on 2017/5/17.
 */

public class Utils {


    /**
     * 根据文件名得到集合，将ContactPerson类集合进行排序，并按照字符拆分成二维集合
     */
    public static ArrayList<ArrayList<ContactPerson>> getSortDataList(Context context, String fileName) {
        ArrayList<ContactPerson> dataList = getDataList(context, fileName);
        Collections.sort(dataList);
        ArrayList<ArrayList<ContactPerson>> contactPersonsList = new ArrayList<>();
        ArrayList<ContactPerson> firstLetterList = new ArrayList<>();
        String firstLetter = "#";
        for (int i = 0;i < dataList.size(); i++){
            ContactPerson person = dataList.get(i);
            if(!person.firstLetter.equals(firstLetter)){
                firstLetterList = new ArrayList<>();
                contactPersonsList.add(firstLetterList);
            }
            firstLetterList.add(person);
            firstLetter = person.firstLetter;
        }
        return contactPersonsList;
    }


    /**
     * 将人名转换为ContactPerson类，并放入集合中
     */
    public static ArrayList<ContactPerson> getDataList(Context context, String fileName) {
        String[] datas = Utils.getDataFromAssets(context, fileName);
        ArrayList<ContactPerson> dataList = new ArrayList<>();
        for (int i = 0;i < datas.length; i++){
            dataList.add(new ContactPerson(datas[i]));
        }
        return dataList;
    }

    /**
     * 读取文件，得到人名数组
     */
    public static String[] getDataFromAssets(Context context, String fileName) {
        try {
            InputStream is = context.getResources().getAssets().open(fileName);
            BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String strTemp = "";
            while ((strTemp = tBufferedReader.readLine()) != null) {
                sb.append(strTemp);
            }
            return sb.toString().split(",");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
