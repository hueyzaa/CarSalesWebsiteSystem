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
            FilterParams params = extractFilterParams(request);

            // Get filtered cars
            List<Car> carList = getFilteredCars(params, request);

            // Apply sorting
            if (params.sortBy != null && !params.sortBy.isEmpty()) {
                carList = applySorting(carList, params.sortBy);
                request.setAttribute("sortBy", params.sortBy);
            }

            // Load brands for filter
            List<Brand> brandList = brandDAO.getAllBrands();

            // Set attributes
            setCarListAttributes(request, carList, brandList);
            setUserAttributes(request);

            logger.info("Loaded {} cars with filters - keyword: {}, brand: {}, sort: {}",
                    carList.size(), params.keyword, params.brandId, params.sortBy);

            forward(request, response, "/WEB-INF/views/cars.jsp");

        } catch (RuntimeException e) {
            logger.error("Database error in CarsServlet", e);
            forwardToError(request, response, "Không thể tải danh sách xe. Vui lòng thử lại sau.");
        } catch (Exception e) {
            logger.error("Unexpected error in CarsServlet", e);
            forwardToError(request, response, "Đã xảy ra lỗi không mong muốn.");
        }
    }

    // ============ FILTER LOGIC ============

    private FilterParams extractFilterParams(HttpServletRequest request) {
        FilterParams params = new FilterParams();

        String searchKeyword = request.getParameter("search");
        String keyword = request.getParameter("keyword");
        params.keyword = searchKeyword != null ? searchKeyword : keyword;

        params.brandParam = request.getParameter("brand");
        params.minPriceStr = request.getParameter("minPrice");
        params.maxPriceStr = request.getParameter("maxPrice");
        params.sortBy = request.getParameter("sort");

        return params;
    }

    private List<Car> getFilteredCars(FilterParams params, HttpServletRequest request) {
        // Search by keyword
        if (isNotEmpty(params.keyword)) {
            request.setAttribute("searchKeyword", params.keyword.trim());
            logger.debug("Searching cars with keyword: {}", params.keyword);
            return carDAO.searchCars(params.keyword.trim());
        }

        // Filter by brand
        if (isNotEmpty(params.brandParam)) {
            try {
                params.brandId = Integer.parseInt(params.brandParam);
                request.setAttribute("selectedBrand", params.brandId);
                logger.debug("Filtering cars by brand ID: {}", params.brandId);
                return carDAO.getCarsByBrand(params.brandId);
            } catch (NumberFormatException e) {
                logger.warn("Invalid brand ID format: {}", params.brandParam);
            }
        }

        // Filter by price range
        if (isNotEmpty(params.minPriceStr) && isNotEmpty(params.maxPriceStr)) {
            try {
                double minPrice = Double.parseDouble(params.minPriceStr);
                double maxPrice = Double.parseDouble(params.maxPriceStr);

                if (minPrice >= 0 && maxPrice >= minPrice) {
                    request.setAttribute("minPrice", minPrice);
                    request.setAttribute("maxPrice", maxPrice);
                    logger.debug("Filtering cars by price range: {} - {}", minPrice, maxPrice);
                    return carDAO.getCarsByPriceRange(minPrice, maxPrice);
                } else {
                    logger.warn("Invalid price range: {} - {}", minPrice, maxPrice);
                }
            } catch (NumberFormatException e) {
                logger.warn("Invalid price format: {} - {}", params.minPriceStr, params.maxPriceStr);
            }
        }

        // Default: get all cars
        logger.debug("Loading all cars");
        return carDAO.getAllCars();
    }

    // ============ SORTING LOGIC ============

    private List<Car> applySorting(List<Car> carList, String sortBy) {
        if (carList == null || carList.isEmpty()) {
            return carList;
        }

        Comparator<Car> comparator = getComparator(sortBy);
        if (comparator != null) {
            carList.sort(comparator);
            logger.debug("Sorted by: {}", sortBy);
        } else {
            logger.debug("Unknown sort type: {}", sortBy);
        }

        return carList;
    }

    private Comparator<Car> getComparator(String sortBy) {
        return switch (sortBy) {
            case "price_asc" -> Comparator.comparingDouble(Car::getPrice);
            case "price_desc" -> Comparator.comparingDouble(Car::getPrice).reversed();
            case "name_asc" -> Comparator.comparing(Car::getName, String.CASE_INSENSITIVE_ORDER);
            case "name_desc" -> Comparator.comparing(Car::getName, String.CASE_INSENSITIVE_ORDER).reversed();
            case "year_desc" -> Comparator.comparingInt(Car::getYear).reversed();
            case "newest" -> Comparator.comparingInt(Car::getId).reversed();
            default -> null;
        };
    }

    // ============ ATTRIBUTE SETTERS ============

    private void setCarListAttributes(HttpServletRequest request, List<Car> carList,
                                      List<Brand> brandList) {
        request.setAttribute("carList", carList);
        request.setAttribute("brandList", brandList);
        request.setAttribute("totalCars", carList.size());
    }

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

    // ============ UTILITY METHODS ============

    private boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }

    @SuppressWarnings("SameParameterValue")
    private void forwardToError(HttpServletRequest request, HttpServletResponse response,
                                String message) throws ServletException, IOException {
        request.setAttribute("error", message);
        forward(request, response, "/WEB-INF/views/error.jsp");
    }

    // ============ INNER CLASS ============

    private static class FilterParams {
        String keyword;
        String brandParam;
        Integer brandId;
        String minPriceStr;
        String maxPriceStr;
        String sortBy;
    }
}