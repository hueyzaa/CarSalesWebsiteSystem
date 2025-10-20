package controller.customer;

import dao.CarDAO;
import service.PromotionService;
import model.Car;
import model.Promotion;
import model.User;
import exception.DatabaseException;
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

@WebServlet("/car-detail")
public class CarDetailServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CarDetailServlet.class);
    private CarDAO carDAO;
    private PromotionService promotionService;

    @Override
    public void init() throws ServletException {
        carDAO = new CarDAO();
        promotionService = new PromotionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get car ID from parameter
            String idParam = request.getParameter("id");

            if (idParam == null || idParam.trim().isEmpty()) {
                logger.warn("Car ID parameter is missing");
                request.getSession().setAttribute("error", "Không tìm thấy thông tin xe!");
                response.sendRedirect(request.getContextPath() + "/cars");
                return;
            }

            int carId = Integer.parseInt(idParam);
            logger.info("Loading car detail for car ID: {}", carId);

            // Get car details
            Car car = carDAO.getCarById(carId);

            if (car == null) {
                logger.warn("Car not found with ID: {}", carId);
                request.getSession().setAttribute("error", "Xe không tồn tại!");
                response.sendRedirect(request.getContextPath() + "/cars");
                return;
            }

            // Get current user
            HttpSession session = request.getSession(false);
            User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
            Integer userId = (currentUser != null) ? currentUser.getUserId() : null;

            logger.debug("User logged in: {}", userId != null);

            // Get active promotions for this car
            List<Promotion> activePromotions = promotionService.getActivePromotionsForCar(carId, userId);

            logger.info("Found {} active promotions for car {}", activePromotions.size(), carId);

            // Calculate best discount available
            double bestDiscountPercentage = 0;
            double bestDiscountAmount = 0;
            Promotion bestPromotion = null;

            for (Promotion promo : activePromotions) {
                try {
                    Car carWithDiscount = promotionService.getCarWithPromotionInfo(carId, promo.getPromotionId());
                    if (carWithDiscount != null) {
                        double discountPercent = carWithDiscount.getDiscountPercentage() > 0
                                ? carWithDiscount.getDiscountPercentage()
                                : promo.getDiscountPercentage();
                        double discountAmount = carWithDiscount.getDiscountAmount() > 0
                                ? carWithDiscount.getDiscountAmount()
                                : promo.getDiscountAmount();

                        // Calculate actual discount value for comparison
                        double actualDiscount = 0;
                        if (discountPercent > 0) {
                            actualDiscount = car.getPrice() * (discountPercent / 100);
                        } else {
                            actualDiscount = discountAmount;
                        }

                        double currentBest = 0;
                        if (bestDiscountPercentage > 0) {
                            currentBest = car.getPrice() * (bestDiscountPercentage / 100);
                        } else {
                            currentBest = bestDiscountAmount;
                        }

                        if (actualDiscount > currentBest) {
                            bestDiscountPercentage = discountPercent;
                            bestDiscountAmount = discountAmount;
                            bestPromotion = promo;
                        }
                    }
                } catch (DatabaseException e) {
                    logger.error("Error getting car discount info", e);
                }
            }

            // Set attributes
            request.setAttribute("car", car);
            request.setAttribute("activePromotions", activePromotions);
            request.setAttribute("bestPromotion", bestPromotion);
            request.setAttribute("bestDiscountPercentage", bestDiscountPercentage);
            request.setAttribute("bestDiscountAmount", bestDiscountAmount);

            // Calculate discounted price if there's a promotion
            if (bestDiscountPercentage > 0) {
                double discountedPrice = car.getPrice() * (1 - bestDiscountPercentage / 100);
                request.setAttribute("discountedPrice", discountedPrice);
                request.setAttribute("savings", car.getPrice() - discountedPrice);
                logger.info("Best discount: {}% = {}₫ off", bestDiscountPercentage, car.getPrice() - discountedPrice);
            } else if (bestDiscountAmount > 0) {
                double discountedPrice = car.getPrice() - bestDiscountAmount;
                request.setAttribute("discountedPrice", discountedPrice);
                request.setAttribute("savings", bestDiscountAmount);
                logger.info("Best discount: {}₫ off", bestDiscountAmount);
            }

            // Forward to car detail JSP
            request.getRequestDispatcher("/WEB-INF/views/car-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            logger.error("Invalid car ID format", e);
            request.getSession().setAttribute("error", "ID xe không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/cars");

        } catch (DatabaseException e) {
            logger.error("Database error loading car detail", e);
            request.setAttribute("error", "Không thể tải thông tin xe!");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Unexpected error loading car detail", e);
            request.setAttribute("error", "Đã xảy ra lỗi không mong muốn!");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}