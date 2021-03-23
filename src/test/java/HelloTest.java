import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.moyao.DemoApplication;
import com.moyao.dto.UserDto;
import com.moyao.dubbo.HelloDubboService;
import com.moyao.entity.User;
import com.moyao.mapper.UserMapper;
import com.moyao.service.UserService;

@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HelloTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @DubboReference
    private HelloDubboService helloDubboService;

    @Test
    public void testWeb() {
        ResponseEntity<String> entity = testRestTemplate.getForEntity("/hello", String.class, 0);
        System.err.println(entity.getBody());
    }

    @Test
    public void testDubbo(){
        UserDto user = helloDubboService.findUser(47L);
        System.err.println(user.getUsername());
    }

    @Test
    public void testDao() {
        User user = userMapper.findById(47L);
        System.err.println(user.getUsername());
    }

    @Test
    public void testAll() {
        List<User> users = userMapper.selectAll();
        users.forEach(System.out::println);
    }

    @Test
    public void testService() {
        UserDto user = userService.findUser(47L);
        System.err.println(user.getUsername());
    }
}

