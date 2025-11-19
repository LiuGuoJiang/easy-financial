package cn.gson.financial.controller;

import cn.gson.financial.annotation.IgnoresLogin;
import cn.gson.financial.common.MailUtil;
import cn.gson.financial.kernel.aliyuncs.SmsService;
import cn.gson.financial.kernel.controller.JsonResult;
import cn.gson.financial.kernel.model.entity.User;
import cn.gson.financial.kernel.model.vo.UserVo;
import cn.gson.financial.kernel.service.UserService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static cn.gson.financial.kernel.aliyuncs.SmsService.SmsBody;

/**
 * <p>****************************************************************************</p>
 * <p><b>Copyright © 2010-2019 soho team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : cn.gson.financial.controller</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2019年07月30日</li>
 * <li>@author     : ____′↘夏悸</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AppController {

    private final UserService userService;

    private final SmsService smsService;

    private final MailUtil mailUtil;

    @Value("${aliyun.sms.signature}")
    private String smsSignature;

    @Value("${aliyun.sms.template-code.verification}")
    private String verificationCode;

    @Value("${aliyun.sms.template-code.register}")
    private String registerCode;

    @IgnoresLogin
    @RequestMapping
    public JsonResult index() {
        return JsonResult.successful().setMsg("纷析云财务");
    }

    /**
     * 检查登录状获取态和并返回登录信息
     *
     * @param user
     * @return
     */
    @IgnoresLogin
    @GetMapping("/init")
    public JsonResult init(@SessionAttribute(value = "user", required = false) UserVo user) {
        if (user == null) {
            return JsonResult.failure();
        }
        return JsonResult.successful(user);
    }

    /**
     * 登录
     *
     * @param mobile
     * @param password
     * @return
     */
    @IgnoresLogin
    @PostMapping("/login")
    public JsonResult login(String mobile, String password, HttpSession session) {
        UserVo user = userService.login(mobile, password);
        session.setAttribute("user", user);
        return JsonResult.successful();
    }

    /**
     * 登出
     *
     * @return
     */
    @GetMapping("/logout")
    public JsonResult logout(HttpSession session) {
        session.invalidate();
        return JsonResult.successful();
    }

    /**
     * 发送短信验证码
     *
     * @param mobile
     * @return
     */
    @IgnoresLogin
    @GetMapping("/sendMsg/{mobile}")
    public JsonResult sendMsg(@PathVariable String mobile, HttpSession session) {
        String numeric = RandomStringUtils.randomNumeric(4);
        log.info("验证码：" + numeric);
        this.sendSmsCode(mobile, numeric);
        session.setAttribute(mobile, numeric);
        return JsonResult.successful();
    }


    /**
     * 注册发送短信验证码
     *
     * @param mobile
     * @return
     */
    @IgnoresLogin
    @GetMapping("/regMsg/{mobile}")
    public JsonResult regMsg(@PathVariable String mobile, HttpSession session) {
        // 检查手机号格式是否正确
        if (!mobile.matches("^1[23456789]\\d{9}$")) {
            return JsonResult.failure("手机号格式错误！");
        }

        LambdaQueryWrapper<User> qw = Wrappers.lambdaQuery();
        qw.eq(User::getMobile, mobile);
        if (this.userService.count(qw) > 0) {
            return JsonResult.failure("此手机号已被注册，请直接用这个手机号登录或者更换手机号注册！");
        }
        String numeric = RandomStringUtils.randomNumeric(4);
        log.info("验证码：{}", numeric);
        session.setAttribute(mobile, numeric);
        this.sendSmsCode(mobile, numeric);
        return JsonResult.successful();
    }


    /**
     * 注册发送短信验证码
     *
     * @param email
     * @return
     */
    @IgnoresLogin
    @GetMapping("/emailMsg")
    public JsonResult emailMsg(@RequestParam String email, HttpSession session) {
        // 检查email格式是否正确
        if (!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
            return JsonResult.failure("邮箱格式错误！");
        }

        LambdaQueryWrapper<User> qw = Wrappers.lambdaQuery();
        qw.eq(User::getEmail, email);

        if (this.userService.count(qw) > 0) {
            return JsonResult.failure("此邮箱已被注册！");
        }
        String numeric = RandomStringUtils.randomNumeric(4);
        log.info("验证码：{}", numeric);
        session.setAttribute(email, numeric);
        mailUtil.sendMailCode(email, smsSignature, numeric);
        return JsonResult.successful();
    }

    /**
     * 注册
     *
     * @param mobile
     * @return
     */
    @IgnoresLogin
    @PostMapping("/register")
    public JsonResult register(@RequestParam String mobile, @RequestParam String email, @RequestParam String password, @RequestParam String code, HttpSession session) {
        LambdaQueryWrapper<User> qw = Wrappers.lambdaQuery();
        qw.eq(User::getMobile, mobile);
        if (this.userService.count(qw) > 0) {
            return JsonResult.failure("此手机号已被注册，请直接用这个手机号登录或者更换手机号注册！");
        }
        qw = Wrappers.lambdaQuery();
        qw.eq(User::getEmail, email);
        if (this.userService.count(qw) > 0) {
            return JsonResult.failure("此邮箱已被注册！");
        }

        // 密码强度校验
        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_+`={}:';<>?,./-]).{6,16}$")) {
            return JsonResult.failure("密码必须包含字母、数字和特殊字符，长度为6-16位！");
        }

        if (!code.equals(Optional.ofNullable(session.getAttribute(email)).orElse(session.getAttribute(mobile)))) {
            return JsonResult.failure("验证码错误！");
        }

        session.removeAttribute(mobile);
        session.removeAttribute(email);

        User user = new User();
        user.setMobile(mobile);
        user.setEmail(email);
        user.setRealName(mobile);
        if (StrUtil.isEmpty(password)) {
            user.setInitPassword(mobile.substring(6));
            user.setPassword(DigestUtils.sha256Hex(user.getInitPassword()));
        } else {
            user.setPassword(DigestUtils.sha256Hex(password));
        }

        this.userService.save(user);

        session.setAttribute("user", this.userService.getUserVo(user.getId()));
        return JsonResult.successful();
    }

    /**
     * 更新个人设置
     *
     * @param realName、email
     * @return
     */
    @PostMapping("/updateUser")
    public JsonResult updateUser(String realName, String email, @SessionAttribute(value = "user", required = false) UserVo user, HttpSession session) {
        user.setRealName(realName);
        user.setEmail(email);
        userService.updateById(user);
        session.setAttribute("user", user);
        return JsonResult.successful();
    }

    /**
     * 重置密码
     *
     * @param newPassword
     * @param repeatPassword
     * @param mobile
     * @param code
     * @param session
     * @return
     */
    @IgnoresLogin
    @PostMapping("/resetPassword")
    public JsonResult resetPassword(@RequestParam String newPassword,
                                    @RequestParam String repeatPassword,
                                    @RequestParam String mobile,
                                    @RequestParam String code, HttpSession session) {
        JsonResult result = JsonResult.instance(true);

        if (!code.equals(session.getAttribute(mobile))) {
            result = JsonResult.failure("验证码错误！");
        } else if (!newPassword.equals(repeatPassword)) {
            result = JsonResult.failure("密码设置错误！");
        }

        if (result.isSuccess()) {
            LambdaQueryWrapper<User> qw = Wrappers.lambdaQuery();
            qw.eq(User::getMobile, mobile);
            User user = userService.getOne(qw);
            if (user != null) {
                user.setPassword(DigestUtils.sha256Hex(newPassword));
                user.setInitPassword("");
                userService.updateById(user);

                session.removeAttribute(mobile);
            } else {
                result = JsonResult.failure("用户不存在！");
            }
        }

        return result;
    }

    /**
     * 修改密码
     *
     * @param original、newPassword
     * @return
     */
    @PostMapping("/updatePwd")
    public JsonResult updatePwd(String original, String newPassword, @SessionAttribute(value = "user", required = false) UserVo user, HttpSession session) {
        //核对旧密码
        User u = userService.getById(user.getId());
        if (!DigestUtils.sha256Hex(original).equals(u.getPassword())) {
            return JsonResult.failure("原密码错误!");
        }

        //更新新密码
        user.setPassword(DigestUtils.sha256Hex(newPassword));
        user.setInitPassword("");
        userService.updateById(user);
        return JsonResult.successful();
    }

    /**
     * 修改手机号码
     *
     * @param verificationCode，mobile
     * @return
     */
    @PostMapping("/changePhoneNumber")
    public JsonResult changePhoneNumber(String verificationCode, String mobile, @SessionAttribute(value = "user", required = false) UserVo user, HttpSession session) {
        //Session里获取手机号对应验证码进行比对
        if (!verificationCode.equals(session.getAttribute(mobile))) {
            return JsonResult.failure("验证码错误!");
        }

        //更新手机号
        user.setMobile(mobile);
        userService.updateById(user);
        session.setAttribute("user", user);
        return JsonResult.successful();
    }

    /**
     * 变更默认账套
     *
     * @param accountSetsId
     * @param user
     * @param session
     * @return
     */
    @GetMapping("/changeAccountSets")
    public JsonResult changeAccountSets(Integer accountSetsId, @SessionAttribute(value = "user", required = false) UserVo user, HttpSession session) {
        if (user.getAccountSetsList().stream().anyMatch(accountSets -> accountSets.getId().equals(accountSetsId))) {
            user.setAccountSetsId(accountSetsId);
            userService.updateById(user);
            session.setAttribute("user", userService.getUserVo(user.getId()));
        }
        return JsonResult.successful();
    }

    private void sendSmsCode(@PathVariable String mobile, String numeric) {
        log.info("smsSignature:{}", smsSignature);
        log.info("verificationCode:{}", verificationCode);
        SmsBody smsBody = new SmsBody(mobile, smsSignature, verificationCode);
        Map<String, String> params = new HashMap<>(1);
        params.put("code", numeric);
        smsBody.setTemplateParam(params);
        smsService.send(smsBody);
    }

    private void sendPasswordSms(@PathVariable String mobile, String password) {
        SmsBody smsBody = new SmsBody(mobile, smsSignature, registerCode);
        Map<String, String> params = new HashMap<>(1);
        params.put("password", password);
        smsBody.setTemplateParam(params);
        smsService.send(smsBody);
    }
}
