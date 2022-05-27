package ru.ystu.cmis.service.impl;

import lombok.SneakyThrows;
import ru.ystu.cmis.domain.Basket;
import ru.ystu.cmis.domain.User;
import ru.ystu.cmis.repository.UserRepository;
import ru.ystu.cmis.service.AuthService;
import ru.ystu.cmis.util.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;

public class AuthServiceImpl implements AuthService {
    @Context
    ThreadLocal<HttpServletRequest> requestInvoker;
    @Context
    ThreadLocal<HttpServletResponse> responseInvoker;

    private final UserRepository userRepository = ServiceFactory.get(UserRepository.class);

    @Override
    public Boolean auth(String login, String password) {
        User user = userRepository.auth(login, password);
        if(user != null) {
            HttpSession session = requestInvoker.get().getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("isAdmin", user.getRole().equals("admin"));
        }

        return user != null;
    }

    @Override
    public AuthService setParams(ThreadLocal<HttpServletRequest> request, ThreadLocal<HttpServletResponse> response) {
        requestInvoker = request;
        responseInvoker = response;
        System.out.println("setParams");
        return this;
    }

    @Override
    public Boolean isAdmin() {
        HttpSession session = requestInvoker.get().getSession(true);
        Object isAdmin = session.getAttribute("isAdmin");
        if (isAdmin == null || !(boolean) isAdmin)
            return false;
        return true;
    }

    @SneakyThrows
    @Override
    public void redirect(String url) {
        responseInvoker.get().sendRedirect(requestInvoker.get().getContextPath() + url);
    }

    @Override
    public void accessRedirect() {
        if (!isAdmin()) {
            redirect("/");
        }
    }

    @Override
    public void createUser(MultivaluedMap<String, String> map) {
        String login = map.getFirst("username");
        String password = map.getFirst("password");
        HttpSession session = requestInvoker.get().getSession(true);
        userRepository.create(login, password, "user");
        auth(login, password);
    }

    @Override
    public void exit() {
        HttpSession session = requestInvoker.get().getSession(false);
        if (session != null) session.invalidate();
    }

    @Override
    public void addToBasket(Integer id) {
        HttpSession session = requestInvoker.get().getSession(true);
        Object basketObject = session.getAttribute("basket");
        List<Basket> baskets = new ArrayList<>();
        if (basketObject != null) {
            baskets = (ArrayList<Basket>) basketObject;
        }
        if(baskets.size() > 0) {
            for (Basket item : baskets) {
                if (item.getId().equals(id)) {
                    item.setCount(item.getCount() + 1);
                }
            }
            if(!BasketAny(id, baskets)){
                Basket basket = new Basket();
                basket.setId(id);
                basket.setCount(1);
                baskets.add(basket);
            }
        }
        else{
            Basket basket = new Basket();
            basket.setId(id);
            basket.setCount(1);
            baskets.add(basket);
        }

        session.setAttribute("basket", baskets);
    }

    private Boolean BasketAny(Integer id, List<Basket> baskets){
        Boolean any = false;
        if(baskets != null){
            for (Basket basket : baskets) {
                if(basket.getId().equals(id)){
                    any = true;
                }
            }
        }
        return any;
    }

    public Integer GetTotalPrice(List<Basket> baskets){
        Integer totalPrice = 0;

        if(baskets != null){
            for (Basket basket : baskets) {
                totalPrice += basket.getSneakers().getPrice() * basket.getCount();
            }
        }

        return totalPrice;
    }
}
