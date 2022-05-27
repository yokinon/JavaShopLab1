package ru.ystu.cmis.util;

import ru.ystu.cmis.repository.CatalogRepository;
import ru.ystu.cmis.repository.UserRepository;
import ru.ystu.cmis.service.AuthService;
import ru.ystu.cmis.service.impl.AuthServiceImpl;

import java.util.HashMap;
import java.util.Map;

public class ServiceFactory {
    private static final Map<Class<?>,Object> services = new HashMap<>(5);
    private static final ServiceFactory factory;
    static {
        services.put(UserRepository.class,new UserRepository());
        services.put(CatalogRepository.class, new CatalogRepository());
        services.put(AuthService.class,new AuthServiceImpl());
        factory = new ServiceFactory();
        System.out.println("init ServiceFactory");
    }
    public static ServiceFactory getInstance(){
        return factory;
    }
    public static <T> T get(Class<T> type){return (T) services.get(type);}

}
