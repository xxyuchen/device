package com.geeker.controller;

import com.geeker.model.User;
import com.geeker.response.Response;
import com.geeker.response.ResponseUtils;
import com.geeker.service.OpDeviceService;
import com.geeker.utils.LoginUserUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/16 0016.
 */
@RestController
@RequestMapping("phone")
public class OpDevicePhoneController {
    @Resource
    private OpDeviceService opDeviceService;
    /**
     * 打电话
     * @param custId
     * @return
     */
    @RequestMapping("/call")
    public Response call(Integer custId) throws Exception {
        return opDeviceService.call(custId);
    }
    /**
     * 同步通讯录
     * @param synTime
     * @return
     */
    @RequestMapping("/phoneBook")
    public Response phoneBook(Date synTime) throws Exception {
        User user = LoginUserUtil.getUser();
        opDeviceService.phoneBook(synTime,user.getId(),user.getDeviceId(),user.getCompanyId());
        return ResponseUtils.success();
    }
    /**
     * 同步群组
     * @param synTime
     * @return
     */
    @RequestMapping("/group")
    public Response group(Date synTime) throws Exception {
        User user = LoginUserUtil.getUser();
        opDeviceService.group(synTime,user.getId(),user.getDeviceId(),user.getCompanyId());
        return ResponseUtils.success();
    }
    /**
     * 发送短信
     * @return
     */
    @RequestMapping("/sendSms")
    public Response sendSms(Integer custId,String parm) throws Exception {
        return opDeviceService.sendSms(custId,parm);
    }
}