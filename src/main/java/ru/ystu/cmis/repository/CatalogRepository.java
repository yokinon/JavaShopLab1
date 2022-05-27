package ru.ystu.cmis.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.ystu.cmis.domain.Sneakers;
import ru.ystu.cmis.util.DbUtil;

import java.util.List;

public class CatalogRepository {
    public Sneakers getSneakersById(Integer id){
        Sneakers sneakers = null;
        try (Session session = DbUtil.getSessionFactory().openSession()) {
            sneakers = session.byId(Sneakers.class).load(id);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sneakers;
    }

    public void createSneakers(String name, Integer price, Integer count){
        Transaction transaction = null;
        try (Session session = DbUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(new Sneakers(name, price, count));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateSneakers(Integer id, String name, Integer price, Integer count){
        Transaction transaction = null;
        try (Session session = DbUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Sneakers sneakers = session.byId(Sneakers.class).load(id);
            sneakers.setName(name);
            sneakers.setPrice(price);
            sneakers.setCount(count);
            session.update(sneakers);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Sneakers> getSneakersList(){
        try (Session session = DbUtil.getSessionFactory().openSession()) {
            List<Sneakers> list = session.createQuery("SELECT G FROM Sneakers G", Sneakers.class).list();
            if (list != null)
                return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteSneakers(Integer id){
        Transaction transaction = null;
        try (Session session = DbUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Sneakers sneakers = session.byId(Sneakers.class).load(id);
            session.delete(sneakers);
            session.flush();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
