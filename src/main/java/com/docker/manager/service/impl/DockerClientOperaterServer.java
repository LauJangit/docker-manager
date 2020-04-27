package com.docker.manager.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.docker.manager.common.utils.JsonResult;
import com.docker.manager.dao.BaseContainerMapper;
import com.docker.manager.dto.ContainerDTO;
import com.docker.manager.response.PageDataResult;
import com.docker.manager.service.DockerClientOperaterService;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 操作docker的客户端
 * 
 * @author 公众号：18岁fantasy
 * @date 2019年6月12日 下午4:18:53
 */
@Component
public class DockerClientOperaterServer implements DockerClientOperaterService {
  private Logger logger = LoggerFactory.getLogger(this.getClass());


  /**
   * spring 配置文件注入，默认从application.properties 中获取
   */
  @Value("${docker_url}")
  private String dockerUrl;
  /**
   * 客户端
   */
  private DockerClient dockerClient;


  /**
   * docker 基本信息
   */
//  private static DockerVo dockerVo;

  /**
   * 容器监控信息
   */
  private static String stats;

  /**
   * 监控线程是否启动
   */
  private static Boolean statsbegin = false;

  @Autowired
  private BaseContainerMapper baseContainerMapper;

  @Override
  public Map<String,Object> addContainer(ContainerDTO containerDTO) {
    Map<String,Object> data = new HashMap();
    List<ContainerDTO> list = new ArrayList<>();
    list.add(containerDTO);
    try {
      int result = baseContainerMapper.insertBatch(list);
      System.out.println("result:" + result);
      if(result == 0){
        data.put("code",0);
        data.put("msg","新增失败！");
        logger.error("主机[新增]，结果=新增失败！");
        return data;
      }
      data.put("code",1);
      data.put("msg","新增成功！");
      logger.info("主机[新增]，结果=新增成功！");
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("主机[新增]异常！", e);
      return data;
    }
    return data;
  }

  @Override
  public PageDataResult getContainerList(Integer pageNum, Integer pageSize, String ip) {
    PageDataResult pageDataResult = new PageDataResult();

    dockerClient = DockerClientBuilder.getInstance("tcp://"+ip+":2375").build();
    List<Container> exec = dockerClient.listContainersCmd().withShowAll(true).exec();
    List<ContainerDTO> list = convertDTO(exec,ip);
    baseContainerMapper.insertBatch(list);
    List<ContainerDTO> containerDTOS = baseContainerMapper.getContainerList(ip);
    if(containerDTOS.size() != 0){
      for (int i = 0; i < containerDTOS.size(); i++) {
        System.out.println("container容器id:" + containerDTOS.get(i).getId());
      }
      pageDataResult.setList(containerDTOS);
      pageDataResult.setTotals(containerDTOS.size());
    }


//    String infoStr = JSONObject.toJSONString(info);
    System.out.println("docker的环境信息如下：=================");
    for (Container container : exec) {
      for (int i=0;i<container.getNames().length;i++)
        System.out.println("Names:"+container.getNames()[i]);
    }

    return pageDataResult;
  }

  public List<ContainerDTO> convertDTO(List<Container> containerList,String ip){
    List<ContainerDTO> containerDTOList= new ArrayList<>();
    for (int i = 0; i < containerList.size(); i++) {
      ContainerDTO containerDTO = new ContainerDTO();
      Container container = containerList.get(i);
      containerDTO.setId(container.getId());
      String containnames = "";
      String ports = "";;
      for (int j = 0; j < container.getNames().length; j++){
        containnames = containnames + container.getNames()[j];
      }
      containerDTO.setContainnames(containnames);
      containerDTO.setImage(container.getImage());
      for (int j = 0; j < container.getPorts().length; j++){
        ports = ports + container.getPorts()[j];
      }
      containerDTO.setPorts(ports);
      containerDTO.setStatus(container.getStatus());
      containerDTO.setIp(ip);
      containerDTOList.add(containerDTO);
    }
    return containerDTOList;
  }

  @Override
  public JsonResult startContainer(String containerId) {
    ContainerDTO container = baseContainerMapper.getContainer(containerId);
    if (!container.getStatus().contains("Up")) {
      dockerClient.startContainerCmd(containerId).exec();
      return JsonResult.ok("启动成功");
    } else {
      return JsonResult.error("启动失败,原因：容器已启动");
    }
  }

