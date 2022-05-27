package ru.ystu.cmis.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.ystu.cmis.domain.User;
import ru.ystu.cmis.util.DbUtil;

public class UserRepository {
    public Boolean create(String login, String password, String role){
        Transaction transaction = null;
        try (Session session = DbUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(login, password, role));
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return false;
    }

    public User auth(String login, String password) {
        try (Session session = DbUtil.getSessionFactory().openSession()) {
            User user = session.createQuery("SELECT u FROM User u WHERE u.login = :login AND u.password = :password", User.class)
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .getSingleResult();
            if (user != null)
                return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
