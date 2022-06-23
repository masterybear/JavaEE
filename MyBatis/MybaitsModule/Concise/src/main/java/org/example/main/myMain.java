package org.example.main;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.example.mapper.RoleMapper;
import org.example.pojo.Role;
import org.example.utils.SqlSessionFactoryUtils;

public class myMain {
    public static void main(String[] args) {

        Logger log = Logger.getLogger(myMain.class);
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtils.openSqlSession();
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            Role role = roleMapper.getRole(1L);
            log.info(role.getRoleName());
        }finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}
