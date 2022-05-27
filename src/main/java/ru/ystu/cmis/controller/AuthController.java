package ru.ystu.cmis.controller;

import com.sun.jersey.api.view.Viewable;
import ru.ystu.cmis.dto.Model;
import ru.ystu.cmis.repository.CatalogRepository;
import ru.ystu.cmis.repository.UserRepository;
import ru.ystu.cmis.service.AuthService;
import ru.ystu.cmis.util.Self;
import ru.ystu.cmis.util.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

@Path("/")
public class AuthController {
    private final UserRepository userRepository = ServiceFactory.get(UserRepository.class);
    private final CatalogRepository catalogRepository = ServiceFactory.get(CatalogRepository.class);
    private final AuthService authService = ServiceFactory.get(AuthService.class);
    @Context
    ThreadLocal<HttpServletRequest> requestInvoker;
    @Context
    ThreadLocal<HttpServletResponse> responseInvoker;

    @Path("/userIn")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable index() {
        //userRepository.create("admin", "admin", "admin");
        return Self.view("auth", new Model());
    }

    @GET
    @Path("/registration")
    @Produces(MediaType.TEXT_HTML)
    public Viewable registration() {
        return Self.view("registration", new Model());
    }

    @POST
    @Path("/registration")
    @Produces(MediaType.TEXT_HTML)
    public Viewable registrationIn(MultivaluedMap<String, String> map){
        authService.setParams(requestInvoker, responseInvoker).createUser(map);
        authService.redirect("/shop");
        return null;
    }

    @GET
    @Path("/out")
    public void exitPage(){
        authService.setParams(requestInvoker, responseInvoker).exit();
        authService.redirect("/userIn");
    }

    @Path("/login")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.TEXT_HTML)
    public void login(MultivaluedMap<String, String> map){
        authService.setParams(requestInvoker, responseInvoker);
        String login = map.getFirst("username");
        String password = map.getFirst("password");
        Boolean isAuth = authService.auth(login, password);
        if(isAuth) {
            if (authService.isAdmin()) {
                authService.redirect("/admin");
                return;
            }
            else{
                authService.redirect("/shop");
                return;
            }
        }
        else{
            authService.redirect("/userIn");
            return;
        }
    }
}
