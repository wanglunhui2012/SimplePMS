package indi.simple.pms.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 21:30
 * @Description:
 */
@Configuration
@ConditionalOnProperty(value = "server.useSSL", havingValue = "true")
public class TomcatHttpsConfig {
    @Value("${server.useSSL}")
    private boolean useSSL;
    @Value("${server.httpPort}")
    private int httpPort;
    @Value("${server.httpsPort}")
    private int httpsPort;

    public TomcatHttpsConfig() {
    }

    @Bean
    public Connector connector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(this.httpPort);
        connector.setSecure(false);
        connector.setRedirectPort(this.httpsPort);
        return connector;
    }

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(@Nullable Connector connector, @Nullable TomcatHttpsConfig.MySSL mySSL) {
        TomcatServletWebServerFactory tomcat;
        if (this.useSSL) {
            tomcat = new TomcatServletWebServerFactory(this.httpsPort) {
                protected void postProcessContext(Context context) {
                    SecurityConstraint securityConstraint = new SecurityConstraint();
                    securityConstraint.setUserConstraint("CONFIDENTIAL");
                    SecurityCollection collection = new SecurityCollection();
                    collection.addPattern("/*");
                    securityConstraint.addCollection(collection);
                    context.addConstraint(securityConstraint);
                }
            };
            tomcat.addAdditionalTomcatConnectors(new Connector[]{connector});
            tomcat.setSsl(mySSL);
            return tomcat;
        } else {
            tomcat = new TomcatServletWebServerFactory(this.httpPort);
            tomcat.setSsl((Ssl)null);
            return tomcat;
        }
    }

    @Configuration
    @ConfigurationProperties("server.my-ssl")
    class MySSL extends Ssl {
        MySSL() {
        }
    }
}

