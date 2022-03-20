package ClinicalKitQR;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class FrontEndPageRedirector implements ErrorPageRegistrar {
    @Override
    public void registerErrorPages(ErrorPageRegistry errorPageRegistry){
        ErrorPage e404 = new ErrorPage(HttpStatus.NOT_FOUND,"/index.html");
        errorPageRegistry.addErrorPages(e404);
    }
}
