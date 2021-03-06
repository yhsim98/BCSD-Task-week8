package serviceImpl;

import domain.User;
import exception.ConflictException;
import exception.NotFoundException;
import exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jdk.nashorn.internal.runtime.ECMAException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import repository.UserRepository;
import service.UserService;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    @Value("${json.web.token.secret.key}")
    String secret;

    @Autowired
    public UserServiceImpl(@Qualifier("myBatis") UserRepository userRepository, JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String userLogin(User user) throws Exception {
        User selectUser = userRepository.getUserByEmail(user.getEmail());

        if(selectUser == null) {
            throw new NotFoundException("account not exist");
        }

        if(!user.getPassword().equals(selectUser.getPassword())){
            throw new UnauthorizedException("password not match");
        }

        return jwtUtil.genJsonWebToken(selectUser.getId());
    }

    @Override
    public String test() { return userRepository.test(); }

    @Override
    public void insertUser(User user) throws Exception {

        User selectUser = userRepository.getUserByEmail(user.getEmail());

        if(selectUser != null) {
            throw new ConflictException("email already being used");
        }

        userRepository.insertUser(user);
    }

    @Override
    public User getUserInfo() throws Exception {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");

        Claims claims = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token.substring(7)).getBody();
        User selectUser = userRepository.getUserById(Long.parseLong(String.valueOf(claims.get("id"))));

        if(selectUser == null){
            throw new NotFoundException("account not exist");
        }

        return selectUser;
    }
}
