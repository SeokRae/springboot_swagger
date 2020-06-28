package com.user.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() { // Swagger 설정의 핵심이 되는 Bean

        ArrayList<ResponseMessage> responseMessageStatus = newArrayList(
                new ResponseMessageBuilder().code(200).message("OK").build(),
                new ResponseMessageBuilder().code(400).message("Invalid Request").build(),
                new ResponseMessageBuilder().code(401).message("No Permission").build(),
                new ResponseMessageBuilder().code(404).message("Not Found").build(),
                new ResponseMessageBuilder().code(500).message("Internal Server Error").build()
        );
        String version = "0.0.1";

        return new Docket(DocumentationType.SWAGGER_2)
                // 여러 Docker Bean을 생성하는 경우 구분하기 위한 group명 명시 (버전관리)
                .groupName(version)
                // 제목, 설명, 버전 등 문서에 대한 정보들을 보여주기 위해 호출
                .apiInfo(apiInfo("API Document Swagger", version))
                // ApiSelectorBuilder를 생성
                .select()
                // API spec이 작성되어 있는 패키지를 지정
                .apis(RequestHandlerSelectors.basePackage("com.user.swagger.controller"))
                // apis()로 선택되어진 API중 특정 path 조건에 맞는 API들을 다시 필터링하여 문서화
                .paths(PathSelectors.ant("/v1/**"))
                .build()
                // 기본으로 세팅되는 200,401,403,404 메시지를 표시 하지 않고 컨트롤러에서 명시
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, responseMessageStatus)
                .globalResponseMessage(RequestMethod.POST, responseMessageStatus)
                .globalResponseMessage(RequestMethod.PUT, responseMessageStatus)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageStatus);
    }

    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfoBuilder()
                .title(title)
                .description("Swagger User API Test") // 설명
                .termsOfServiceUrl("https://seokr.tistory.com/")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE")
                .version(version)
                .build();
    }
}