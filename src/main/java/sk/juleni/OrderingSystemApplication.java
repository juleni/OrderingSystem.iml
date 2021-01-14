package sk.juleni;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sk.juleni.model.Order;
import sk.juleni.model.Product;
import sk.juleni.model.User;
import sk.juleni.repository.OrderRepository;
import sk.juleni.repository.ProductRepository;
import sk.juleni.repository.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DEMO application for managing of users, products and orders
 *
 * @author Julian Legeny
 */
@SpringBootApplication
public class OrderingSystemApplication implements CommandLineRunner {

    @Autowired
    private UserRepository urepo;
    @Autowired
    private ProductRepository prepo;
    @Autowired
    private OrderRepository orepo;

    public static void main(String[] args) {
        SpringApplication.run(OrderingSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // create few sample data...

        // I. create user objects
        User defaultUser = new User(User.DEFAULT_LOGIN,User.DEFAULT_PASS,"test@testapp.org");
        User testUser1 = new User(User.DEFAULT_LOGIN + "1",User.DEFAULT_PASS,"email1@testapp.org");
        User testUser2 = new User(User.DEFAULT_LOGIN + "2",User.DEFAULT_PASS,"email1@testapp.org");

        // retrieve default user from the list - has to be only one in the list
        //User testUser =  urepo.findByLogin(User.DEFAULT_LOGIN).get(0);

        // II. create product objects
        Product productX1 = new Product("pKod-X1", "Produkt nazov X1", "Popis X1", BigDecimal.valueOf(10));
        productX1.setBaseProduct(productX1);
        Product productX2 = new Product("pKod-X2", "Produkt nazov X2", "Popis X2", BigDecimal.valueOf(20));
        productX2.setBaseProduct(productX2);
        Product productX3 = new Product("pKod-X3", "Produkt nazov X3", "Popis X3", BigDecimal.valueOf(30));
        productX3.setBaseProduct(productX3);

        Product productY1 = new Product("Kod-Y1", "Produkt Y1", "Popis Y1", BigDecimal.valueOf(40));
        productY1.setBaseProduct(productY1);
        Product productY2 = new Product("Kod-Y2", "Produkt Y2", "Popis Y2", BigDecimal.valueOf(50));
        productY2.setBaseProduct(productY2);
        Product productY3 = new Product("Kod-Y3", "Produkt Y3", "Popis Y3", BigDecimal.valueOf(60));
        productY3.setBaseProduct(productY3);

        Product productZ1 = new Product("AAA", "Produkt AAA", "Popis AAA", BigDecimal.valueOf(100));
        Product productZ2 = new Product("BBB", "Produkt BBB", "Popis BBB", BigDecimal.valueOf(200));
        Product productZ3 = new Product("CCC", "Produkt CCC", "Popis CCC", BigDecimal.valueOf(300));

        // III. create order objects
        Order testOrderA1 = new Order("Objednavka A1", 3.00, "Popis A1");
        Order testOrderA2 = new Order("Objednavka A2", 6.00, "Popis A2");
        Order testOrderB = new Order("Objednavka B", 9.00, "Popis B");
        Order testOrderC = new Order("Objednavka C", 10.00, "Popis C");

        // add 6 'common' base products to the defaultUser
        defaultUser.addProduct(productX1);
        defaultUser.addProduct(productX2);
        defaultUser.addProduct(productX3);
        defaultUser.addProduct(productY1);
        defaultUser.addProduct(productY2);
        defaultUser.addProduct(productY3);
        defaultUser = urepo.save(defaultUser);

        // add 3 products to the order A1 (user_id have to be null if the product belongs to the order)
        Product tempProduct = prepo.findOneById(productX1.getProduct_id());
        Product orderProductX1  = new Product(tempProduct);
        orderProductX1.setUserProduct(null);
        testOrderA1.addProduct(orderProductX1);

        tempProduct = prepo.findOneById(productX2.getProduct_id());
        Product orderProductX2  = new Product(tempProduct);
        orderProductX2.setUserProduct(null);
        testOrderA1.addProduct(orderProductX2);

        tempProduct = prepo.findOneById(productX3.getProduct_id());
        Product orderProductX3  = new Product(tempProduct);
        orderProductX3.setUserProduct(null);
        testOrderA1.addProduct(orderProductX3);

        testOrderA1.setUserOrder(defaultUser);
        orepo.save(testOrderA1);

        // add 3 'common' base products to the defaultUser
        testUser1.addProduct(productZ1);
        testUser1.addProduct(productZ2);
        testUser1.addProduct(productZ3);
        testUser1 = urepo.save(testUser1);

        tempProduct = prepo.findOneById(productZ2.getProduct_id());
        Product orderProductZ2  = new Product(tempProduct);
        orderProductZ2.setUserProduct(null);
        testOrderA2.addProduct(orderProductZ2);

        testOrderA2.setUserOrder(testUser1);
        orepo.save(testOrderA2);

        productX2.setProduct_id(Product.NEW_ID);
        testOrderA1.addProduct(productX2);
        productX3.setProduct_id(Product.NEW_ID);
        testOrderA1.addProduct(productX3);
        // add order A1 to the default user
        defaultUser.addOrder(testOrderA1);

        productY1.setProduct_id(Product.NEW_ID);
        testOrderA2.addProduct(productY1);
        productY2.setProduct_id(Product.NEW_ID);
        testOrderA2.addProduct(productY2);
        productY3.setProduct_id(Product.NEW_ID);
        testOrderA2.addProduct(productY3);
        // add order A2 to the default user
        defaultUser.addOrder(testOrderA2);

        // save user with belonged orders and their belonged products
//        urepo.save(defaultUser);
/*
        // add 3 'common' products to the testUser1
        testUser1.addProduct(productZ1);
        testUser1.addProduct(productZ2);
        testUser1.addProduct(productZ3);
        urepo.save(testUser1);

        // add 3 another  products to the order A2
        testUser1.removeProduct(productZ1);
        testUser1.removeProduct(productZ2);
        testUser1.removeProduct(productZ3);

        productZ1.setProduct_id(null);
        testOrderB.addProduct(productZ1);
        productZ2.setProduct_id(null);
        testOrderB.addProduct(productZ2);
        productZ3.setProduct_id(null);
        testOrderB.addProduct(productZ3);
        // add order A3 to the test user 1
        testUser1.addOrder(testOrderB);
        // save belonged order and its belonged products to testUser1
        urepo.save(testUser1);

        /*
        // add 3 another products to the order A2
        productX1.setProduct_id(null);
        testOrderC.addProduct(productX1);
        productY1.setProduct_id(null);
        testOrderC.addProduct(productY1);
        productZ1.setProduct_id(null);
        testOrderC.addProduct(productZ1);
        testUser2.addOrder(testOrderC);
        // save belonged order and its belonged products to the default user
        urepo.save(testUser2);
        */

    }
}
