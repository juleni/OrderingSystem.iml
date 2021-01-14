package sk.juleni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sk.juleni.model.Order;
import sk.juleni.model.User;
import sk.juleni.repository.OrderRepository;
import sk.juleni.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Controller responsible for Login to the application
 * TODO: Add security
 *
 * @author Julian Legeny
 */
@Controller
public class LoginController {
    @Autowired
    UserRepository urepo;

    @Autowired
    OrderRepository orepo;

    @RequestMapping("/login")
    public String getLogin()
    {
        return "login";
    }

    @RequestMapping("/")
    public String home() {return "login"; }

    @PostMapping("/login")
    public String login_user(@RequestParam("username") String username, @RequestParam("password") String password,
                             HttpSession session, ModelMap modelMap) {

        User userAuth = urepo.findByUsernamePassword(username, password);

        if (userAuth != null) {

            String uName = userAuth.getUser_login();
            String uPass = userAuth.getUser_password();
            Long currentUserId = userAuth.getUser_id();

            if (username.equals(uName) && password.equals(uPass)) {
                session.setAttribute("username", username);
                session.setAttribute("currentUserId", currentUserId);
                session.setAttribute("currentUser", userAuth);


                List<Order> allUsersOrders = orepo.findAll();
                // update order prices for all orders - to the transient Order attribute totalOrderPrice
                // TODO: UPGRADE: Implement setting of transient attributes totalOrderPrice and totalOrderPriceDiscount
                //                somewhere in the OrderRepositoryImpl??? - find out how to do it!
                preprocessOrders(allUsersOrders);

                modelMap.put("orders", allUsersOrders);
                if (allUsersOrders == null || (allUsersOrders != null && allUsersOrders.size() == 0)) {
                    modelMap.put("message", "Nenašli sa žiadne položky.");
                }
                return "mainpage";
            }
            else {
                modelMap.put("error", "Nesprávne prihlasovacie údaje");
                return "login";
            }

        } else {
            modelMap.put("error", "Nesprávne prihlasovacie údaje");
            return "login";
        }
    }

    @GetMapping("/mainpage")
    public ModelAndView main_page1(HttpSession session) {
        ModelAndView mv = new ModelAndView("mainpage");

        // get list of all orders for all users
        List<Order> allUsersOrders = orepo.findAll();
        // update order prices for all orders - to the transient Order attribute totalOrderPrice
        // TODO: UPGRADE: Implement setting of transient attributes totalOrderPrice and totalOrderPriceDiscount
        //                somewhere in the OrderRepositoryImpl??? - find out how to do it!
        preprocessOrders(allUsersOrders);

        mv.addObject("orders", allUsersOrders);
        if (allUsersOrders != null && allUsersOrders.size() == 0) {
            mv.addObject("message", "Nenašli sa žiadne položky.");
        }
        return mv;
    }

    /**
     * Helper method for setting up transient parameters in the object
     *
     * @param lstOrders - List - list of orders
     * @return List - updated transient parameters in the list of orders
     */
    private List<Order> preprocessOrders(List<Order> lstOrders) {

        for (Order order : lstOrders) {
            // count and set up total order price and discount
            order.setTotalOrderPrice(order.countTotalOrderPrice(order));
            order.setTotalOrderPriceDiscount(order.countTotalOrderPriceDiscount(order));
            // set up userLogin for particular order
            order.setUserLogin(order.getUserOrder().getUser_login());
        }
        return lstOrders;
    }
}
