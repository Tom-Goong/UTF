package com.yaoting.utf.common.context;

import com.yaoting.utf.common.constant.EnvEnum;
import com.yaoting.utf.common.utils.SpringTool;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;


@Slf4j
@Getter
@ToString
@Component
public class AppContext implements PermanentContext {

    @Value("${app.name}")
    private String name;

    @Value("${app.env}")
    private EnvEnum env;

    @Value("${app.app-key}")
    private String appKey;

    @Value("${app.app-secret}")
    private String appSecret;

    private final ContextStatus status = ContextStatus.running;

    private final String ip = InetAddress.getLocalHost().getHostAddress();

    private final String hostname = InetAddress.getLocalHost().getHostName();

    private final Date startTime = new Date();

    public AppContext() throws UnknownHostException {
    }


    @Override
    public Context setStatus(ContextStatus contextStatus) {
        throw new IllegalStateException("AppContext can't change state");
    }

    @Override
    public void toFailed() {
        throw new IllegalStateException("AppContext can't change state");
    }

    @Override
    public void toCompleted() {
        throw new IllegalStateException("AppContext can't change state");
    }

    public static AppContext getInstance() {
        return SpringTool.getBean(AppContext.class);
    }

    public static boolean isPreEnv() {
        return isEnvOf(EnvEnum.PRE);
    }

    public static boolean isProEnv() {
        return isEnvOf(EnvEnum.PROD);
    }

    public static boolean isLocalEnv() {
        return isEnvOf(EnvEnum.LOCAL);
    }

    public static boolean isDevEnv() {
        return isEnvOf(EnvEnum.DEV);
    }

    public static boolean isEnvOf(EnvEnum env) {
        return getInstance().getEnv() == env;
    }
}
