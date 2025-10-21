package controller.customer;

import dao.BrandDAO;
import dao.CarDAO;
import model.Brand;
import model.Car;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cars")
public class CarsServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CarsServlet.class);
    private CarDAO carDAO;
    private BrandDAO brandDAO;

    @Override
    public void init() throws ServletException {
        carDAO = new CarDAO();
        brandDAO = new BrandDAO();
        logger.info("CarsServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get filter parameters
            String searchKeyword = request.getParameter("search");
            String keyword = request.getParameter("keyword");
            String brandParam = request.getParameter("brand");
            String minPriceStr = request.getParameter("minPrice");
            String maxPriceStr = request.getParameter("maxPrice");
            String sortBy = request.getParameter("sort");

            // Combine search parameters
            String finalKeyword = searchKeyword != null ? searchKeyword : keyword;

            List<Car> carList = new ArrayList<>();

            // Apply filters with priority
            if (finalKeyword != null && !finalKeyword.trim().isEmpty()) {
                // Search by keyword
                carList = carDAO.searchCars(finalKeyword.trim());
                request.setAttribute("searchKeyword", finalKeyword.trim());
                logger.debug("Searching cars with keyword: {}", finalKeyword);

            } else if (brandParam != null && !brandParam.trim().isEmpty()) {
                // Filter by brand
                try {
                    int brandId = Integer.parseInt(brandParam);
                    carList = carDAO.getCarsByBrand(brandId);
                    request.setAttribute("selectedBrand", brandId);
                    logger.debug("Filtering cars by brand ID: {}", brandId);
                } catch (NumberFormatException e) {
                    logger.warn("Invalid brand ID format: {}", brandParam);
                    carList = carDAO.getAllCars();
                }

            } else if (minPriceStr != null && maxPriceStr != null
                    && !minPriceStr.trim().isEmpty() && !maxPriceStr.trim().isEmpty()) {
                // Filter by price range
                try {
                    double minPrice = Double.parseDouble(minPriceStr);
                    double maxPrice = Double.parseDouble(maxPriceStr);

                    if (minPrice >= 0 && maxPrice >= minPrice) {
                        carList = carDAO.getCarsByPriceRange(minPrice, maxPrice);
                        request.setAttribute("minPrice", minPrice);
                        request.setAttribute("maxPrice", maxPrice);
                        logger.debug("Filtering cars by price range: {} - {}", minPrice, maxPrice);
                    } else {
                        logger.warn("Invalid price range: {} - {}", minPrice, maxPrice);
                        carList = carDAO.getAllCars();
                    }
                } catch (NumberFormatException e) {
                    logger.warn("Invalid price format: {} - {}", minPriceStr, maxPriceStr);
                    carList = carDAO.getAllCars();
                }

            } else {
                // Get all cars
                carList = carDAO.getAllCars();
                logger.debug("Loading all cars");
            }

            // Apply sorting if requested
            if (sortBy != null && !sortBy.isEmpty()) {
                carList = applySorting(carList, sortBy);
                request.setAttribute("sortBy", sortBy);
            }

            // Load brand list for filter dropdown
            List<Brand> brandList = brandDAO.getAllBrands();

            // Set attributes
            request.setAttribute("carList", carList);
            request.setAttribute("brandList", brandList);
            request.setAttribute("totalCars", carList.size());

            // Check user role and authentication
            HttpSession session = request.getSession(false);
            if (session != null) {
                User user = (User) session.getAttribute("user");
                if (user != null) {
                    request.setAttribute("isLoggedIn", true);
                    request.setAttribute("isAdmin", user.isAdmin());
                    request.setAttribute("isStaff", user.isStaff() || user.isAdmin());
                }
            }

            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/views/cars.jsp").forward(request, response);

        } catch (RuntimeException e) {
            logger.error("Database error in CarsServlet", e);
            request.setAttribute("error", "Không thể tải danh sách xe. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Unexpected error in CarsServlet", e);
            request.setAttribute("error", "Đã xảy ra lỗi không mong muốn.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    /**
     * Apply sorting to car list
     */
    private List<Car> applySorting(List<Car> carList, String sortBy) {
        if (carList == null || carList.isEmpty()) {
            return carList;
        }

        switch (sortBy) {
            case "price_asc":
                carList.sort((c1, c2) -> Double.compare(c1.getPrice(), c2.getPrice()));
                logger.debug("Sorted by price ascending");
                break;

            case "price_desc":
                carList.sort((c1, c2) -> Double.compare(c2.getPrice(), c1.getPrice()));
                logger.debug("Sorted by price descending");
                break;

            case "name_asc":
                carList.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
                logger.debug("Sorted by name ascending");
                break;

            case "name_desc":
                carList.sort((c1, c2) -> c2.getName().compareToIgnoreCase(c1.getName()));
                logger.debug("Sorted by name descending");
                break;

            case "year_desc":
                carList.sort((c1, c2) -> Integer.compare(c2.getYear(), c1.getYear()));
                logger.debug("Sorted by year descending");
                break;

            case "newest":
                carList.sort((c1, c2) -> Integer.compare(c2.getId(), c1.getId()));
                logger.debug("Sorted by newest");
                break;

            default:
                logger.debug("No sorting applied or unknown sort type: {}", sortBy);
                break;
        }

        return carList;
    }

    @Override
    public void destroy() {
        logger.info("CarsServlet destroyed");
        super.destroy();
    }
}