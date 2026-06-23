package com.scbank.process.api.fw.core.masking.impl;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.core.masking.IMaskingManager;
import com.scbank.process.api.fw.core.masking.IMaskingStrategy;
import com.scbank.process.api.fw.core.masking.IMaskingStrategy.MaskingType;
import com.scbank.process.api.fw.core.masking.impl.strategy.AccountNumberMaskingStrategy;
import com.scbank.process.api.fw.core.masking.impl.strategy.AddressMaskingStrategy;
import com.scbank.process.api.fw.core.masking.impl.strategy.CardNumberMaskingStrategy;
import com.scbank.process.api.fw.core.masking.impl.strategy.DefaultMaskingStrategy;
import com.scbank.process.api.fw.core.masking.impl.strategy.EmailMaskingStrategy;
import com.scbank.process.api.fw.core.masking.impl.strategy.NameMaskingStrategy;
import com.scbank.process.api.fw.core.masking.impl.strategy.PhoneMaskingStrategy;
import com.scbank.process.api.fw.core.masking.impl.strategy.RegNoMaskingStrategy;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DefaultMaskingManager implements IMaskingManager {

    private final Map<MaskingType, IMaskingStrategy> registry = new EnumMap<>(MaskingType.class);

    @PostConstruct
    public void init() {

        log.info("# 프레임워크 필드 마스킹 전략 초기화 처리...");

        registry.put(MaskingType.REGNO, new RegNoMaskingStrategy()); // 01
        registry.put(MaskingType.ACCOUNT, new AccountNumberMaskingStrategy()); // 02
        registry.put(MaskingType.NAME, new NameMaskingStrategy()); // 03
        registry.put(MaskingType.PHONE, new PhoneMaskingStrategy()); // 04
        registry.put(MaskingType.ADDRES, new AddressMaskingStrategy()); // 05
        registry.put(MaskingType.EMAIL, new EmailMaskingStrategy()); // 06
        registry.put(MaskingType.CARD, new CardNumberMaskingStrategy()); // 07
        registry.put(MaskingType.DEFAULT, new DefaultMaskingStrategy());

        registry.entrySet().forEach((m) -> {
            log.info("# {}: {}", m.getKey(), m.getValue().getClass().getName());
        });
    }

    @Override
    public byte[] apply(MaskingType type, byte[] source, String charset) {
        IMaskingStrategy maskingStrategy = registry.getOrDefault(type, new DefaultMaskingStrategy());
        return maskingStrategy.mask(source, charset);
    }

    @Override
    public String apply(MaskingType type, String source, String charset) {
        try {
            return new String(this.apply(type, source.getBytes(charset), charset));
        } catch (Exception e) {
            return source;
        }
    }
}
