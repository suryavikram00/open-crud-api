package com.api.open.crud.api.feignclient.user_micro;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.api.open.crud.api.entity.user_micro.UserEntityModel;

@FeignClient(name = "userClient", url = "${user.client.url}")
public interface UserFeignClient {

  @GetMapping("/user/{id}")
  UserEntityModel getData(@PathVariable("id") int id);
  
}