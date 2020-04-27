package com.docker.manager.pojo;

import javax.persistence.*;

@Table(name = "containerlist")
public class BaseContain {

    /**
     * 容器ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 容器名
     */
    @Column(name = "containnames")
    private String containnames;

    /**
     * 容器状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 产品
     */
    @Column(name = "image")
    private String image;

    /**
     * 端口
     */
    @Column(name = "ports")
    private String ports;

    /**
     * ip
     */
    @Column(name = "ip")
    private String ip;



}