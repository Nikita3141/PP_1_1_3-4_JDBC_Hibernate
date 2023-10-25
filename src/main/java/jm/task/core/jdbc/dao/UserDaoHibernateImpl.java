package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }
    private final SessionFactory sessionFactory = Util.getConnections();
    private Transaction transaction = null;

    @Override
    public void createUsersTable() {

        try (Session session = sessionFactory.openSession()) {
            String sql = "CREATE TABLE IF NOT EXISTS `mydbtest` .`users` (`id` INT NOT NULL AUTO_INCREMENT,`name` VARCHAR(45) NULL,`lastname` VARCHAR(45) NULL,`age` INT(3) NULL, PRIMARY KEY (`id`))";

            transaction = session.beginTransaction();

            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
            System.out.println("Таблица создана");
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void dropUsersTable() {

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String sql = "DROP TABLE IF EXISTS users";
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
            System.out.println("Таблица дропнута");

        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }

        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.save(new User(name,lastName,age));
            transaction.commit();
            System.out.println("User с именем "+ name+ " добавлен в базу");

        }catch (HibernateException e){
            e.printStackTrace();
            if(transaction != null){
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.delete(session.get(User.class,id));
            transaction.commit();
            System.out.println("User c id = "+id+" удалён");
        }catch (HibernateException e){
            e.printStackTrace();
            if(transaction != null){
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<User> criteriaQuery = session.getCriteriaBuilder().createQuery(User.class);
            criteriaQuery.from(User.class);
            transaction = session.beginTransaction();
            userList = session.createQuery(criteriaQuery).getResultList();
            transaction.commit();
            return userList;
        } catch (HibernateException e) {
            e.printStackTrace();
            transaction.rollback();

        }
        return userList;
    }

        @Override
        public void cleanUsersTable() {
            Session session = sessionFactory.openSession();
            try {
                transaction = session.beginTransaction();
                final List userList = session.createCriteria(User.class).list();

                for (Object o : userList) {
                    session.delete(o);
                }

                session.getTransaction().commit();
                System.out.println("Таблица очищена");
            } catch (Exception e) {
                e.printStackTrace();
                if (transaction != null) {
                    transaction.rollback();
                }
            } finally {
                if (session != null)
                    session.close();
            }
        }


}


