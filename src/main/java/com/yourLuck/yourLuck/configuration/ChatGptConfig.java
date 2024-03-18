//package com.yourLuck.yourLuck.configuration;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@Getter
//@Setter
//public class ChatGptConfig {
//
//    public static final String AUTHORIZATION = "Authorization";
//    public static final String BEARER = "Bearer ";
//    public static final String API_KEY = "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";
////    public static final String MODEL = "gpt-3.5-turbo-instruct";
//public static final String MODEL = "gpt-3.5-turbo";
////2024-03-15 16:46:24.451 ERROR 87327 --- [nio-8081-exec-2] c.y.y.exception.GlobalControllerAdvice   : Error occurs org.springframework.web.client.HttpClientErrorException$TooManyRequests: 429 Too Many Requests: "{<EOL>    "error": {<EOL>        "message": "You exceeded your current quota, please check your plan and billing details. For more information on this error, read the docs: https://platform.openai.com/docs/guides/error-codes/api-errors.",<EOL>        "type": "insufficient_quota",<EOL>        "param": null,<EOL>        "code": "insufficient_quota"<EOL>    }<EOL>}<EOL>"
////            2024-03-15 16:46:24.467  WARN 87327 --- [nio-8081-exec-2] .m.m.a.ExceptionHandlerExceptionResolver : Resolved [org.springframework.web.client.HttpClientErrorException$TooManyRequests: 429 Too Many Requests: "{<EOL>    "error": {<EOL>        "message": "You exceeded your current quota, please check your plan and billing details. For more information on this error, read the docs: https://platform.openai.com/docs/guides/error-codes/api-errors.",<EOL>        "type": "insufficient_quota",<EOL>        "param": null,<EOL>        "code": "insufficient_quota"<EOL>    }<EOL>}<EOL>"]
//
//
//    public static final Integer MAX_TOKEN = 300;
//    public static final Double TEMPERATURE = 0.0;
//    public static final Double TOP_P = 1.0;
//    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
//    public static final String URL = "https://api.openai.com/v1/completions";
//}
