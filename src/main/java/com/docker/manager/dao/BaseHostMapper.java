package com.docker.manager.dao;


import com.docker.manager.dto.HostDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mapper.MyMapper;

import java.util.List;

@Repository
public interface BaseHostMapper extends MyMapper<HostDTO> {

    List<HostDTO> getHostList(@Param("userid") Integer user_id);

    int insertHost(List<HostDTO> list);

}