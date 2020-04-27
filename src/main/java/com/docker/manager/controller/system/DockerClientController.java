package com.docker.manager.controller.system;
import java.io.File;
import java.util.List;

import com.docker.manager.dto.ContainerDTO;
import com.docker.manager.service.impl.DockerClientOperaterServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
/**
 * docker 管理controller
 * @date 2019年6月13日 下午5:08:11
 */
@Controller
public class DockerClientController {

  /**
   * docker 交互层服务
   */
  @Autowired
  private DockerClientOperaterServer dockerOper;

  /**
   * tomcat war包 主机临时存放目录
   */
  public static final String appLocalDir = "d:\\docker_app\\";

  /**
   * 首界面
   * @param model
   * @return
   */
//  @RequestMapping(value = "/")
//  public String req(Model model) {
//    List<ContainerVO> cls = dockerOper.getContainers();
//    model.addAttribute("cls", cls);//容器
//    model.addAttribute("dockerInfo", dockerOper.getDockerInfo());//docker服务信息
//    return "index";
//  }

  /**
   * 添加容器
   * @param file
   * @param tomVersion
   * @param cpu
   * @param mem
   * @return
   */
//  @RequestMapping(value = "/add_container")
//  @ResponseBody
//  public String addContainer(@RequestParam("war") MultipartFile file, @RequestParam("tomVersion") String tomVersion,Integer cpu,Long mem) {
//    try {
//
//      if (file.isEmpty()) {
//        return jsonResultFail("请选择war文件");
//      }
//      File localApp = new File(appLocalDir + file.getOriginalFilename());
//      file.transferTo(localApp);
//      String containerName = dockerOper.createAndStartrContainerAnddeployApp(tomVersion,localApp,cpu,mem);
//
//      return jsonResultSuccess("创建成功,容器名："+containerName);
//
//    } catch (Exception e) {
//      return jsonResultFail("创建失败,"+e.getMessage());
//    }
//  }
  /**
   * 容器监控，定时由界面触发，也可以改为用websocket向界面推送
   * @return
   */
  @RequestMapping(value = "/stats")
  @ResponseBody
  public String getStats() {
    try {
      return jsonResultSuccess(dockerOper.getStatStr());
    } catch (Exception e) {
      return jsonResultFail("创建失败,"+e.getMessage());
    }
  }
  //------------------------------------------组装json返回结果
  public String jsonResultSuccess(String msg) {
    return "{ \"code\": \"1\",\"msg\": \""+msg+"\"}";
  }
  
  public String jsonResultFail(String msg) {
    return "{ \"code\": \"0\",\"msg\": \""+msg+"\"}";
  }
}
