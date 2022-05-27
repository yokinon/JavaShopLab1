package ru.ystu.cmis.config;

import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.spi.template.ViewProcessor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;
import ru.ystu.cmis.util.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Provider
public class ThymeleafViewProcessor implements ViewProcessor<String> {
    ServiceFactory serviceFactory = ServiceFactory.getInstance();
    @Context
    ServletContext servletContext;

    @Context
    ThreadLocal<HttpServletRequest> requestInvoker;

    @Context
    ThreadLocal<HttpServletResponse> responseInvoker;

    TemplateEngine templateEngine;

    public ThymeleafViewProcessor() {
        TemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/tpl/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("LEGACYHTML5");
        templateResolver.setCacheTTLMs(3600000L);

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    @Override
    public String resolve(String s) {
        return s;
    }

    @Override
    public void writeTo(String map, Viewable viewable, OutputStream outputStream) throws IOException {
        outputStream.flush();
        WebContext context = new WebContext(requestInvoker.get(),
                responseInvoker.get(), servletContext, requestInvoker.get().getLocale());
        context.setVariables((Map<String, Object>)viewable.getModel());
        templateEngine.process(viewable.getTemplateName(), context, responseInvoker
                .get().getWriter());
    }
}
