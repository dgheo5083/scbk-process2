package com.scbank.process.api.fw.common.property.impl;

import com.scbank.process.api.fw.common.property.IPropertyInfo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DefaultPropertyInfo implements IPropertyInfo {

	private static final long serialVersionUID = 1L;

	private String propertyName;

	private String propertyValue;
}
