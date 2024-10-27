package com.api.open.crud.api.feignclient.user_micro;

import org.springframework.cloud.openfeign.FeignClient;
import com.api.open.crud.api.entity.user_micro.UserEntityModel;
import com.api.open.crud.api.feignclient.OpenFeignClient;

@FeignClient(name = "userClient", url = "${user.client.url}/user")
public interface UserFeignClient extends OpenFeignClient<UserEntityModel> {

  // @GetMapping("/user/{id}")
  // ResponseEntity<CrudApiResponse<UserEntityModel>>  getData(@PathVariable("id") int id);
  
}