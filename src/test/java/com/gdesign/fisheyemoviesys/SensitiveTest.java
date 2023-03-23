package com.gdesign.fisheyemoviesys;

import com.gdesign.fisheyemoviesys.entity.SensitiveWordDO;
import com.gdesign.fisheyemoviesys.service.SensitiveWordService;
import com.gdesign.fisheyemoviesys.utils.SensitiveWordInit;
import com.gdesign.fisheyemoviesys.utils.SensitiveWordUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
@Slf4j
public class SensitiveTest {

    @Resource
    private SensitiveWordService sensitiveService;

    @Test
    public void sensitiveWordFiltering() {
        // 初始化敏感词库对象
        SensitiveWordInit sensitiveWordInit = new SensitiveWordInit();
        // 从数据库中获取敏感词对象集合
        List<SensitiveWordDO> sensitiveWords = sensitiveService.list();
        // 构建敏感词库
        Map sensitiveWordMap = sensitiveWordInit.initKeyWord(sensitiveWords);
        // 传入SensitivewordEngine类中的敏感词库
        SensitiveWordUtils.sensitiveWordMap = sensitiveWordMap;
        String text = "fa轮答案找我";
        // 得到敏感词有哪些，传入2表示获取所有敏感词
        Set<String> set = SensitiveWordUtils.getSensitiveWord(text, 2);
        log.info("获取敏感词内容, 敏感词为: {}", set);
        String newWord = SensitiveWordUtils.replaceSensitiveWord(text, 2, "*");
        log.info("新的文本为:{}", newWord);
    }

}