  @Override
  public JsonResult endContainer(String containerId) {
    ContainerDTO container = baseContainerMapper.getContainer(containerId);
    if (!container.getStatus().contains("Exited")) {
      dockerClient.stopContainerCmd(containerId).exec();
      return JsonResult.ok("关闭成功");
    } else {
      return JsonResult.error("关闭失败,原因：容器已关闭");
    }
  }
  public static void main(String[] args) throws URISyntaxException, InterruptedException {
//    本地调用
//    try {
//      System.out.println("开始监控程序...");
//      String[] commands = {"docker","ps","-a"};
//      BufferedReader br = new BufferedReader(
//              new InputStreamReader(Runtime.getRuntime().exec(commands).getInputStream()));
//      // StringBuffer b = new StringBuffer();
//      String line = null;
//      StringBuffer b = new StringBuffer();
//      while ((line = br.readLine()) != null) {
//        System.out.println("监控结果，line：" + line);
//        String[] strings = line.split(" ");
//        for (int i = 0; i < strings.length; i++){
//          if (!strings[i].trim().equals("")) {
//            System.out.println("字符串读取：" + strings[i].trim());
//          }
//        }
//        if (line.indexOf("CONTAINER ID") != -1 && b.length() != 0) {
//          stats = b.toString();
//          System.out.println("监控结果，b：" + stats);
//          System.out.println("监控结果，line：" + line);
//          b.delete(0, b.length());
//          b.append(
//                  "<span style='background-color: #0033dd;font-size:20px'>CONTAINER ID        NAME             CPU %           MEM USAGE / LIMIT     MEM %               NET I/O             BLOCK I/O     PIDS</span><br/>");
//        } else {
//          b.append(line + "<br/>");
//        }
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }

//    System.out.println(infoStr);
  }

  /**
   * 初始化docker客户端链接
   *
   * @param dockerUrl
   */
//  public void initClient(String dockerUrl) {
//    if (dockerClient == null) {
//      System.out.println("初始化docker连接:" + dockerUrl);
//      this.dockerClient = DockerClientBuilder.getInstance(dockerUrl).build();
//    }
//    stat();
//    refreshContainers();
//
//  }

  /**
   * 获取docker服务基本信息
   */
//  public DockerVo getDockerInfo() {
//
//    if (dockerVo == null) {
//      dockerVo = new DockerVo();
//      Info info = dockerClient.infoCmd().exec();
//      System.out.println("info:" + info);
//      dockerVo.setTotalMem(info.getMemTotal());
//      dockerVo.setImages(info.getImages());
//      dockerVo.setCpus(info.getNCPU());
//    }
//    return dockerVo;
//  }

  /**
   * 获取监控信息
   * 
   * @return
   */
  public String getStatStr() {
    return stats;
  }

  /**
   * 获取监控信息
   * 
//   * @param containtId 容器id或者容器名称
   */
//  public void stat() {
//    if (!statsbegin) {
//      synchronized (statsbegin) {
//        new Thread(new Runnable() {
//
//          public void run() {
//            try {
//              System.out.println("开始监控程序...");
//              String statCmd = "docker stats ";
//              BufferedReader br = new BufferedReader(
//                  new InputStreamReader(Runtime.getRuntime().exec(statCmd).getInputStream()));
//              // StringBuffer b = new StringBuffer();
//              String line = null;
//              StringBuffer b = new StringBuffer();
//              while ((line = br.readLine()) != null) {
//                if (line.indexOf("NAME") != -1 && b.length() != 0) {
//                  stats = b.toString();
//                  System.out.println("监控结果：" + stats);
//                  b.delete(0, b.length());
//                  b.append(
//                      "<span style='background-color: #0033dd;font-size:20px'>CONTAINER ID        NAME             CPU %           MEM USAGE / LIMIT     MEM %               NET I/O             BLOCK I/O     PIDS</span><br/>");
//                } else {
//                  b.append(line + "<br/>");
//                }
//
//              }
//            } catch (Exception e) {
//              e.printStackTrace();
//            }
//          }
//        }).start();
//        statsbegin = true;
//      }
//    }
//    // 也可以用docker-java接口获取，但是需要自行解析和计算百分比比较繁琐
//    /*
//     * dockerClient.statsCmd(containtId).exec(new ResultCallback<Statistics>() {
//     *
//     * public void close() throws IOException { } public void onStart(Closeable closeable) { }
//
//       * public void onNext(Statistics object) { System.out.println("######"+object);
//       * //sts.put(containtId, object.) } public void onError(Throwable throwable) { } public
//       * void onComplete() {
//       *
//       * }});
//       */
//  }

  /**
   * 初始化docker链接
   * 
//   * @param dockerUrl
   */
//  public void initClient() {
//    initClient(dockerUrl);
//  }

  /**
   * 获取容器列表
   * 
   * @return
   */
//  public List<ContainerVO> getContainers() {
//    List<ContainerVO> containerVOs = new ArrayList<ContainerVO>();
//    for (Iterator<ContainerVO> iterator = cs.values().iterator(); iterator.hasNext();) {
//      containerVOs.add(iterator.next());
//    }
//    return containerVOs;
//  }

  /**
   * 将上传到主机的app复制到tomcat，让tomcat加载
   * 
   * @param containerId
   * @param appRootPath
   */
