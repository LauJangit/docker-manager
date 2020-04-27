package com.docker.manager.dto;

import lombok.Data;

/**
 * 容器基本信息

 */
@Data
public class ContainerDTO {

  private String containnames;//容器名
  private String id;//容器id
  private String image;//镜像名
  private String ports;//端口映射的主机端口
  private String status;//容器状态
  private String ip;//ip地址



}
