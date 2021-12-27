package cn.jaylong.auth.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.jaylong.auth.po.User;
import cn.jaylong.auth.po.VerifierEmbed;
import cn.jaylong.auth.repository.UserJpaRepository;
import cn.jaylong.auth.security.model.UserDetails;
import cn.jaylong.auth.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * Author: I'm
 * Date: 2021/9/22
 */
@RestController
@Api(tags = "测试模块")
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final UserJpaRepository jpaRepository;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Value("${app.name}")
    public String appName;

    @ApiOperation("测试app name")
    @GetMapping("/value")
    public String value() {
        return appName;
    }

    @ApiOperation("测试保存user数据")
    @GetMapping("/save")
    @Transactional
    public void insert(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String roles,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber
    ) {
        // 更新
        User user = new User();
        if (StrUtil.isNotBlank(id)) {
            user = jpaRepository.findById(Convert.toStr(id)).orElse(user);
        }
        String random = RandomUtil.randomNumbers(32);
        user.setId(StrUtil.isBlank(id) ? random : id);
        if (StrUtil.isBlank(username)) {
            if (user.getUsername() != null) {
                username = user.getUsername();
            } else {
                username = random;
            }
        }
        user.setUsername(username);
        if (StrUtil.isBlank(password)) {
            if (user.getPassword() != null) {
                password = user.getPassword();
            }
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setZoneId("1");
        VerifierEmbed embed = new VerifierEmbed();
        if (StrUtil.isBlank(email)) {
            if (user.getEmail() != null) {
                email = user.getEmail().getValue();
            } else {
                email = random;
            }
        }
        embed.setValue(StrUtil.isBlank(email) ? random : email);
        user.setEmail(embed);
        if (StrUtil.isBlank(phoneNumber)) {
            if (user.getPhoneNumber() != null) {
                phoneNumber = user.getPhoneNumber().getValue();
            } else {
                phoneNumber = random;
            }
        }
        embed.setValue(StrUtil.isBlank(phoneNumber) ? random : phoneNumber);
        user.setPhoneNumber(embed);
        jpaRepository.save(user);
    }

    @ApiOperation("测试删除user数据")
    @GetMapping("/delete")
    public void delete(@RequestParam String id) {
        User user = jpaRepository.findById(id).orElse(new User());
        jpaRepository.delete(user);
    }

    @ApiOperation("测试查询user数据")
    @GetMapping("/query")
    public User query(@RequestParam String id) {
        return jpaRepository.findById(id).orElse(new User());
    }

    @ApiOperation("测试根据用户名获取用户")
    @GetMapping("/loadUserByUsername")
    public UserDetails loadUserByUsername(@RequestParam String username) {
        return userService.loadUserByUsername(username);
    }

    public static void main(String[] strings) {
        String base32Key = "QDWSM3OYBPGTEVSPB5FKVDM3CSNCWHVK";
        Base32 base32 = new Base32();
        byte[] b = base32.decode(base32Key);
        String secret = Hex.encodeHexString(b);
        System.out.println(secret);
    }

}
