package ru.ystu.cmis.controller;

import com.sun.jersey.api.view.Viewable;
import ru.ystu.cmis.domain.Basket;
import ru.ystu.cmis.domain.Sneakers;
import ru.ystu.cmis.dto.Model;
import ru.ystu.cmis.repository.CatalogRepository;
import ru.ystu.cmis.service.AuthService;
import ru.ystu.cmis.util.Self;
import ru.ystu.cmis.util.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/shop")
public class ShopController {
    private final CatalogRepository catalogRepository = ServiceFactory.get(CatalogRepository.class);
    private final AuthService authService = ServiceFactory.get(AuthService.class);
    @Context
    ThreadLocal<HttpServletRequest> requestInvoker;
    @Context
    ThreadLocal<HttpServletResponse> responseInvoker;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable index() {
        List<Sneakers> sneakersList = catalogRepository.getSneakersList();
        Model model = new Model();
        model.put("sneakersList", sneakersList);
        return Self.view("catalog", model);
    }

    @Path("/basket")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable basket() {
        authService.setParams(requestInvoker, responseInvoker);
        HttpSession session = requestInvoker.get().getSession(true);
        List<Basket> basketItems = (ArrayList<Basket>)session.getAttribute("basket");
        if(basketItems == null){
            basketItems = new ArrayList<>();
        }

        for (Basket item : basketItems) {
            Sneakers sneakers = catalogRepository.getSneakersById(item.getId());
            item.setSneakers(sneakers);
        }
        Integer totalPrice = authService.GetTotalPrice(basketItems);

        Model model = new Model();
        model.put("basketItemList", basketItems);
        model.put("totalPrice", totalPrice);
        return Self.view("basket", model);
    }

    @GET
    @Path("/addToBasket/{id}")
    @Produces(MediaType.TEXT_HTML)
    public void addToBasket(@PathParam("id") Integer id){
        authService.setParams(requestInvoker, responseInvoker);
        authService.addToBasket(id);
    }

    @GET
    @Path("/clear")
    @Produces(MediaType.TEXT_HTML)
    public void clearbasket(){
        authService.setParams(requestInvoker, responseInvoker);
        HttpSession session = requestInvoker.get().getSession(true);
        session.removeAttribute("basket");
        authService.redirect("/shop/basket");
    }

    @GET
    @Path("/buy")
    @Produces(MediaType.TEXT_HTML)
    public void buy(){
        authService.setParams(requestInvoker, responseInvoker);
        HttpSession session = requestInvoker.get().getSession(true);
        List<Basket> basketItems = (ArrayList<Basket>)session.getAttribute("basket");
        for (Basket basket : basketItems) {
            Sneakers sneakers = catalogRepository.getSneakersById(basket.getId());
            if(sneakers.getCount() >= basket.getCount()) {
                sneakers.setCount(sneakers.getCount() - basket.getCount());
                catalogRepository.updateSneakers(sneakers.getId(), sneakers.getName(), sneakers.getPrice(), sneakers.getCount());
            }
        }
        clearbasket();
    }
}
