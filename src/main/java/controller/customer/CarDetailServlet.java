package controller.customer;

import dao.CarDAO;
import service.PromotionService;
import model.Car;
import model.Promotion;
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
import java.util.List;

/**
 * CarDetailServlet - Display detailed car information
 * Accessible by everyone (Guest/Customer/Staff/Admin)
 * Shows personalized promotions for logged-in customers
 */
@WebServlet("/car-detail")
public class CarDetailServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CarDetailServlet.class);
    private CarDAO carDAO;
    private PromotionService promotionService;

    @Override
    public void init() throws ServletException {
        super.init();
        carDAO = new CarDAO();
        promotionService = new PromotionService();
        logger.info("CarDetailServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idParam = request.getParameter("id");

            if (idParam == null || idParam.trim().isEmpty()) {
                logger.warn("Car ID parameter missing");
                redirectWithError(request, response, "/cars", "Không tìm thấy thông tin xe!");
                return;
            }

            int carId = Integer.parseInt(idParam);
            logger.info("Loading car detail for car ID: {}", carId);

            Car car = carDAO.getCarById(carId);

            if (car == null) {
                logger.warn("Car not found with ID: {}", carId);
                redirectWithError(request, response, "/cars", "Xe không tồn tại!");
                return;
            }

            HttpSession session = request.getSession(false);
            Integer userId = SessionUtils.getUserId(session);
            boolean isCustomer = SessionUtils.isCustomer(session);
            boolean isLoggedIn = SessionUtils.isLoggedIn(session);

            logger.debug("User: {}, IsCustomer: {}", isLoggedIn ? userId : "Guest", isCustomer);

            // Get promotions and calculate best discount
            List<Promotion> activePromotions = promotionService.getActivePromotionsForCar(carId, userId);
            DiscountInfo discountInfo = calculateBestDiscount(car, activePromotions);

            // Set car and promotion attributes
            request.setAttribute("car", car);
            request.setAttribute("activePromotions", activePromotions);
            request.setAttribute("bestPromotion", discountInfo.bestPromotion);
            request.setAttribute("bestDiscountPercentage", discountInfo.discountPercentage);
            request.setAttribute("bestDiscountAmount", discountInfo.discountAmount);
            request.setAttribute("isLoggedIn", isLoggedIn);
            request.setAttribute("isCustomer", isCustomer);

            // Set discounted price if applicable
            if (discountInfo.discountedPrice > 0) {
                request.setAttribute("discountedPrice", discountInfo.discountedPrice);
                request.setAttribute("savings", discountInfo.savings);
                logger.info("Best discount for car {}: {}% = {:,.0f}₫ off",
                        carId, discountInfo.discountPercentage, discountInfo.savings);
            }

            // Load related cars
            loadRelatedCars(request, car, carId);

            request.getRequestDispatcher("/WEB-INF/views/car-detail.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            logger.error("Invalid car ID format: {}", request.getParameter("id"), e);
            redirectWithError(request, response, "/cars", "ID xe không hợp lệ!");
        } catch (RuntimeException e) {
            logger.error("Database error loading car detail", e);
            handleError(request, response, "Không thể tải thông tin xe. Vui lòng thử lại sau!");
        } catch (Exception e) {
            logger.error("Unexpected error loading car detail", e);
            handleError(request, response, "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau!");
        }
    }

    /**
     * Calculate best discount from available promotions
     */
    private DiscountInfo calculateBestDiscount(Car car, List<Promotion> activePromotions) {
        DiscountInfo info = new DiscountInfo();

        if (activePromotions == null || activePromotions.isEmpty()) {
            logger.debug("No active promotions for car {}", car.getId());
            return info;
        }

        logger.info("Found {} active promotions for car {}", activePromotions.size(), car.getId());

        for (Promotion promo : activePromotions) {
            try {
                Car carWithDiscount = promotionService.getCarWithPromotionInfo(
                        car.getId(), promo.getPromotionId());

                if (carWithDiscount == null) continue;

                double discountPercent = carWithDiscount.getDiscountPercentage() > 0
                        ? carWithDiscount.getDiscountPercentage()
                        : promo.getDiscountPercentage();

                double discountAmount = carWithDiscount.getDiscountAmount() > 0
                        ? carWithDiscount.getDiscountAmount()
                        : promo.getDiscountAmount();

                // Calculate actual discount value
                double actualDiscount = (discountPercent > 0)
                        ? car.getPrice() * (discountPercent / 100)
                        : discountAmount;

                double currentBest = (info.discountPercentage > 0)
                        ? car.getPrice() * (info.discountPercentage / 100)
                        : info.discountAmount;

                // Update if this is better
                if (actualDiscount > currentBest) {
                    info.discountPercentage = discountPercent;
                    info.discountAmount = discountAmount;
                    info.bestPromotion = promo;

                    if (discountPercent > 0) {
                        info.discountedPrice = car.getPrice() * (1 - discountPercent / 100);
                        info.savings = car.getPrice() - info.discountedPrice;
                    } else {
                        info.discountedPrice = car.getPrice() - discountAmount;
                        info.savings = discountAmount;
                    }
                }
            } catch (Exception e) {
                logger.error("Error calculating discount for promotion {}",
                        promo.getPromotionId(), e);
            }
        }

        return info;
    }

    /**
     * Load related cars (same brand)
     */
    private void loadRelatedCars(HttpServletRequest request, Car car, int currentCarId) {
        try {
            List<Car> relatedCars = carDAO.getCarsByBrand(car.getBrandId());

            // Remove current car
            relatedCars.removeIf(c -> c.getId() == currentCarId);

            // Limit to 4
            if (relatedCars.size() > 4) {
                relatedCars = relatedCars.subList(0, 4);
            }

            request.setAttribute("relatedCars", relatedCars);
            logger.debug("Loaded {} related cars for car {}", relatedCars.size(), currentCarId);

        } catch (Exception e) {
            logger.error("Error loading related cars", e);
            request.setAttribute("relatedCars", null);
        }
    }

    /**
     * Redirect with error message
     */
    private void redirectWithError(HttpServletRequest request, HttpServletResponse response,
                                   String path, String errorMessage) throws IOException {
        request.getSession().setAttribute("error", errorMessage);
        response.sendRedirect(request.getContextPath() + path);
    }

    /**
     * Handle error and forward to error page
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String errorMessage) throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                .forward(request, response);
    }

    /**
     * Helper class to hold discount information
     */
    private static class DiscountInfo {
        double discountPercentage = 0;
        double discountAmount = 0;
        double discountedPrice = 0;
        double savings = 0;
        Promotion bestPromotion = null;
    }

    @Override
    public void destroy() {
        super.destroy();
        logger.info("CarDetailServlet destroyed");
    }
}