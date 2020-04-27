package com.docker.manager.controller.system;


import com.docker.manager.common.utils.JsonResult;
import com.docker.manager.dto.ContainerDTO;
import com.docker.manager.response.PageDataResult;
import com.docker.manager.service.impl.DockerClientOperaterServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: ContainerController
 * @Description: 容器管理
 * @version: 1.0
 */
@Controller
@RequestMapping("container")
public class ContainerController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DockerClientOperaterServer dockerClientOperaterServer;

    /**
     *
     * 功能描述: 新增和更新容器
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "/setContainer", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> setContainer(ContainerDTO containerDTO) {
        Map<String,Object> data = new HashMap();
        data = dockerClientOperaterServer.addContainer(containerDTO);
        return data;
    }

    /**
     *
     * 功能描述: 跳到容器管理
     *
     * @param:
     * @return:
     */
    @RequestMapping("containerManage")
    public String containerManage(@RequestParam("ip") String ip , HttpSession session) {
        System.out.println("ip:" + ip);
        session.setAttribute("ip", ip);
        logger.info("进入容器管理");
        return "/container/containerManage";
    }//等等


    /**
     *
     * 功能描述: 分页查询容器列表
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "/getContainerList", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getContainerList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize, HttpSession session) {
        /*logger.info("分页查询用户列表！搜索条件：userSearch：" + userSearch + ",pageNum:" + page.getPageNum()
                + ",每页记录数量pageSize:" + page.getPageSize());*/
        PageDataResult pdr = new PageDataResult();
        try {
            if(null == pageNum) {
                pageNum = 1;
            }
            if(null == pageSize) {
                pageSize = 10;
            }
            String ip =  session.getAttribute("ip").toString();
            pdr = dockerClientOperaterServer.getContainerList(pageNum ,pageSize,ip);
            // 获取用户列表
//            pdr = adminUserService.getUserList(userSearch, pageNum ,pageSize);
//            logger.info("用户列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("容器列表查询异常！", e);
        }
        return pdr;
    }


    /**
     *
     * 功能描述: 进入容器内部进行控制
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "/enterContainer")
    public String enterContainer(@RequestParam("id") String id , HttpSession session) {
        System.out.println("container:" + id);
        session.setAttribute("containerId", id);
        return "/container/enterContainer";
    }

    /**
     *
     * 功能描述: 启动容器
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "/startContainer")
    @ResponseBody
    public JsonResult startContainer(HttpSession session) {
        String containId = session.getAttribute("containerId").toString();
        System.out.println("containerId:" + containId);
        return  dockerClientOperaterServer.startContainer(containId);
    }

    /**
     *
     * 功能描述: 关闭容器
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "/endContainer")
    @ResponseBody
    public JsonResult endContainer(HttpSession session) {
        String containId = session.getAttribute("containerId").toString();
        System.out.println("containerId:" + containId);
        return  dockerClientOperaterServer.endContainer(containId);
    }
}
