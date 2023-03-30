package com.tensua;

import com.tensua.config.component.PushComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhooke
 * @since 2023/3/10 17:47
 **/
@SpringBootTest(classes = BusinessApplication.class)
public class UtilTest {
    @Autowired
    private PushComponent pushComponent;

    @Test
    public void pushTest(){
        pushComponent.pushDingTalk("测试消息");
    }
}
