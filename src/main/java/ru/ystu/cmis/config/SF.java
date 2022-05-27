package ru.ystu.cmis.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import ru.ystu.cmis.domain.User;

public class SF {
    private SF(){}
    private static final SessionFactory sf;
    static {
        Configuration cfg = new Configuration()
                .addAnnotatedClass(User.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(cfg.getProperties());

        sf = cfg.buildSessionFactory(builder.build());
    }

    public static SessionFactory getInstance() {
        return sf;
    }
}