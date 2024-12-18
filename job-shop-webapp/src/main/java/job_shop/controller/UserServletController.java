package job_shop.controller;

import job_shop.dao.CustomerDao;
import job_shop.model.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserServlet", value = "/users")
public class UserServletController extends HttpServlet {
    private CustomerDao customerDao = new CustomerDao();

    /**
    * Handle view user page, add_user page and search users by category
     *
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String type = req.getParameter("type");
            List<Customer> customerList;
            if (type == null || type.isEmpty()) {
                customerList = customerDao.findAll();
                req.setAttribute("customerList", customerList);
                req.getRequestDispatcher("user.jsp").forward(req, resp);
            } else if ("SEARCH".equalsIgnoreCase(type)) {
                int category1 = Integer.parseInt(req.getParameter("category1"));
                int category2 = Integer.parseInt(req.getParameter("category2"));
                customerList = customerDao.findAllByCategory(category1, category2);
                req.setAttribute("customerList", customerList);
                req.getRequestDispatcher("user.jsp").forward(req, resp);
            } else if ("ADD".equalsIgnoreCase(type)) {
                req.getRequestDispatcher("add_user.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle add user
     *
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String name = req.getParameter("name");
            String address = req.getParameter("address");
            int category = Integer.parseInt(req.getParameter("category"));
            Customer customer = new Customer(name, address, category);
            boolean result = customerDao.add(customer.getName(), customer.getAddress(), customer.getCategory());
            if (result) {
                System.out.println("Add the customer successfully");
            } else {
                System.out.println("Add the customer fail");
            }
            resp.sendRedirect(req.getContextPath() + "/users");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
