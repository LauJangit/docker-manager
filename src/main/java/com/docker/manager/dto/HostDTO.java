package com.docker.manager.dto;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
public class HostDTO {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String  ip;
  private String hostname;
  public String product;
  public String application;
  public String roomno;
  public Integer userid;
  public String status;
  public String memo;

}
