package ru.ystu.cmis.service;

import ru.ystu.cmis.domain.Basket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public interface AuthService {
    Boolean auth(String login, String password);
    AuthService setParams(ThreadLocal<HttpServletRequest> request, ThreadLocal<HttpServletResponse> response);
    Boolean isAdmin();
    void accessRedirect();
    void redirect(String url);
    void createUser(MultivaluedMap<String, String> map);
    void exit();
    void addToBasket(Integer id);
    public Integer GetTotalPrice(List<Basket> baskets);
}
