package com.mekari.mokaaddons.webhookhandler.query.getUser;

import com.mekari.mokaaddons.webhookhandler.common.handler.RequestParam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Request implements RequestParam<User> {
    private int id;
}
