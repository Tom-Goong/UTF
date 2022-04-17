package com.yaoting.utf.common.constant;

public class HTTPConstant {

    /**
     *  ===========  Header  ==================
     */
    public static final String ACCEPTED_SERVER = "Accepted-Server";
    public static final String TRACE_ID = "Trace-Id";
    public static final String SPAN_ID = "Span-Id";
    public static final String TIME_COST = "Time-Cost";
    public static final String APP_KEY = "App-Key";
    public static final String APP_SECRET = "App-Secret";
    public static final String APP_AGENT_FOR = "App-Agent-For";


    //  ===========  Walle Gateway  ==================
    public static final String ACCOUNT_TYPE = "Account-Type";

    public static final String ACCESS_TYPE = "Access-Type";      // 访问方式
    public static final String ACCESS_TYPE_GATEWAY = "Gateway";  // 通过gateway代理访问
    public static final String ACCESS_TYPE_DIRECT = "Direct";    // 直接访问

    public static final String ORIGIN_ID = "Origin-IP"; // 请求来源ip，网关解析后，透传
    public static final String ACCOUNT_VALIDATED = "Account-Validated";  // 验证成功的账号（代理访问才会有这个值）



    /**
     *  ===========  Param  ==================
     */
    public static final String APP_KEY_PARAM = "appKey";
    public static final String APP_SECRET_PARAM = "appSecret";


    /**
     *  ===========  Code  ==================
     */

    /**
     *  为了和其他项目统一，用了奇葩的状态码
     */
    public static final int CUSTOM_REDIRECT_CODE = 401;
}
