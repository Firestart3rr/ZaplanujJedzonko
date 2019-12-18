package pl.coderslab.web;

import pl.coderslab.dao.RecipeDao;
import pl.coderslab.model.Recipe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/app/recipe/details")
public class AppRecipeDetailsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int recipeId = Integer.parseInt(request.getParameter("id"));
        String recipeName = request.getParameter("name");
        RecipeDao rdao = new RecipeDao();
        Recipe recipe = new Recipe();
        if (recipeId > 0) {
            recipe = rdao.read(recipeId);
        }
        if (recipeName != null) {
            recipe = rdao.read(recipeName);
        }
        request.setAttribute("recipe", recipe);
        getServletContext().getRequestDispatcher("/app-recipe-details.jsp").forward(request, response);
    }
}
