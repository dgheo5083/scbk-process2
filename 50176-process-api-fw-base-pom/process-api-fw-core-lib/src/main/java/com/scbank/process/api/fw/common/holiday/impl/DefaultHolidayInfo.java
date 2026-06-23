package com.scbank.process.api.fw.common.holiday.impl;

import com.scbank.process.api.fw.common.holiday.IHolidayInfo;

import lombok.Builder;
import lombok.Data;

/**
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 17.
 */
@Data
@Builder
public class DefaultHolidayInfo implements IHolidayInfo {

    private static final long serialVersionUID = 1L;

    private String date;

    private String description;
}
