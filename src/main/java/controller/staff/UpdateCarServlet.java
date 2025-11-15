package controller.staff;

import dao.BrandDAO;
import dao.CarDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Brand;
import model.Car;
import model.CarImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ValidationUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/staff/update-car")
public class UpdateCarServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(controller.staff.UpdateCarServlet.class);
    private CarDAO carDAO;
    private BrandDAO brandDAO;
    @Override
    public void init() {
        carDAO = new CarDAO();
        brandDAO = new BrandDAO();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Car car = carDAO.getCarById(id);

            if (car == null) {
                throw new Exception("Không tìm thấy xe với ID: " + id);
            }

            List<Brand> brandList = brandDAO.getAllBrands();
            request.setAttribute("car", car);
            request.setAttribute("brandList", brandList);

            request.getRequestDispatcher("/WEB-INF/views/Staff/update-car.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Lỗi khi tải form cập nhật xe", e);
            request.setAttribute("error", "Không thể tải thông tin xe. Vui lòng thử lại.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = ValidationUtil.validatePositiveInt(request.getParameter("id"), "ID xe");
            String name = ValidationUtil.validateString(request.getParameter("name"), "Tên xe", 100);
            int brandId = ValidationUtil.validatePositiveInt(request.getParameter("brandId"), "Hãng xe");
            String status = ValidationUtil.validateStatus(request.getParameter("status"));
            String description = request.getParameter("description");
            Integer year = null;
            Integer stock = null;
            String color = request.getParameter("color");

            if (request.getParameter("year") != null && !request.getParameter("year").isEmpty()) {
                year = ValidationUtil.validatePositiveInt(request.getParameter("year"), "Năm sản xuất");
            }
            if (request.getParameter("stock") != null && !request.getParameter("stock").isEmpty()) {
                stock = ValidationUtil.validatePositiveInt(request.getParameter("stock"), "Tồn kho");
            }


            Car car = new Car();
            car.setId(id);
            car.setName(name);
            car.setBrandId(brandId);
            car.setStatus(status);
            car.setDescription(description);
            if (year != null) car.setYear(year);
            if (color != null) car.setColor(color);
            if (stock != null) car.setStock(stock);


            boolean updated = carDAO.updateCar(car);
            if (!updated) {
                throw new Exception("Không thể cập nhật xe, vui lòng thử lại.");
            }


            request.getSession().setAttribute("success", "Cập nhật xe thành công!");
            response.sendRedirect(request.getContextPath() + "/staff/dashboard");

        } catch (Exception e) {
            logger.error("Lỗi không xác định khi cập nhật xe", e);
            handleError(request, response, "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau.");
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Car car = carDAO.getCarById(id);
            List<Brand> brandList = brandDAO.getAllBrands();

            request.setAttribute("car", car);
            request.setAttribute("brandList", brandList);
        } catch (Exception e) {
            logger.warn("Không thể nạp lại thông tin xe khi có lỗi", e);
        }

        request.getRequestDispatcher("/WEB-INF/views/Staff/update-car.jsp").forward(request, response);
    }
}