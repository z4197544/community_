package life.zxw.community.community;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class CommunityApplicationTests {

    @Test
    void contextLoads() {
        List<String> string = Arrays.asList("abc","","ba");
        List<String> ss = string.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        List<String> aa = string.stream().map(s -> s.toUpperCase()).collect(Collectors.toList());
        System.out.println(ss);
        System.out.println(aa);
    }

}
