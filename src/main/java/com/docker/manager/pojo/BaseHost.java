package com.docker.manager.pojo;

import javax.persistence.*;

@Table(name = "host")
public class BaseHost {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * IP地址
     */
    @Column(name = "ip")
    private String ip;

    /**
     * 主机名
     */
    @Column(name = "hostname")
    private String hostname;

    /**
     * 产品
     */
    @Column(name = "product")
    private String product;

    /**
     * 应用
     */
    @Column(name = "application")
    private String application;

    /**
     * 房间号
     */
    @Column(name = "roomno")
    private String roomno;

    /**
     * 用户ID
     */
    @Column(name = "userid")
    private Integer userid;

    /**
     * 状态（0：无效；1：有效）
     */
    @Column(name = "status")
    private String status;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;


    @Override
    public String toString() {
        return "BaseAdminUser{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", hostname='" + hostname + '\'' +
                ", product=" + product +
                ", application='" + application + '\'' +
                ", roomno='" + roomno + '\'' +
                ", userid=" + userid + '\'' +
                ", status='" + status + '\'' +
                ", memo='" + memo +
                '}';
    }
}