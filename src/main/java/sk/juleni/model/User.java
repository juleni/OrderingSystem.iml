package sk.juleni.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User object represents users. User table is mapped as following:
 *
 *      [USER] --- 1:M ---  [PRODUCT] --- M:1 ---  [TORDER] --- M:1 --- [USER]
 *
 * @author Julian Legeny
 *
 */
@Entity
public class User extends DataObject {

    public static final String DEFAULT_LOGIN = "test";
    public static final String DEFAULT_PASS = "testpass";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @NotNull
    @Column(unique = true)
    private String user_login;
    @NotNull
    private String user_password;
    @NotNull
    private String user_email;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private final Date user_last_modified = new Date();

    // join column to the torder table
    @OneToMany(cascade = CascadeType.ALL,
               mappedBy="userOrder", orphanRemoval = true)
    private Set<Order> orders = new HashSet<>();

    // join column to the product table
    @OneToMany(cascade  = CascadeType.ALL,
            mappedBy="userProduct", orphanRemoval = true)
    private Set<Product> productsBase = new HashSet<>();

    public User() {
        // required by JPA but not used for me
    }

    /**
     * Constructor for creating the user
     *
     * @param user_id - Long - user ID, has to be unique or -1 if new object if created
     */
    public User(Long user_id) {
        this.user_id = user_id;
    }

    /**
     * Constructor for creating the user
     *
     * @param user_login - String - login name, has to be unique
     * @param user_password - String - user password
     * @param user_email - String - user email
     */
    public User(@NotNull String user_login, @NotNull String user_password, @NotNull String user_email) {
        this.user_login = user_login;
        this.user_password = user_password;
        this.user_email = user_email;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Set<Product> getProductsBase() {
        return productsBase;
    }

    public void setProductsBase(Set<Product> productsBase) {
        this.productsBase = productsBase;
    }

    /**
     * Add order to the list of orders
     *
     * @param order - Order object to be added to the list
     */
    public void addOrder(Order order) {
        orders.add(order);
        order.setUserOrder(this);
    }

    /**
     * Remove order from the list of orders
     *
     * @param order - Order object to be removed to the list
     */
    public void removeOrder(Order order) {
        order.setUserOrder(null);
        this.orders.remove(order);
    }

    /**
     * Add product to the list of products
     *
     * @param product - Product object to be added to the list
     */
    public void addProduct(Product product) {
        productsBase.add(product);
        product.setUserProduct(this);
    }

    /**
     * Remove product from the list of products
     *
     * @param product - Product object to be removed to the list
     */
    public void removeProduct(Product product) {
        product.setUserProduct(null);
        this.productsBase.remove(product);
    }

    @Override
    public boolean equals(Object object)
    {
        return object != null
                && object.getClass().equals(this.getClass())
                && ((User) object).getUser_id().equals(this.user_id);
    }

    @Override
    public int hashCode()
    {
        return this.user_login.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + user_id +
                ", userLogin='" + user_login + '\'' +
                ", userPassword='" + user_password + '\'' +
                ", userEmail='" + user_email + '\'' +
                '}';
    }
}
