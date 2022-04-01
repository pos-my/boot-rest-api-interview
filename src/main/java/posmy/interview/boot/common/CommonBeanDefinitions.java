package posmy.interview.boot.common;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CommonBeanDefinitions implements InitializingBean {

    private ModelMapper mapper = new ModelMapper();

    @Bean
    public ModelMapper getMapper() {
        return this.mapper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub

    }

}
