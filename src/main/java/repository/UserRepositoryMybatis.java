package repository;

import domain.User;
import mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryMybatis implements UserRepository{

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByEmail(String email) {
        User user = userMapper.getUserByEmail(email);
        return user;
    }

    @Override
    public String test(){ return userMapper.test(); }
}