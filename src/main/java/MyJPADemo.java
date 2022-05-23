import org.hibernate.jpa.HibernatePersistenceProvider;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.*;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class MyJPADemo {

    private DataSource getDataSource() {
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUser("username");
        dataSource.setPassword("password");
        dataSource.setUrl("jdbc:sqlite:C:/sqlite/db/customer.db");
        return dataSource;
    }

    private Properties getProperties() {
        final Properties properties = new Properties();
        properties.put( "hibernate.dialect", "org.hibernate.dialect.sqlitedialect" );
        properties.put( "hibernate.connection.driver_class", "org.sqlite.Driver" );
        return properties;
    }

    private EntityManagerFactory entityManagerFactory(DataSource dataSource, Properties hibernateProperties ){
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(".");
        em.setJpaVendorAdapter( new HibernateJpaVendorAdapter() );
        em.setJpaProperties( hibernateProperties );
        em.setPersistenceUnitName( "demo-unit" );
        em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        em.afterPropertiesSet();
        return em.getObject();
    }

    public static void main(String[] args) {
        MyJPADemo jpaDemo = new MyJPADemo();
        DataSource dataSource = jpaDemo.getDataSource();
        Properties properties = jpaDemo.getProperties();
        EntityManagerFactory entityManagerFactory = jpaDemo.entityManagerFactory(dataSource, properties);
        EntityManager em = entityManagerFactory.createEntityManager();
        PersistenceUnitUtil unitUtil = entityManagerFactory.getPersistenceUnitUtil();
        insertToCustomer(em);
        getCustomerById(em);

    }
    private static void insertToCustomer(EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Customer c= new Customer();
        c.setName("Bob");
        c.setId("1");
        em.merge(c);
        tx.commit();
    }

    private static void insertToOrder(EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Order o= new Order();
        o.setId("1");
        o.setCustomerId("1");
        em.merge(o);
        tx.commit();
    }


    private static void getCustomerById(EntityManager em) {
        Query query = em.createQuery("select c from customer c where c.id = ?1");
        query.setParameter(1, "1");
        Customer c = (Customer)query.getSingleResult();
        System.out.println(c);
    }

    private static void getOrderById(EntityManager em) {
        Query query = em.createQuery("select o from order o where o.id = ?1");
        query.setParameter(1, "1");
        Order o = (Order)query.getSingleResult();
        System.out.println(o);
    }


}