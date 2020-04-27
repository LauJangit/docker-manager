package com.docker.manager.service;

import com.docker.manager.dto.HostDTO;
import com.docker.manager.response.PageDataResult;

import java.util.Map;


/**
 * @Title: AdminUserService
 * @Description:
 * @version: 1.0
 */
public interface HostService {

    PageDataResult getList(Integer user_id,Integer pageNum, Integer pageSize);


    Map<String,Object> addHost(HostDTO host);
}
