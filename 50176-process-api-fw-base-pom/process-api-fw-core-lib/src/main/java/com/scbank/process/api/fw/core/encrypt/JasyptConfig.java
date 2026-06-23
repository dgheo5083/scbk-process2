package com.scbank.process.api.fw.core.encrypt;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.salt.ZeroSaltGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@Configuration
@EnableEncryptableProperties
public class JasyptConfig {
	
	@Value("${jasypt.encryptor.password}")
	private String scretKey;
	
	@Value("${jasypt.encryptor.algorithm}")
	private String algorithm;

	@Bean("jasyptEncryptor")
	PooledPBEStringEncryptor jasyptEncryptor() {
		PooledPBEStringEncryptor enc = new PooledPBEStringEncryptor();
		
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setAlgorithm(algorithm);
		config.setKeyObtentionIterations("1000");
		config.setPassword(scretKey);
		config.setSaltGenerator(new ZeroSaltGenerator());
		config.setStringOutputType("base64");
		config.setPoolSize(1);
		config.setProviderName("SunJCE");
		
		enc.setConfig(config);
		
		return enc;
	}
}
