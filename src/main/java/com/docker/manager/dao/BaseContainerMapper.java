package com.docker.manager.dao;


import com.docker.manager.dto.ContainerDTO;
import com.docker.manager.pojo.BaseAdminUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mapper.MyMapper;

import java.util.List;

@Repository
public interface BaseContainerMapper extends MyMapper<BaseAdminUser> {

    List<ContainerDTO> getContainerList(String ip);

    ContainerDTO getContainer(String id);

    int insertBatch(List<ContainerDTO> list);


    BaseAdminUser getUserByUserName(@Param("sysUserName") String sysUserName, @Param("id") Integer id);

    int updateUserStatus(@Param("id") Integer id, @Param("status") Integer status);

    int updateUser(BaseAdminUser user);

    BaseAdminUser findByUserName(@Param("userName") String userName);

    int updatePwd(@Param("userName") String userName, @Param("password") String password);

}