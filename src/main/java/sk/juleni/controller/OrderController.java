package sk.juleni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sk.juleni.model.Order;
import sk.juleni.model.Product;
import sk.juleni.model.User;
import sk.juleni.repository.OrderRepository;
import sk.juleni.repository.ProductRepository;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Controller responsible for managing of Order objects (CRUD operations)
 *
 * @author Julian Legeny
 */
@Controller
public class OrderController {
    @Autowired
    OrderRepository orepo;

    @Autowired
    ProductRepository prepo;

    /**
     * Display page for adding new order (default order page as well)
     *
     * @return ModelAndView - set up attributes
     *                        message - no item found message (for list)
     *                        products - list of all products that can be assigned to the product (in select list)
     *                        orders - list of all products that will be shown on the page
     */
    @GetMapping("/orderpage/{order_id}")
    public ModelAndView order_page(@PathVariable("order_id") Long orderId, HttpSession session) {
        ModelAndView mv = new ModelAndView("orderpage");
        Order editOrder = null;

        if(orderId != null && orderId >  Order.NEW_ID) {
            // there was sent order_id in the URL - it means retrieve order info from DB
            editOrder = orepo.findOneById(orderId);

            // retrieve all belonged order products
            List<Long> productIDs = new ArrayList<Long>();
            Set<Product> lstBelongedProducts = editOrder.getOrderProducts();
            Iterator<Product> itIterator = lstBelongedProducts.iterator();
            while (itIterator.hasNext()) {
                productIDs.add(itIterator.next().getProduct_id());

            }

            mv.addObject("productIDs", productIDs);


        } else {
            // new order should be created
            editOrder = new Order(Order.NEW_ID, true);
        }
        mv.addObject("editOrder", editOrder);


        // call helper method that will set up list of orders and list of products to the ModelView parameters
        setOrderProductParameters(mv, session);
        return mv;
    }

    /**
     * Adding new order and belonged products
     *
     * @param productIDs - IDs of selected products from multiselect
     * @param order - Order object that will be added
     * @return ModelAndView - set up attributes
     *                        message_err - error message if product_code already exists in the db
     *                        message_ok - ok message if product was successfully added
     *                        products - list of all products that can be assigned to the product (in select list)
     *                        orders - list of all products that will be shown on the page
     */
    @PostMapping("/addOrder")
    public ModelAndView addOrder(@RequestParam("productIDs") List<Long> productIDs, Order order, HttpSession session)
    {
        // after processing go to the orderpage
        ModelAndView mv = new ModelAndView("orderpage");
        User currentUser = (User) session.getAttribute("currentUser");
        Boolean canSave = false;

        // do only if order number was typed (no empty spaces)
        if (order != null && !order.getOrder_no().trim().isEmpty()) {
            if (order.getOrder_id() == Order.NEW_ID) {
                // there will be new order added
                // check if the order number does not exists (unique parameter) for surrent user
                //       - if yes, do not save and just return message
                Order checkOrder = orepo.findByNoAndUserId(order.getOrder_no(), currentUser.getUser_id());
                if(checkOrder != null)
                {
                    mv.addObject("message_err", "Zadané číslo objednávky už v systéme existuje. Zadajte iné.");
                } else { canSave = true; }

            }
            if ((order.getOrder_id() > Order.NEW_ID) || canSave) {
                try {
                    // retrieve list of assigned products
                    if (productIDs != null) {
                        List<Product> lstProducts = prepo.findByProductIDs(productIDs);
                        Product newProduct;
                        for (Product product : lstProducts) {
                            // create new Product objects to be assigned and remove constraint user_id from the product object
                            // the product will be assigned to the order from now
                            newProduct = new Product(product);
                            newProduct.setUserProduct(null);
                            order.addProduct(newProduct);
                        }
                    }

                    // create User object and set up currentUserId - the saved order will belong to
                    order.setUserOrder(currentUser);
                    orepo.save(order);
                    mv.addObject("message_ok","Produkt bol úspešne uložený.");
                } catch (Exception e) {
                    mv.addObject("message_err", "Vyskytla sa chyba pri ukladaní.");
                }
            }
        } else {
            mv.addObject("message_err", "Číslo objednávky nemôže byť prázdne. Zadajte iné.");
        }

        // call helper method that will set up list of orders and list of products to the ModelView parameters
        setOrderProductParameters(mv, session);

        return mv;
    }

    /**
     * Deleting order and belonged products
     *
     * @param order_id - ID of the order to be deleted
     * @param order - Order object that will be deleted
     * @return ModelAndView - set up attributes
     *                        message_err - error message if product_code already exists in the db
     *                        message_ok - ok message if product was successfully added
     *                        products - list of all products that can be assigned to the product (in select list)
     *                        orders - list of all products that will be shown on the page
     */
    @PostMapping("/deleteOrder")
    public ModelAndView deleteOrder(@RequestParam("order_id") String order_id, Order order, HttpSession session)
    {
        ModelAndView mv = new ModelAndView("orderpage");
        try {
            // TODO: Rework this as a service @Transactional
            prepo.deleteByOrderId(order_id);
            orepo.delete(order);
            mv.addObject("message_ok","Objednávka bola úspešne zmazaná.");
        } catch (Exception e) {
            mv.addObject("message_err", "Vyskytla sa chyba pri mazaní.");
        }
        // call helper method that will set up list of orders and list of products to the ModelView parameters
        setOrderProductParameters(mv, session);

        return mv;
    }

    /**
     * Helper method for setting up the parameters to the ModelView. This code is used on more places - duplicity.
     * TODO: Check it out - retrieving and setting up the objects
     *
     * @param mv ModelAndView - ModelAndView that will be set up parameters for
     * @param session - HttpSession - session the currentUserId is retrieved from
     * @return ModelAndView - updated ModelAndView
     */
    private ModelAndView setOrderProductParameters(ModelAndView mv, HttpSession session) {
        // set default order data (e.g. object_no - in the constructor)
        //mv.addObject("defaultEmptyOrder", new Order(true));
        User currentUser = (User) session.getAttribute("currentUser");

        // get list of all base products belonged to the user
        List<Product> allProducts = prepo.findByUserId(currentUser.getUser_id().toString());
        mv.addObject("products", allProducts);

        List<Order> allUserOrders = orepo.findByUserId(currentUser.getUser_id().toString());
        // update order prices for all orders - to the transient Order attribute totalOrderPrice
        // TODO: UPGRADE: Implement setting of transient attributes totalOrderPrice and totalOrderPriceDiscount
        //                somewhere in the OrderRepositoryImpl??? - find out how to do it!
        for (Order order : allUserOrders) {
            order.setTotalOrderPrice(order.countTotalOrderPrice(order));
            order.setTotalOrderPriceDiscount(order.countTotalOrderPriceDiscount(order));
        }
        mv.addObject("orders", allUserOrders);

        if (allUserOrders != null && allUserOrders.size() == 0) {
            mv.addObject("message", "Nenašli sa žiadne položky.");
        }

        return mv;
    }
}

