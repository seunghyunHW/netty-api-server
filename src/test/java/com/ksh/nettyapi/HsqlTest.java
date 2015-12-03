package com.ksh.nettyapi;

import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-03
 * Time : 오후 12:33
 * To change this template use File | Settings | File and Code Templates.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/hsqlApplicationContext.xml")
@Transactional
public class HsqlTest {

    @Autowired
    private SqlSession sqlSession;

    @Test
    public void test() {
        List<Map<String, Object>> userList = sqlSession.selectList("users.list");
        for (Map<String, Object> user : userList) {
            assertNotNull(user.get("USERID"));
            assertNotNull(user.get("USERNAME"));
            assertNotNull(user.get("PASSWORD"));
        }
    }


}