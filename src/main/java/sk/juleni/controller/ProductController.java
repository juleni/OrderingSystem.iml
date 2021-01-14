package sk.juleni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sk.juleni.model.Product;
import sk.juleni.model.User;
import sk.juleni.repository.ProductRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Controller responsible for managing of Product objects (CRUD operations)
 *
 * @author Julian Legeny
 */

@Controller
public class ProductController {
    @Autowired
    ProductRepository prepo;

    /**
     * Display page for adding/editing product (default product page as well)
     *
     * @return ModelAndView - set up attributes
     *                        message - no item found message (for list)
     *                        products - list of all products that will be shown on the page
     */
    @GetMapping("/productpage/{product_id}")
    public ModelAndView product_page(@PathVariable("product_id") Long productId, HttpSession session) {
        ModelAndView mv = new ModelAndView("productpage");
        User currentUser = (User) session.getAttribute("currentUser");
        Product editProduct = null;

        if (productId != null && productId > Product.NEW_ID) {
            // there was sent product_id in the URL - it means retrieve product info from DB
            editProduct = prepo.findOneById(productId);
        } else {
            // new product should be created
            editProduct = new Product(Product.NEW_ID);
        }
        mv.addObject("editProduct", editProduct);

        // TODO: UPGRADE: Make it Pageable
        List<Product> allProducts = prepo.findByUserId(currentUser.getUser_id().toString());
        mv.addObject("products", allProducts);

        if (allProducts != null && allProducts.size() == 0) {
            mv.addObject("message", "Nenašli sa žiadne položky.");
        }
        return mv;
    }

    /**
     * Adding/saving product
     *
     * @param product_code - code of the product that is checked if not exists
     * @param product - Product object that will be added
     * @return ModelAndView - set up attributes
     *                        message_err - error message if product_code already exists in the db
     *                        message_ok - ok message if product was successfully added
     *                        products - list of user's belonged products that will be shown on the page
     */
    @PostMapping("/addProduct")
    public ModelAndView addProduct(@RequestParam("product_code") String product_code, Product product,
                                   HttpSession session)
    {
        ModelAndView mv = new ModelAndView("productpage");
        User currentUser = (User) session.getAttribute("currentUser");
        Boolean canSave = false;

        // do only if product code was typed (no empty spaces)
        if (product_code != null && !product_code.trim().isEmpty()) {
            if (product.getProduct_id() == Product.NEW_ID) {
                // there will be new product added
                // try to check in the db if product code already exists - if yes, dont save and just return message
                List<Product> list = prepo.findByCode(product_code);
                if(list != null && !list.isEmpty())
                {
                    mv.addObject("message_err", "Zadaný kód produktu už v systéme existuje. Zadajte iný.");
                } else {
                    canSave = true;
                    product.setBaseProduct(product);
                }
            }
            if ((product.getProduct_id() > Product.NEW_ID) || canSave) {
                try {
                    // there will be added/updated 'common' product the user can add to any order in the future
                    // It means there have to be set up user_id column in  the product table
                    // set up current user to the product
                    product.setUserProduct(currentUser);
                    prepo.save(product);
                    mv.addObject("message_ok","Produkt bol úspešne uložený.");
                } catch (Exception e) {
                    mv.addObject("message_err", "Vyskytla sa chyba pri ukladaní.");
                }
            }
        } else {
            mv.addObject("message_err", "Kód produktu nemôže byť prázdny. Zadajte iný.");
        }

        // refresh list of all products
        List<Product> allProducts = prepo.findByUserId(currentUser.getUser_id().toString());
        mv.addObject("products", allProducts);

        if (allProducts.isEmpty()) {
            mv.addObject("message", "Nenašli sa žiadne položky.");
        }

        return mv;
    }

    /**
     * Deleting product from the users base set of products
     *
     * @param product_id - ID of the product that should be deleted
     * @return ModelAndView - set up attributes
     *                        message_err - error message if error occured while deleting
     *                        message_ok - ok message if product was successfully deleted
     *                        products - list of user's belonged products that will be shown on the page
     */
    @PostMapping("/deleteProduct")
    public ModelAndView deleteProduct(@RequestParam("product_id") String product_id, Product product, HttpSession session)
    {
        ModelAndView mv = new ModelAndView("productpage");
        try {
            prepo.delete(product);
            mv.addObject("message_ok","Produkt bol úspešne zmazaný.");
        } catch (Exception e) {
            mv.addObject("message_err", "Vyskytla sa chyba pri mazaní.");
        }

        User currentUser = (User) session.getAttribute("currentUser");
        // refresh list of all products
        List<Product> allProducts = prepo.findByUserId(currentUser.getUser_id().toString());
        mv.addObject("products", allProducts);

        if (allProducts.isEmpty()) {
            mv.addObject("message", "Nenašli sa žiadne položky.");
        }

        return mv;
    }
}
