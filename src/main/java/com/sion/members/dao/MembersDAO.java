package com.sion.members.dao;

import com.sion.members.dto.MembersDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MembersDAO {

    @Autowired
    private SqlSession mybatis;

    /** 로그인 정보 검사 **/
    public boolean isMembers(MembersDTO dto) {
        return mybatis.selectOne("Members.isMembers", dto);
    }

    public MembersDTO getMember(String id){
        return mybatis.selectOne("Members.getMember", id);
    }

}
