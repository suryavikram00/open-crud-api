package com.api.open.crud.api.feignclient.account_micro;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.api.open.crud.api.entity.account_micro.AccountEntityModel;

@FeignClient(name = "accountClient", url = "${account.client.url}")
public interface  AccountFeignClient {

  @GetMapping("/account/{id}")
  AccountEntityModel getData(@PathVariable("id") int id);
  
}
