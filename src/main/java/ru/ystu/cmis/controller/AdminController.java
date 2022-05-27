package ru.ystu.cmis.controller;

import com.sun.jersey.api.view.Viewable;
import ru.ystu.cmis.domain.Sneakers;
import ru.ystu.cmis.dto.Model;
import ru.ystu.cmis.repository.CatalogRepository;
import ru.ystu.cmis.service.AuthService;
import ru.ystu.cmis.util.Self;
import ru.ystu.cmis.util.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

@Path("/admin")
public class AdminController {
    private final CatalogRepository catalogRepository = ServiceFactory.get(CatalogRepository.class);
    private final AuthService authService = ServiceFactory.get(AuthService.class);
    @Context
    ThreadLocal<HttpServletRequest> requestInvoker;
    @Context
    ThreadLocal<HttpServletResponse> responseInvoker;
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable index() {
        Boolean isAdmin = authService.setParams(requestInvoker, responseInvoker).isAdmin();
        if(isAdmin) {
            List<Sneakers> sneakersList = catalogRepository.getSneakersList();
            Model model = new Model();
            model.put("sneakersList", sneakersList);
            return Self.view("sneakersList", model);
        }
        else {
            authService.redirect("/userIn");
            return null;
        }
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.TEXT_HTML)
    public Viewable add(MultivaluedMap<String, String> map){
        Integer id = Integer.parseInt(map.getFirst("id"));
        String name = map.getFirst("name");
        Integer price = Integer.parseInt(map.getFirst("price"));
        Integer count = Integer.parseInt(map.getFirst("count"));
        if(id != 0){
            catalogRepository.updateSneakers(id, name, price, count);
        }
        else {
            catalogRepository.createSneakers(name, price, count);
        }
        return index();
    }

    @GET
    @Path("/delete/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Viewable delete(@PathParam("id") Integer id) {
        catalogRepository.deleteSneakers(id);
        return index();
    }

    @GET
    @Path("/edit/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Viewable edit(@PathParam("id") Integer id) {
        Sneakers sneakers = catalogRepository.getSneakersById(id);
        Model model = new Model();
        model.put("sneakers", sneakers);
        return Self.view("editSneakers", model);
    }
}
