package com.docker.manager.controller.system;


import com.docker.manager.dto.AdminUserDTO;
import com.docker.manager.dto.HostDTO;
import com.docker.manager.dto.PermissionDTO;
import com.docker.manager.pojo.BaseAdminPermission;
import com.docker.manager.pojo.BaseAdminUser;
import com.docker.manager.response.PageDataResult;
import com.docker.manager.service.AdminPermissionService;
import com.docker.manager.service.HostService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: HostController
 * @Description: 主机管理
 * @version: 1.0
 */
@Controller
@RequestMapping("host")
public class HostController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HostService hostService;

    /**
     *
     * 功能描述: 跳到容器管理
     *
     * @param:
     * @return:
     */
    @RequestMapping("hostManage")
    public String hostManage() {
        logger.info("进入主机管理");
        return "/host/hostManage";
    }

    /**
     *
     * 功能描述: 分页查询主机列表
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "/getHostList", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getHostList(@RequestParam("pageNum") Integer pageNum,
                                           @RequestParam("pageSize") Integer pageSize, HttpSession session) {
        Integer userId = Integer.valueOf(session.getAttribute("userId").toString());
        PageDataResult pdr = new PageDataResult();
        try {
            if(null == pageNum) {
                pageNum = 1;
            }
            if(null == pageSize) {
                pageSize = 10;
            }
            pdr = hostService.getList(userId,pageNum ,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户列表查询异常！", e);
        }
        return pdr;
    }


    /**
     *
     * 功能描述: 新增和更新主机
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "/setHost", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> setHost(HostDTO hostDTO,HttpSession session) {
        int userId = Integer.parseInt(session.getAttribute("userId").toString()) ;
        logger.info("设置主机[新增或更新]！user:" + hostDTO);
        hostDTO.setUserid(userId);
        Map<String,Object> data = new HashMap();
        data = hostService.addHost(hostDTO);
        return data;
    }
}
