package com.sion.members.service;

import com.sion.members.dao.MembersDAO;
import com.sion.members.dto.MembersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MembersService implements UserDetailsService {

    @Autowired
    private MembersDAO membersDAO;

    public boolean isMembers(MembersDTO dto) {
        return membersDAO.isMembers(dto);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MembersDTO dto = membersDAO.getMember(username);
        return new User(dto.getId(), dto.getPw(), AuthorityUtils.createAuthorityList(dto.getRole()));
    }
}
