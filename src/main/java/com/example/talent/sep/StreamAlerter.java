package com.example.talent.sep;

import java.util.*;

public class StreamAlerter {
//    private RingBuffer ring;
//    private Trie trie;

    //敏感词索引
    public static Map<String, Integer> wordIndex = new HashMap<String, Integer>();

    public StreamAlerter(String[] strings) {

        if (strings == null) {
            return;
        }
        Set<String> keyWordSet = new HashSet<>();
        //数组转map 便于遍历
        for (int i = 0; i < strings.length; i++) {
            wordIndex.put(strings[i], 0);
            keyWordSet.add(strings[i]);
        }
        System.out.println(wordIndex);
        addSensitiveWordToHashMap(keyWordSet);
    }

    TreeMap<String, TreeMap<String, String>> sensitiveWordMap = null;

    //同一个字符调用次数计数，定有有序map
    TreeMap<String, Integer> countMap = new TreeMap<>();

    public boolean query(char ch) {
        boolean flag = true;

        String str = String.valueOf(ch);

        TreeMap wordMap = (TreeMap) sensitiveWordMap.get(str);

        if (wordMap == null) {
            return false;
        }

        Integer count = countMap.get(str);
        if (count == null) {
            count = 0;
        }
        countMap.put(str, count + 1);

        Iterator<Map.Entry<String, Object>> it = wordMap.entrySet().iterator();

        TreeMap<String,String> tempMap = new TreeMap<>();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String key = String.valueOf(entry.getKey().charAt(0));
            String value = String.valueOf(entry.getValue());
            if (key.equals(str) && ("0").equals(value)) {
                flag = false;
            }
            tempMap.put(entry.getKey(), value);
        }
        Integer times = countMap.get(str);

        //处理同一个字符出现两次以上的数据
        if (times >= 2 ) {
           int tnum = 1;

            for (Map.Entry<String, String> ts : tempMap.entrySet()){
                //当第几次调用就取tempMap中的第几个，
                if(tnum == times){
                    if(ts.getValue().equals("0")){
                        flag = false;
                    }else{
                        flag = true;
                    }
                    break;
                }
                tnum ++;
            }
        }
        return flag;
    }

    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordMap = new TreeMap();
        String key = null;
        TreeMap<String, String> newWorMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();    //关键字
            newWorMap = new TreeMap<String, String>();

            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);       //转换成char型
                //词组中最后一个
                if (i == key.length() - 1) {
                    newWorMap.put(String.valueOf(keyChar) + i, "1");//最后一个字符
                } else {
                    newWorMap.put(String.valueOf(keyChar) + i, "0");//不是
                }
                sensitiveWordMap.put(String.valueOf(key.charAt(i)), (TreeMap<String, String>) newWorMap);
            }
            System.out.println(sensitiveWordMap);
        }
    }

    public static void main(String[] args) {
        StreamAlerter as = new StreamAlerter(new String[]{"赌博", "游戏", "摇头丸", "XXX"});

        boolean query = as.query('X');
        boolean query1 = as.query('X');
        boolean query2 = as.query('X');
        System.out.println(query);
        System.out.println(query1);
        System.out.println(query2);

    }
}
