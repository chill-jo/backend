package com.example.surveyapp.global.advice;

import com.example.surveyapp.global.response.ApiResponse;
import com.example.surveyapp.global.response.exception.ErrorResponseDto;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

@ControllerAdvice
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response
    ){
//        if(response instanceof ServletServerHttpResponse servletResponse){
//            int status = servletResponse.getServletResponse().getStatus();
//            if(status >= 400){
//                return body;
//            }
//        }

        //spring 기본 에러 응답
        if(body instanceof Map map){
            if (map.containsKey("status") && map.containsKey("error")) {
                return body;
            }
        }

        //exception handler에서 반환한 ErrorResponseDto (커스텀에러)
        if(body instanceof ErrorResponseDto){
            return ApiResponse.fail(((ErrorResponseDto) body).getMessage(), body);
        }

        //이미 ApiResponse로 감싸서 반환하고 있을 때
        if(body instanceof ApiResponse<?>){
            return body;
        }

        return new ApiResponse<>(true, "성공", body);
    }


}
