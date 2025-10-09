package controller.servlet;

import dao.BrandDAO;
import dao.CarDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/cars")
public class CarsServlet extends HttpServlet {
    private CarDAO carDAO;
    private BrandDAO brandDAO;

    @Override
    public void init() throws ServletException {
        carDAO = new CarDAO();
        brandDAO = new BrandDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String brand = request.getParameter("brand");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");
        String keyword = request.getParameter("keyword");

        List<model.Car> carList;
        if (keyword != null && !keyword.trim().isEmpty()) {
            carList = carDAO.searchCars(keyword);
        } else if (brand != null && !brand.isEmpty()) {
            try {
                int brandId = Integer.parseInt(brand);
                carList = carDAO.getCarsByBrand(brandId);
            } catch (NumberFormatException e) {
                carList = carDAO.getAllCars();
            }
        } else if (minPriceStr != null && maxPriceStr != null) {
            try {
                double minPrice = Double.parseDouble(minPriceStr);
                double maxPrice = Double.parseDouble(maxPriceStr);
                carList = carDAO.getCarsByPriceRange(minPrice, maxPrice);
            } catch (NumberFormatException e) {
                carList = carDAO.getAllCars();
            }
        } else {
            carList = carDAO.getAllCars();
        }

        List<model.Brand> brandList = brandDAO.getAllBrands();
        request.setAttribute("carList", carList);
        request.setAttribute("brandList", brandList);

        // Kiểm tra vai trò admin
        HttpSession session = request.getSession(false);
        boolean isAdmin = session != null && "ADMIN".equals(session.getAttribute("userRole"));
        request.setAttribute("isAdmin", isAdmin);

        request.getRequestDispatcher("/WEB-INF/views/public/cars.jsp").forward(request, response);
    }
}