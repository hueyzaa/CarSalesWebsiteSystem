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
import java.util.Collections;
import java.util.List;

@WebServlet("/car-detail")
public class CarDetailServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CarDetailServlet.class);
    private static final int MAX_RELATED_CARS = 4;

    private CarDAO carDAO;
    private PromotionService promotionService;

    @Override
    public void init() {
        carDAO = new CarDAO();
        promotionService = new PromotionService();
        logger.info("CarDetailServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Integer carId = getCarId(request);
            if (carId == null) {
                redirectWithError(request, response, "/cars",
                        "Không tìm thấy thông tin xe!");
                return;
            }

            Car car = carDAO.getCarById(carId);
            if (car == null) {
                redirectWithError(request, response, "/cars",
                        "Xe không tồn tại!");
                return;
            }

            setupCarDetailPage(request, response, car);

        } catch (NumberFormatException e) {
            logger.error("Invalid car ID: {}", request.getParameter("id"));
            redirectWithError(request, response, "/cars",
                    "ID xe không hợp lệ!");
        } catch (Exception e) {
            logger.error("Error loading car detail", e);
            forwardToError(request, response,
                    "Không thể tải thông tin xe. Vui lòng thử lại sau!");
        }
    }

    // ============ MAIN LOGIC ============

    private void setupCarDetailPage(HttpServletRequest request, HttpServletResponse response,
                                    Car car) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userId = SessionUtils.getUserId(session);

        // Get promotions and calculate best discount
        List<Promotion> promotions = promotionService.getActivePromotionsForCar(
                car.getId(), userId);
        DiscountInfo discount = calculateBestDiscount(car, promotions);

        // Set attributes
        setCarAttributes(request, car, promotions, discount);
        setUserAttributes(request, session);
        loadRelatedCars(request, car);

        forward(request, response, "/WEB-INF/views/Customer/car-detail.jsp");
    }

    private void setCarAttributes(HttpServletRequest request, Car car,
                                  List<Promotion> promotions, DiscountInfo discount) {
        request.setAttribute("car", car);
        request.setAttribute("activePromotions", promotions);
        request.setAttribute("bestPromotion", discount.bestPromotion);
        request.setAttribute("bestDiscountPercentage", discount.discountPercentage);
        request.setAttribute("bestDiscountAmount", discount.discountAmount);

        if (discount.hasDiscount()) {
            request.setAttribute("discountedPrice", discount.discountedPrice);
            request.setAttribute("savings", discount.savings);
            logger.info("Best discount for car {}: {}% = {} VND off",
                    car.getId(),
                    discount.discountPercentage,
                    String.format("%,.0f", discount.savings));
        }
    }

    private void setUserAttributes(HttpServletRequest request, HttpSession session) {
        request.setAttribute("isLoggedIn", SessionUtils.isLoggedIn(session));
        request.setAttribute("isCustomer", SessionUtils.isCustomer(session));
    }

    // ============ DISCOUNT CALCULATION ============

    private DiscountInfo calculateBestDiscount(Car car, List<Promotion> promotions) {
        DiscountInfo best = new DiscountInfo();

        if (promotions == null || promotions.isEmpty()) {
            return best;
        }

        logger.info("Found {} active promotions for car {}", promotions.size(), car.getId());

        for (Promotion promo : promotions) {
            DiscountInfo current = calculatePromotionDiscount(car, promo);
            if (current.isBetterThan(best, car.getPrice())) {
                best = current;
            }
        }

        return best;
    }

    private DiscountInfo calculatePromotionDiscount(Car car, Promotion promo) {
        try {
            Car carWithPromo = promotionService.getCarWithPromotionInfo(
                    car.getId(), promo.getPromotionId());

            if (carWithPromo == null) {
                return new DiscountInfo();
            }

            double discountPercent = getEffectiveValue(
                    carWithPromo.getDiscountPercentage(),
                    promo.getDiscountPercentage());

            double discountAmount = getEffectiveValue(
                    carWithPromo.getDiscountAmount(),
                    promo.getDiscountAmount());

            return DiscountInfo.create(car.getPrice(), discountPercent,
                    discountAmount, promo);

        } catch (Exception e) {
            logger.error("Error calculating discount for promotion {}",
                    promo.getPromotionId(), e);
            return new DiscountInfo();
        }
    }

    private double getEffectiveValue(double carValue, double promoValue) {
        return carValue > 0 ? carValue : promoValue;
    }

    // ============ RELATED CARS ============

    private void loadRelatedCars(HttpServletRequest request, Car car) {
        try {
            List<Car> relatedCars = carDAO.getCarsByBrand(car.getBrandId());
            relatedCars.removeIf(c -> c.getId() == car.getId());

            if (relatedCars.size() > MAX_RELATED_CARS) {
                relatedCars = relatedCars.subList(0, MAX_RELATED_CARS);
            }

            request.setAttribute("relatedCars", relatedCars);
            logger.debug("Loaded {} related cars", relatedCars.size());

        } catch (Exception e) {
            logger.error("Error loading related cars", e);
            request.setAttribute("relatedCars", Collections.emptyList());
        }
    }

    // ============ UTILITY METHODS ============

    private Integer getCarId(HttpServletRequest request) {
        String param = request.getParameter("id");
        if (param == null || param.trim().isEmpty()) {
            return null;
        }

        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void redirectWithError(HttpServletRequest request, HttpServletResponse response,
                                   String path, String message) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("error", message);
        logger.warn("Redirecting with error: {}", message);
        response.sendRedirect(request.getContextPath() + path);
    }

    @SuppressWarnings("SameParameterValue")
    private void forwardToError(HttpServletRequest request, HttpServletResponse response,
                                String message) throws ServletException, IOException {
        request.setAttribute("error", message);
        forward(request, response, "/WEB-INF/views/Customer/error.jsp");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }

    // ============ INNER CLASS ============

    private static class DiscountInfo {
        double discountPercentage;
        double discountAmount;
        double discountedPrice;
        double savings;
        Promotion bestPromotion;

        static DiscountInfo create(double carPrice, double discountPercent,
                                   double discountAmount, Promotion promo) {
            DiscountInfo info = new DiscountInfo();
            info.discountPercentage = discountPercent;
            info.discountAmount = discountAmount;
            info.bestPromotion = promo;

            if (discountPercent > 0) {
                info.savings = carPrice * (discountPercent / 100);
                info.discountedPrice = carPrice - info.savings;
            } else {
                info.savings = discountAmount;
                info.discountedPrice = carPrice - discountAmount;
            }

            return info;
        }

        boolean hasDiscount() {
            return discountedPrice > 0;
        }

        double getActualSavings(double carPrice) {
            return (discountPercentage > 0)
                    ? carPrice * (discountPercentage / 100)
                    : discountAmount;
        }

        boolean isBetterThan(DiscountInfo other, double carPrice) {
            return this.getActualSavings(carPrice) > other.getActualSavings(carPrice);
        }
    }
}