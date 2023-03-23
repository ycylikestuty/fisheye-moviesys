package com.gdesign.fisheyemoviesys.utils;

import com.gdesign.fisheyemoviesys.entity.SensitiveWordDO;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author ycy
 */
@Slf4j
public class SensitiveWordInit {
    /**
     * 敏感词库
     */
    public HashMap sensitiveWordMap;

    /**
     * 初始化敏感词
     *
     * @return
     */
    public Map initKeyWord(List<SensitiveWordDO> sensitiveWords) {
        try {
            // 从敏感词集合中取出敏感词放入Set中，List->set
            Set<String> keyWordSet = new HashSet<>();
            for (SensitiveWordDO s : sensitiveWords) {
                keyWordSet.add(s.getWord());
            }
            // 将敏感词库加入到HashMap中，set->HashMap
            addSensitiveWordToHashMap(keyWordSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }

    /**
     * 封装敏感词库
     *
     * @param keyWordSet
     */
    @SuppressWarnings("rawtypes")
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordMap = new HashMap(keyWordSet.size());
        // 敏感词
        String key = null;
        // 用来按照相应的格式保存敏感词库数据
        Map nowMap = null;
        // 辅助构建敏感词库
        Map<String, String> newWorMap = null;
        // 使用迭代器循环敏感词集合
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            // 等于敏感词库，HashMap对象在内存中占用的是同一个地址，所以当nowMap对象变化，sensitiveWordMap也会改变
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                // 获取敏感词当中的字，在敏感词库中字为HashMap对象的Key键值
                char keyChar = key.charAt(i);
                // 判断这个字是否存在于敏感词库中
                Object wordMap = nowMap.get(keyChar);
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    newWorMap = new HashMap<>();
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }
                // 如果该字是当前敏感词的最后一个字，则标识为结尾字
                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");
                }
//                log.info("封装敏感词库过程："+sensitiveWordMap);
            }
//            log.info("查看敏感词库数据:" + sensitiveWordMap);
        }
    }

}