//  public void pushAppToCotainer(String containerId, String appRootPath) {
//    dockerClient.copyArchiveToContainerCmd(containerId).withHostResource(appRootPath)
//        .withRemotePath("/usr/local/tomcat/webapps").exec();
//  }

  /**
   * 获取tomcat应用的缓存地址
   * 
   * @param containerId
   * @param appName
   * @return
   */
//  public String getAppAccessUrl(String containerId, String appName) {
//    ContainerVO containerVO = cs.get(containerId);
//    return "http://localhost:" + containerVO.getPubPort() + "/" + appName;
//  }

  /**
   * 刷新容器列表
   */
//  private void refreshContainers() {
//    List<ContainerDTO> dockerSearch = dockerClient.listContainersCmd().exec();
//    for (Iterator<ContainerDTO> iterator = dockerSearch.iterator(); iterator.hasNext();) {
//      ContainerDTO container = (ContainerDTO) iterator.next();
//      ContainerPort[] containerPort = container.getPorts();
//      // 获取容器详细信息
//      InspectContainerResponse containerInfo =
//          dockerClient.inspectContainerCmd(container.getId()).exec();
//      ContainerVO containerVO = new ContainerVO();
//      containerVO.setContainerName(containerInfo.getName());
//      containerVO.setContainerId(containerInfo.getId());
//      containerVO.setImageName(container.getImage());
//      containerVO.setInnerPort(containerPort[0].getPrivatePort());
//      containerVO.setPubPort(containerPort[0].getPublicPort());
//      containerVO.setCpu(containerInfo.getHostConfig().getCpuShares());
//      containerVO.setMemLimit(containerInfo.getHostConfig().getMemory());
//      containerVO.setCreateTime(containerInfo.getCreated());
//      containerVO.setStatus(containerInfo.getState().getStatus());
//
//      if (!cs.containsKey(container.getId())) {
//        cs.put(container.getId(), containerVO);
//      }
//    }
//  }

  /**
   * 创建并启动容器,同时部署应用
   */
//  public String createAndStartrContainerAnddeployApp(String tomVersion, File appLocalFile,
//      Integer cpu, Long mem) {
//
//    String containerId = createContainer(tomVersion, cpu, mem).getId();
//    startContainer(containerId);
//    pushAppToCotainer(containerId, appLocalFile.getAbsolutePath());
//    String appName = appLocalFile.getName().split("\\.")[0];
//    ContainerVO containerVO = cs.get(containerId);
//    String containerName = containerVO.getContainerName();
//    String accessUrl = getAppAccessUrl(containerId, appName);
//    containerVO.setAccessUrl(accessUrl);
//    containerVO.setCpu(cpu);
//    containerVO.setMemLimit(mem);
//    return containerName;
//  }

  /**
   * 创建并启动容器
   */
//  public void createAndStartrContainer(String tomVersion, Integer cpu, Long mem) {
//    startContainer(createContainer(tomVersion, cpu, mem).getId());
//  }

  /**
   * 创建容器
   * 
   * @return
   */
//  public CreateContainerResponse createContainer(String tomVersion, Integer cpuShares,
//      Long memory) {
//    ExposedPort tcp8080 = ExposedPort.tcp(8080);
//
//    Ports portBindings = new Ports();
//    portBindings.bind(tcp8080, Ports.Binding.empty());
//    String containerName = "app" + RandomUtils.nextInt();
//
//    CreateContainerResponse newContainer = dockerClient.createContainerCmd(tomVersion)// 镜像
//        .withName(containerName)// 容器名称
//        .withExposedPorts(tcp8080).withPortBindings(portBindings)// 端口绑定
//        .withCpuShares(cpuShares).withMemory(memory * 1024 * 1024)// 限额,参数
//        .exec();
//
//    System.out.println("创建容器：" + containerName);
//
//    return newContainer;
//  }

  /**
   * 启动容器
   * 
   * @param containerId
   */
//  public void startContainer(String containerId) {
//    dockerClient.startContainerCmd(containerId).exec();
//    refreshContainers();
//    System.out.println("启动容器：" + cs.get(containerId).getContainerName());
//  }

  /**
   * 停止容器
   * 
   * @param containerId
   */
//  public void stopContainer(String containerId) {
//    dockerClient.stopContainerCmd(containerId).exec();
//    System.out.println("停止容器：" + cs.get(containerId).getContainerName());
//    refreshContainers();
//  }

  /**
   * 删除容器
   * 
   * @param containerId
   */
//  public void rmContainer(String containerId) {
//    dockerClient.removeContainerCmd(containerId).exec();
//    System.out.println("删除容器：" + cs.get(containerId).getContainerName());
//    refreshContainers();
//  }

  /**
   * 关闭docker连接
   * 
//   * @param containerId
   * @throws IOException
   */
//  public void closeClient() throws IOException {
//    dockerClient.close();
//  }

  /**
   * spring bean初始化后执行此方法，初始化docker连接
   */
//  public void afterPropertiesSet() throws Exception {
//    initClient();
//  }

}
