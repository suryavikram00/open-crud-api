package com.api.open.crud.api.feignclient.account_micro;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.api.open.crud.api.entity.account_micro.AccountEntityModel;
import com.api.open.crud.api.exception.model.CrudApiResponse;

import lombok.Data;

@FeignClient(name = "accountClient", url = "${account.client.url}")
public interface  AccountFeignClient {

  @GetMapping("/account/{id}")
  ResponseEntity<CrudApiResponse<AccountEntityModel>> getData(@PathVariable("id") int id);
  
}

