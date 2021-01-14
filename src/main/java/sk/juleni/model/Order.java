package sk.juleni.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Order object represents orders. TOrder table is mapped as following:
 *
 *      [USER] --- 1:M ---  [PRODUCT] --- M:1 ---  [TORDER] --- M:1 --- [USER]
 *
 * @author Julian Legeny
 */
@Entity
// !!! be aware of the table name that is reserved sql word (e.g. order :)
// make unique constraint for (user_id and order_no) - orders belonged to one user should have unique numbers
@Table(name = "torder",
       uniqueConstraints={@UniqueConstraint(columnNames = {"user_id", "order_no"})})
public class Order extends DataObject {

    public static final String OBJ_PREFIX = "OBJ-";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable=false)
    private User userOrder;

    // join column to the product table
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,
               mappedBy="orderProduct")
    //    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    //    @JoinTable(
    //            name = "order_product",
    //            joinColumns = @JoinColumn(name = "order_id"),
    //            inverseJoinColumns = @JoinColumn(name = "product_id")
    //    )
    private Set<Product> orderProducts = new HashSet<>();

    @NotNull
    // @Column(unique = true)
    private String order_no;

    @NotNull
    private String order_name;
    private String order_desc;
    private double order_discount;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date order_last_modified = new Date();;

    @Transient
    private BigDecimal totalOrderPrice = new BigDecimal(0);

    @Transient
    private BigDecimal totalOrderPriceDiscount = new BigDecimal(0);

    @Transient
    private String userLogin;

    // used for generating of product_no
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1000);
    // generate order_number when asking for
    private String generateOrderNumber() {
        return OBJ_PREFIX + ID_GENERATOR.getAndIncrement();
    }

    public Order() {
        // required by JPA but not used for me
    }

    /**
     * Constructor for creating the order with generated order_no
     *
     * @param order_id - Long - order ID, has to be unique or -1 if new object if created
     */
    public Order(Long order_id, boolean generateNumber) {
        this.order_id = order_id;
        if (generateNumber) {
            this.order_no = this.generateOrderNumber();
        }
    }

    /**
     * Constructor that allows to generate order_number - that should be unique
     *
     * @param generateNumber - boolean yes, if the order_number has to be generated
     */
//    public Order(boolean generateNumber) {
//        if (generateNumber) {
//            this.order_no = this.generateOrderNumber();
//            this.order_name = "";
//            this.order_desc = "";
//            this.order_discount = 0.00;
//        }
//    }

    /**
     * Constructor for creating test items in the Application class
     *
     * @param name - name of the order
     * @param order_discount - order discount - should be double
     * @param desc - description of the order
     * @param products - array of Product objects assigned to the order
     */
    public Order(String name, double order_discount, String desc, Product... products) {

        this.order_no = this.generateOrderNumber();
        this.order_name = name;
        this.order_discount = order_discount;
        this.order_desc = desc;
        this.orderProducts = Stream.of(products).collect(Collectors.toSet());
        // TODO: check lambda
        this.orderProducts.forEach(x -> x.setOrderProduct(this));
        this.totalOrderPrice = countTotalOrderPrice(this);
        this.totalOrderPriceDiscount = countTotalOrderPriceDiscount(this);
    }

    /**
     * Count the total price of the order. There are counted all products prices belonged to the order.
     *
     * @param order - Order object the total price is counted for
     * @return BigDecimal - Total price of the order
     */
    public BigDecimal countTotalOrderPrice(Order order) {
        BigDecimal totalSum = new BigDecimal(0);


        // iterate through all products and count total price of them -
        // TODO: can be speeded up by adding the order_total_price parameter to the order object/table
        if (order != null && order.getOrderProducts().size() > 0) {
            Set<Product> orderProducts = order.getOrderProducts();
            for (Product product : orderProducts) {
                totalSum = totalSum.add(product.getProduct_price());
            }

        }
        return totalSum;
    }

    /**
     * Count discount from the total order price
     *
     * @param order - order object the discount price is counted for
     * @return BigDecimal - discount from the total order price
     */
    public BigDecimal countTotalOrderPriceDiscount(Order order) {

        return order.getTotalOrderPrice().multiply(
                new BigDecimal(order.getOrder_discount())).divide(new BigDecimal(100));
    }

    public BigDecimal getTotalOrderPriceDiscount() {
        return totalOrderPriceDiscount;
    }

    public void setTotalOrderPriceDiscount(BigDecimal totalOrderPriceDiscount) {
        this.totalOrderPriceDiscount = totalOrderPriceDiscount;
    }

    public BigDecimal getTotalOrderPrice() {

        return totalOrderPrice;
    }

    public void setTotalOrderPrice(BigDecimal totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public Date getOrder_last_modified() {
        return order_last_modified;
    }

    public void setOrder_last_modified(Date order_last_modified) {
        this.order_last_modified = order_last_modified;
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public Set<Product> getOrderProducts() {
       return orderProducts;
    }

    public void setOrderProducts(Set<Product> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getOrder_desc() {
        return order_desc;
    }

    public void setOrder_desc(String order_desc) {
        this.order_desc = order_desc;
    }

    public double getOrder_discount() {
        return order_discount;
    }

    public void setOrder_discount(double order_discount) {
        this.order_discount = order_discount;
    }

    public User getUserOrder() {
        return userOrder;
    }

    /**
     * Set up User object the order is belonged to
     *
     * @param userOrder - User object the order  belongs to
     */
    public void setUserOrder(User userOrder) {
        this.userOrder = userOrder;
    }

    public String getUsreLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    /**
     * Add product to the list of products
     *
     * @param product - Product object to be added to the list
     */
    public void addProduct(Product product) {
        orderProducts.add(product);
        product.setOrderProduct(this);
    }

    /**
     * Remove product from the list of products
     *
     * @param product - Product object to be removed to the list
     */
    public void removeProduct(Product product) {
        product.setOrderProduct(null);
        this.orderProducts.remove(product);
    }

    @Override
    public String toString() {
        return "Order{" +
                "order_id=" + order_id +
                ", userOrder=" + userOrder +
                ", orderProducts=" + orderProducts +
                ", order_no='" + order_no + '\'' +
                ", order_name='" + order_name + '\'' +
                ", order_desc='" + order_desc + '\'' +
                ", order_discount=" + order_discount +
                ", order_last_modified=" + order_last_modified +
                ", totalOrderPrice=" + totalOrderPrice +
                ", totalOrderPriceDiscount=" + totalOrderPriceDiscount +
                ", userLogin='" + userLogin + '\'' +
                '}';
    }
}
