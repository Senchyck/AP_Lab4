package controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dao.PizzaDAO;
import dto.OrderDTO;
import entity.PizzaOrder;
import entity.User;
import enums.PizzaOrderStatus;
import enums.PizzaSize;
import repository.OrderRepository;
import repository.UserRepository;
import util.PizzaUtils;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PizzaDAO pizzaDAO;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PizzaUtils pizzaUtils;

    // Initial sort settings
    private String SortSettings = "sort-by=order-date&order=desc";

    @GetMapping("/list")
    public String listAll(Model model, HttpServletRequest request) {

        User loggedInUser = getLoggedInUser(request.getUserPrincipal());

        List<PizzaOrder> orders = new ArrayList<>();

        if (request.isUserInRole("CUSTOMER")) {
            orders = orderRepository.findAllByUserOrderByOrderDateDesc(loggedInUser);
        } else {
            orders = orderRepository.findAllByOrderByOrderDateDesc();
        }

        List<OrderDTO> orderDTOs = new ArrayList<>();

        orders.forEach(order -> {
            double smallPizzaPrice = pizzaUtils.calculateSmallPizzaPrice(order.getPizza());
            double pizzaPrice = smallPizzaPrice * order.getPizzaSize().getPriceMultiplier() * order.getQuantity();

            orderDTOs.add(new OrderDTO(order.getId(), order.getOrderDate(), order.getStatus(), order.getPizza(),
                    order.getPizzaSize(), order.getQuantity(), pizzaPrice));
        });

        model.addAttribute("orderDTOs", orderDTOs);

        return "orders/list-pizza-orders";
    }

    @GetMapping("/add")
    public String selectPizza() {
        return "redirect:/pizzas/list?select";
    }

    @GetMapping("/add/{pizzaId}")
    public String showAddPizzaOrderForm(@PathVariable int pizzaId, Model model, Principal principal) {

        PizzaOrder pizzaOrder = new PizzaOrder();

        pizzaOrder.setPizza(pizzaDAO.findById(pizzaId));

        //Set initial values for: status, size, quantity
        pizzaOrder.setStatus(PizzaOrderStatus.ORDERED);
        pizzaOrder.setPizzaSize(PizzaSize.LARGE);
        pizzaOrder.setQuantity(1);
        pizzaOrder.setOrderDate(LocalDateTime.now(ZoneId.of("Europe/Ukraine")));

        User loggedInUser = getLoggedInUser(principal);
        pizzaOrder.setUser(loggedInUser);

        model.addAttribute("allSizes", PizzaSize.values());
        model.addAttribute("pizzaOrder", pizzaOrder);

        return "orders/add-or-update-pizza-order-form";
    }

    private User getLoggedInUser(Principal principal) {

        // principal.getName() means get email, because we use email as user name
        // method getName() is imposed by Principal interface
        String email = principal.getName();

        return userRepository.findByEmail(email);
    }

    @PostMapping("/add")
    public String savePizzaOrder(@Valid @ModelAttribute PizzaOrder pizzaOrder, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("allSizes", PizzaSize.values());

            return "orders/add-or-update-pizza-order-form";
        }

        pizzaOrder.setId(0);

        orderRepository.save(pizzaOrder);

        return "redirect:/orders/list";
    }

    @GetMapping("/update/{orderId}")
    public String showUpdatePizzaOrderForm(@PathVariable int orderId,  Model model) {

        model.addAttribute("pizzaOrder", orderRepository.findById(orderId).orElseGet(() -> {
            PizzaOrder order = new PizzaOrder();
            order.setId(9999);
            return order;
        }));

        model.addAttribute("allSizes", PizzaSize.values());

        return "orders/add-or-update-pizza-order-form";
    }

    @PostMapping("/update")
    public String updatePizzaOrder(@Valid @ModelAttribute PizzaOrder order, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("allSizes", PizzaSize.values());

            return "orders/add-or-update-pizza-order-form";
        }

        orderRepository.save(order);

        return "redirect:/orders/list";
    }

    @GetMapping("/delete/{orderId}")
    public String deleteById(@PathVariable int orderId) {

        orderRepository.deleteById(orderId);

        return "redirect:/orders/list";
    }

    @GetMapping("/change-status/{orderId}")
    public String showChangeOrderStatusForm(@PathVariable int orderId, Model model) {

        model.addAttribute("pizzaOrder", orderRepository.findById(orderId).get());
        model.addAttribute("allStatuses", PizzaOrderStatus.values());

        return "orders/change-order-status-form";
    }

    @PostMapping("/change-status")
    public String changeOrderStatus(@ModelAttribute PizzaOrder pizzaOrder) {

        orderRepository.save(pizzaOrder);

        return "redirect:/orders/list";
    }





}
