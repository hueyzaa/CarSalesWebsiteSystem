package controller.customer;

import dao.BrandDAO;
import dao.CarDAO;
import model.Brand;
import model.Car;
import util.SessionUtils;
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

/**
 * CarsServlet - Display and filter car listings
 * Accessible by everyone (Guest/Customer/Staff/Admin)
 */
@WebServlet("/cars")
public class CarsServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CarsServlet.class);
    private CarDAO carDAO;
    private BrandDAO brandDAO;

    @Override
    public void init() throws ServletException {
        super.init();
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

            String finalKeyword = searchKeyword != null ? searchKeyword : keyword;

            // Get filtered cars
            List<Car> carList = getFilteredCars(finalKeyword, brandParam, minPriceStr, maxPriceStr, request);

            // Apply sorting
            if (sortBy != null && !sortBy.isEmpty()) {
                carList = applySorting(carList, sortBy);
                request.setAttribute("sortBy", sortBy);
            }

            // Load brands for filter
            List<Brand> brandList = brandDAO.getAllBrands();

            // Set attributes
            request.setAttribute("carList", carList);
            request.setAttribute("brandList", brandList);
            request.setAttribute("totalCars", carList.size());

            // Set user info
            setUserAttributes(request);

            logger.info("Loaded {} cars with filters - keyword: {}, brand: {}, sort: {}",
                    carList.size(), finalKeyword, brandParam, sortBy);

            request.getRequestDispatcher("/WEB-INF/views/cars.jsp")
                    .forward(request, response);

        } catch (RuntimeException e) {
            logger.error("Database error in CarsServlet", e);
            request.setAttribute("error", "Không thể tải danh sách xe. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            logger.error("Unexpected error in CarsServlet", e);
            request.setAttribute("error", "Đã xảy ra lỗi không mong muốn.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                    .forward(request, response);
        }
    }

    /**
     * Get filtered cars based on parameters
     */
    private List<Car> getFilteredCars(String keyword, String brandParam,
                                      String minPriceStr, String maxPriceStr,
                                      HttpServletRequest request) {
        // Search by keyword
        if (keyword != null && !keyword.trim().isEmpty()) {
            request.setAttribute("searchKeyword", keyword.trim());
            logger.debug("Searching cars with keyword: {}", keyword);
            return carDAO.searchCars(keyword.trim());
        }

        // Filter by brand
        if (brandParam != null && !brandParam.trim().isEmpty()) {
            try {
                int brandId = Integer.parseInt(brandParam);
                request.setAttribute("selectedBrand", brandId);
                logger.debug("Filtering cars by brand ID: {}", brandId);
                return carDAO.getCarsByBrand(brandId);
            } catch (NumberFormatException e) {
                logger.warn("Invalid brand ID format: {}", brandParam);
            }
        }

        // Filter by price range
        if (minPriceStr != null && maxPriceStr != null
                && !minPriceStr.trim().isEmpty() && !maxPriceStr.trim().isEmpty()) {
            try {
                double minPrice = Double.parseDouble(minPriceStr);
                double maxPrice = Double.parseDouble(maxPriceStr);

                if (minPrice >= 0 && maxPrice >= minPrice) {
                    request.setAttribute("minPrice", minPrice);
                    request.setAttribute("maxPrice", maxPrice);
                    logger.debug("Filtering cars by price range: {} - {}", minPrice, maxPrice);
                    return carDAO.getCarsByPriceRange(minPrice, maxPrice);
                } else {
                    logger.warn("Invalid price range: {} - {}", minPrice, maxPrice);
                }
            } catch (NumberFormatException e) {
                logger.warn("Invalid price format: {} - {}", minPriceStr, maxPriceStr);
            }
        }

        // Default: get all cars
        logger.debug("Loading all cars");
        return carDAO.getAllCars();
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
                logger.debug("Unknown sort type: {}", sortBy);
                break;
        }

        return carList;
    }

    /**
     * Set user-related attributes
     */
    private void setUserAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (SessionUtils.isLoggedIn(session)) {
            request.setAttribute("isLoggedIn", true);
            request.setAttribute("userRole", SessionUtils.getUserRole(session));
            request.setAttribute("userId", SessionUtils.getUserId(session));
            request.setAttribute("isAdmin", SessionUtils.isAdmin(session));
            request.setAttribute("isStaff", SessionUtils.isStaffOrAdmin(session));
            request.setAttribute("isCustomer", SessionUtils.isCustomer(session));

            logger.debug("User viewing cars - ID: {}, Role: {}",
                    SessionUtils.getUserId(session), SessionUtils.getUserRole(session));
        } else {
            request.setAttribute("isLoggedIn", false);
            request.setAttribute("isAdmin", false);
            request.setAttribute("isStaff", false);
            request.setAttribute("isCustomer", false);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("CarsServlet destroyed");
    }
}