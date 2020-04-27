package com.docker.manager.service.impl;

import com.docker.manager.dao.BaseHostMapper;
import com.docker.manager.dto.HostDTO;
import com.docker.manager.pojo.BaseHost;
import com.docker.manager.response.PageDataResult;
import com.docker.manager.service.HostService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.catalina.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.NewThreadAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: AdminUserServiceImpl
 * @Description:
 * @version: 1.0
 */
@Service
public class HostServiceImpl implements HostService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BaseHostMapper baseHostMapper;

    @Override
    public PageDataResult getList(Integer user_id,Integer pageNum, Integer pageSize) {
        PageDataResult pageDataResult = new PageDataResult();
        List<HostDTO> hostDTOList = baseHostMapper.getHostList(user_id);

        PageHelper.startPage(pageNum, pageSize);

        if(hostDTOList.size() != 0){
            PageInfo<HostDTO> pageInfo = new PageInfo<>(hostDTOList);
            pageDataResult.setList(hostDTOList);
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }

        return pageDataResult;
    }

    @Override
    public Map<String,Object> addHost(HostDTO host) {
        Map<String,Object> data = new HashMap();
        List<HostDTO> list = new ArrayList<>();
        list.add(host);
        try {
            int result = baseHostMapper.insertHost(list);
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

}
