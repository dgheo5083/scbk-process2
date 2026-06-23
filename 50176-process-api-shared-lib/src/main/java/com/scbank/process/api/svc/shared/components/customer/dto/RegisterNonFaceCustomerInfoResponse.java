package com.scbank.process.api.svc.shared.components.customer.dto;

import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;

import lombok.Data;

@Data
@IntegrationMessage(id = "RegisterNonFaceCustomerInfoResponse", type = Type.RESPONSE)
public class RegisterNonFaceCustomerInfoResponse {

}
