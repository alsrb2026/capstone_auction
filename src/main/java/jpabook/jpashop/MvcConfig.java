package jpabook.jpashop;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    final Path FILE_ROOT = Paths.get("./photo").toAbsolutePath().normalize();

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/postimages/**") //images로 접근하는 모든 요청을
                .addResourceLocations(FILE_ROOT.toUri().toString()); //외부폴더인 photo폴더로 돌림 -> 경로 숨김 + 파일 실시간 접근 가능해짐
    }
}