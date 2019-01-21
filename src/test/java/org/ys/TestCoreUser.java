package org.ys;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.ys.core.dao.CoreUserMapper;
import org.ys.core.model.CoreUser;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestCoreUser {
    @Autowired
    private CoreUserMapper coreUserMapper;

    @Test
    public void testInsert(){
        CoreUser coreUser = new CoreUser();
        coreUser.setUserName("test");
        coreUserMapper.insert(coreUser);
    }
}
