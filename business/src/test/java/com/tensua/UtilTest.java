package com.tensua;

import com.tensua.config.component.PushComponent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhooke
 * @since 2023/3/10 17:47
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BusinessApplication.class)
public class UtilTest {
    @Autowired
    private PushComponent pushComponent;

    @Test
    public void pushTest(){
        pushComponent.pushDingTalk("测试消息");
    }
}
