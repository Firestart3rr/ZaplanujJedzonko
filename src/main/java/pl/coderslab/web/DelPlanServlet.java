package pl.coderslab.web;

import pl.coderslab.dao.PlanDao;
import pl.coderslab.dao.RecipeDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/delplan")
public class DelPlanServlet extends HttpServlet {
    private int planId;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("id")!=null) planId = Integer.parseInt(request.getParameter("id"));

        if (request.getParameter("del")!=null && request.getParameter("del").equals("true")) {
            PlanDao pdao = new PlanDao();
            pdao.delete(planId);
            getServletContext().getRequestDispatcher("/app/plan/list").forward(request, response);
        } else getServletContext().getRequestDispatcher("/plan-delete-confirmation-popup.jsp").forward(request, response);

    }
}