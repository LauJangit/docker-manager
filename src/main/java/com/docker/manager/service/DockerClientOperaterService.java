package com.docker.manager.service;

import com.docker.manager.common.utils.JsonResult;
import com.docker.manager.dto.ContainerDTO;
import com.docker.manager.response.PageDataResult;

import java.util.Map;


/**
 * @Title: AdminUserService
 * @Description:
 * @version: 1.0
 */
public interface DockerClientOperaterService {

    Map<String,Object> addContainer(ContainerDTO containerDTO);

    PageDataResult getContainerList(Integer pageNum, Integer pageSize, String ip);

    JsonResult startContainer(String containerId);

    JsonResult endContainer(String containerId);
}
