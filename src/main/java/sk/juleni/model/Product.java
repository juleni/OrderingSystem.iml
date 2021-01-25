package sk.juleni.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Product object represents all products. Product table is mapped as following:
 *
 *      [USER] --- 1:M --- [PRODUCT] --- M:1 --- [TORDER] --- M:1 --- [USER]
 *                         [PRODUCT] --- M:1 --- [PRODUCT]
 *
 *      base_id  - self referenced ID to the base product (parent)
 *      order_id - ID of the order the product belongs to (in this case user_id = null)
 *               - IF = null, the product is not assigned to the order - in this case it means the product belongs
 *                 to the user and user_id has to be specified (product is common and is used for creating new order)
 *      user_id - ID of the user the product belongs to (in this case order_id = null)
 *              - IF = null, the product is assigned to the order - in this case it means the product belongs
 *                to the order and order_id has to be specified (product is assigned to the order)
 *
 * @author Julian Legeny
 *
 */
@Entity
// make unique constraint for (user_id and product_code) - all product codes related to the one user should be unique
@Table(name = "product",
       uniqueConstraints={@UniqueConstraint(columnNames = {"user_id", "product_code"})})
public class Product extends DataObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    @ManyToOne
    @JoinColumn(name = "base_id")
    // base product the current product belongs to
    Product baseProduct;

    // reference to the order table the product belongs to
    // if the order_id is specified - then user_id should be null
    @ManyToOne
    @JoinColumn(name = "order_id", nullable=true)
    private Order orderProduct;

    // reference to the user table the product belongs to
    // if the user_id is specified - then product_id should be null
    @ManyToOne
    @JoinColumn(name = "user_id", nullable=true)
    private User userProduct;

    @NotNull
    // @Column(unique = true)
    private String product_code;
    @NotNull
    private String product_name;
    private String product_desc;
    private BigDecimal product_price;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date product_last_modified = new Date();

     public Product() {
        // required by JPA but not used for me
    }

    /**
     * Constructor for creating the product
     *
     * @param product_id - Long - product ID, has to be unique or -1 if new object if created
     */
    public Product(Long product_id) {
        this.product_id = product_id;
    }

    /**
     * Constructor for creating the product
     *
     * @param product_code - String - product code, has to be unique
     * @param product_name - String - product name
     * @param product_desc - String - product description
     * @param product_price - BigDecimal - product price
     */
    public Product(String product_code, String product_name, String product_desc, BigDecimal product_price) {
        this.product_code = product_code;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_desc = product_desc;
    }

    public Product(Product pProduct) {
        this.product_code = pProduct.getProduct_code();
        this.product_name = pProduct.getProduct_name();
        this.product_price = pProduct.getProduct_price();
        this.product_desc = pProduct.getProduct_desc();
        this.userProduct = pProduct.getUserProduct();
        this.orderProduct = pProduct.getOrderProduct();
        this.baseProduct = pProduct;
    }

    public Date getProduct_last_modified() {
        return product_last_modified;
    }

    public void setProduct_last_modified(Date product_last_modified) {
        this.product_last_modified = product_last_modified;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public BigDecimal getProduct_price() {
        return product_price;
    }

    public void setProduct_price(BigDecimal product_price) {
        this.product_price = product_price;
    }

    public Order getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(Order orderProduct) {
        this.orderProduct = orderProduct;
    }

    public User getUserProduct() {
        return userProduct;
    }

    public void setUserProduct(User userProduct) {
        this.userProduct = userProduct;
    }

    public Product getBaseProduct() {
        return baseProduct;
    }

    public void setBaseProduct(Product baseProduct) {
        this.baseProduct = baseProduct;
    }

    @Override
    public boolean equals(Object object)
    {
        return object != null
                && object.getClass().equals(this.getClass())
                && ((Product) object).getProduct_id().equals(this.product_id);
    }

    @Override
    public int hashCode()
    {
        return this.product_code.hashCode();
    }
}
