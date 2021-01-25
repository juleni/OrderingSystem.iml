package sk.juleni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sk.juleni.model.User;
import sk.juleni.repository.OrderRepository;
import sk.juleni.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Controller responsible for managing of User objects (CRUD operations)
 *
 * @author Julian Legeny
 */
@Controller
public class UserController {

    @Autowired
    UserRepository urepo;
    @Autowired
    OrderRepository orepo;

    /**
     * Display page for adding/editing user (default user page as well)
     *
     * @return ModelAndView - set up attributes
     *                        message - no item found message (for list)
     *                        users - list of all users that will be shown on the page
     */
    @GetMapping("/userpage/{user_id}")
    public ModelAndView user_page(@PathVariable("user_id") Long userId) {

        ModelAndView mv = new ModelAndView("userpage");
        User editUser;

        if (userId != null && userId > User.NEW_ID) {
            // there was sent user_id in the URL - it means retrieve user info from DB
            editUser = urepo.findOneById(userId);
        } else {
            // new product should be created
            editUser = new User(User.NEW_ID);
        }
        mv.addObject("editUser", editUser);

        // TODO: UPGRADE: Make it Pageable
        List<User> allUsers = urepo.findAll();
        mv.addObject("users", allUsers);

        if (allUsers.isEmpty()) {
            mv.addObject("message", "Nenašli sa žiadne položky.");
        }
        return mv;
    }

    /**
     * Adding new user
     *
     * @param user_login - user login that is checked if not exists
     * @param user - User object that will be added
     * @return ModelAndView - set up attributes
     *                        message_err - error message if user_login already exists in the db
     *                        message_ok - ok message if user was successfully added
     */
    @PostMapping("/addUser")
    public ModelAndView addUser(@RequestParam("user_login") String user_login, User user)
    {
        ModelAndView mv=new ModelAndView("userpage");
        boolean canSave = false;

        if (user.getUser_id().equals(User.NEW_ID)) {
            // there will be new user added
            // try to check in the db if login code already exists - if yes, dont save and just return message
            List<User> list=urepo.findByLogin(user_login);

            if(list != null && list.size()!=0)
            {
                mv.addObject("message_err", "Zadaný login už v systéme existuje. Zadajte iný.");

            } else { canSave = true; }
        }
        if (canSave || (user.getUser_id() > User.NEW_ID))
        {
            try {
                // there will be added/updated user
                urepo.save(user);
                mv.addObject("message_ok","Užívateľ bol úspešne uložený.");
            } catch (Exception e) {
                mv.addObject("message_err", "Vyskytla sa chyba pri ukladaní.");
            }
        }
        // refresh list of all users
        List<User> allUsers = urepo.findAll();
        mv.addObject("users", allUsers);

        return mv;
    }

    /**
     * Deleting user
     *
     * @param user_id - user login that is checked if not exists
     * @param user - User object that will be added
     * @return ModelAndView - set up attributes
     *                        message_err - error message if user_login already exists in the db
     *                        message_ok - ok message if user was successfully added
     */
    @PostMapping("/deleteUser")
    public ModelAndView deleteUser(@RequestParam("user_id") String user_id, User user, HttpSession session)
    {
        ModelAndView mv=new ModelAndView("userpage");
        User currentUser = (User) session.getAttribute("currentUser");
        if (!user.getUser_id().equals(currentUser.getUser_id())) {
            try {
                urepo.delete(user);
                mv.addObject("message_ok","Užívateľ bol úspešne zmazaný.");
            } catch (Exception e) {
                mv.addObject("message_err", "Vyskytla sa chyba pri mazaní.");
            }
        } else {
            mv.addObject("message_err", "Nemožno zmazať aktuálne prihláseného užívateľa.");
        }
        // refresh list of all users
        List<User> allUsers = urepo.findAll();
        mv.addObject("users", allUsers);

        return mv;
    }


    @GetMapping(value = "/logout")
    public String logout_user(HttpSession session) {
        session.removeAttribute("username");
        session.invalidate();
        return "redirect:/login";
    }
}
