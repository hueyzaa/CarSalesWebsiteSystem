package controller.customer;

import dao.BrandDAO;
import dao.CarDAO;
import model.Brand;
import model.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@WebServlet("/cars")
public class CarsServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CarsServlet.class);

    private CarDAO carDAO;
    private BrandDAO brandDAO;

    @Override
    public void init() {
        carDAO = new CarDAO();
        brandDAO = new BrandDAO();
        logger.info("CarsServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get filter parameters
            String keyword = getParameter(request, "search", "keyword");
            String brandParam = request.getParameter("brand");
            String minPriceStr = request.getParameter("minPrice");
            String maxPriceStr = request.getParameter("maxPrice");
            String sortBy = request.getParameter("sort");

            // Get filtered cars
            List<Car> carList = getFilteredCars(keyword, brandParam, minPriceStr, maxPriceStr);

            // Apply sorting
            if (sortBy != null && !sortBy.isEmpty()) {
                carList = applySorting(carList, sortBy);
                request.setAttribute("sortBy", sortBy);
            }

            // Load brands for filter dropdown
            List<Brand> brandList = brandDAO.getAllBrands();

            // Set attributes for JSP
            request.setAttribute("carList", carList);
            request.setAttribute("brandList", brandList);
            request.setAttribute("totalCars", carList.size());

            // Preserve filter values
            if (isNotEmpty(keyword)) {
                request.setAttribute("searchKeyword", keyword);
            }
            if (isNotEmpty(brandParam)) {
                try {
                    request.setAttribute("selectedBrand", Integer.parseInt(brandParam));
                } catch (NumberFormatException e) {
                    logger.warn("Invalid brand ID: {}", brandParam);
                }
            }
            if (isNotEmpty(minPriceStr)) {
                try {
                    request.setAttribute("minPrice", Double.parseDouble(minPriceStr));
                } catch (NumberFormatException e) {
                    logger.warn("Invalid minPrice: {}", minPriceStr);
                }
            }
            if (isNotEmpty(maxPriceStr)) {
                try {
                    request.setAttribute("maxPrice", Double.parseDouble(maxPriceStr));
                } catch (NumberFormatException e) {
                    logger.warn("Invalid maxPrice: {}", maxPriceStr);
                }
            }

            logger.info("Loaded {} cars", carList.size());

            request.getRequestDispatcher("/WEB-INF/views/Customer/cars.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Error in CarsServlet", e);
            request.setAttribute("error", "Không thể tải danh sách xe. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/WEB-INF/views/Customer/error.jsp").forward(request, response);
        }
    }

    // ============ FILTER LOGIC ============

    private List<Car> getFilteredCars(String keyword, String brandParam,
                                      String minPriceStr, String maxPriceStr) {
        // Priority 1: Search by keyword
        if (isNotEmpty(keyword)) {
            logger.debug("Searching cars with keyword: {}", keyword);
            return carDAO.searchCars(keyword.trim());
        }

        // Priority 2: Filter by brand
        if (isNotEmpty(brandParam)) {
            try {
                int brandId = Integer.parseInt(brandParam);
                logger.debug("Filtering cars by brand: {}", brandId);
                return carDAO.getCarsByBrand(brandId);
            } catch (NumberFormatException e) {
                logger.warn("Invalid brand ID: {}", brandParam);
            }
        }

        // Priority 3: Filter by price range
        if (isNotEmpty(minPriceStr) && isNotEmpty(maxPriceStr)) {
            try {
                double minPrice = Double.parseDouble(minPriceStr);
                double maxPrice = Double.parseDouble(maxPriceStr);

                if (minPrice >= 0 && maxPrice >= minPrice) {
                    logger.debug("Filtering cars by price: {} - {}", minPrice, maxPrice);
                    return carDAO.getCarsByPriceRange(minPrice, maxPrice);
                }
            } catch (NumberFormatException e) {
                logger.warn("Invalid price format: {} - {}", minPriceStr, maxPriceStr);
            }
        }

        // Default: Get all cars
        logger.debug("Loading all cars");
        return carDAO.getAllCars();
    }

    // ============ SORTING LOGIC ============

    private List<Car> applySorting(List<Car> carList, String sortBy) {
        if (carList == null || carList.isEmpty()) {
            return carList;
        }

        Comparator<Car> comparator = switch (sortBy) {
            case "price_asc" -> Comparator.comparingDouble(Car::getPrice);
            case "price_desc" -> Comparator.comparingDouble(Car::getPrice).reversed();
            case "name_asc" -> Comparator.comparing(Car::getName, String.CASE_INSENSITIVE_ORDER);
            case "name_desc" -> Comparator.comparing(Car::getName, String.CASE_INSENSITIVE_ORDER).reversed();
            case "year_desc" -> Comparator.comparingInt(Car::getYear).reversed();
            case "newest" -> Comparator.comparingInt(Car::getId).reversed();
            default -> null;
        };

        if (comparator != null) {
            carList.sort(comparator);
            logger.debug("Sorted by: {}", sortBy);
        }

        return carList;
    }

    // ============ UTILITY METHODS ============

    /**
     * Get first non-empty parameter from multiple parameter names
     */
    private String getParameter(HttpServletRequest request, String... paramNames) {
        for (String paramName : paramNames) {
            String value = request.getParameter(paramName);
            if (isNotEmpty(value)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Check if string is not null and not empty after trim
     */
    private boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}