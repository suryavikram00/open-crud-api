package com.api.open.crud.api.feignclient.account_micro;

import org.springframework.cloud.openfeign.FeignClient;
import com.api.open.crud.api.entity.account_micro.AccountEntityModel;
import com.api.open.crud.api.feignclient.OpenFeignClient;

@FeignClient(name = "accountClient", url = "${account.client.url}/account")
public interface AccountFeignClient extends OpenFeignClient<AccountEntityModel> {

  // @GetMapping("/account/{id}")
  // ResponseEntity<CrudApiResponse<AccountEntityModel>> getData(@PathVariable("id") int id);
  
}

