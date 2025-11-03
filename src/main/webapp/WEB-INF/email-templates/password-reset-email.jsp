<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại mật khẩu - ${appName}</title>
</head>
<body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #0f0f0f;">
<table role="presentation" style="width: 100%; border-collapse: collapse; background-color: #0f0f0f;">
    <tr>
        <td align="center" style="padding: 40px 20px;">
            <table role="presentation" style="width: 100%; max-width: 600px; border-collapse: collapse; background-color: #1a1a1a; border-radius: 20px; overflow: hidden; box-shadow: 0 20px 60px rgba(0,0,0,0.5);">

                <!-- Header with gradient -->
                <tr>
                    <td style="background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%); padding: 0;">
                        <div style="height: 4px; background: linear-gradient(90deg, #ffd700 0%, #ffed4e 50%, #ffd700 100%);"></div>
                    </td>
                </tr>

                <!-- Logo & Title -->
                <tr>
                    <td style="background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%); padding: 40px 40px 30px; text-align: center; border-bottom: 1px solid #333;">
                        <!-- Lock Icon -->
                        <div style="width: 80px; height: 80px; background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%); border-radius: 50%; margin: 0 auto 20px; display: inline-flex; align-items: center; justify-content: center;">
                            <span style="font-size: 40px;"></span>
                        </div>
                        <h1 style="margin: 0; color: #f8f9fa; font-size: 28px; font-weight: 700;">
                            ${appName}
                        </h1>
                        <p style="margin: 10px 0 0; color: #888; font-size: 14px;">
                            Yêu cầu đặt lại mật khẩu
                        </p>
                    </td>
                </tr>

                <!-- Main Content -->
                <tr>
                    <td style="padding: 40px;">
                        <h2 style="margin: 0 0 20px; color: #ffd700; font-size: 24px; font-weight: 700; text-align: center;">
                            Xin chào ${userName}! 👋
                        </h2>

                        <p style="margin: 0 0 20px; color: #e0e0e0; font-size: 16px; line-height: 1.6;">
                            Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn tại
                            <strong style="color: #ffd700;">${appName}</strong>.
                        </p>

                        <p style="margin: 0 0 30px; color: #e0e0e0; font-size: 16px; line-height: 1.6;">
                            Để tiếp tục, vui lòng nhấn vào nút bên dưới:
                        </p>

                        <!-- CTA Button -->
                        <table role="presentation" style="width: 100%; border-collapse: collapse; margin: 0 0 30px 0;">
                            <tr>
                                <td align="center">
                                    <a href="${resetUrl}"
                                       style="display: inline-block; padding: 16px 50px; background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%); color: #1a1a1a; text-decoration: none; font-weight: 700; font-size: 16px; border-radius: 10px; box-shadow: 0 4px 15px rgba(255, 215, 0, 0.3); text-align: center;">
                                        Đặt lại mật khẩu ngay
                                    </a>
                                </td>
                            </tr>
                        </table>

                        <!-- Alternative Link -->
                        <div style="background: #0f0f0f; border: 1px solid #333; border-radius: 10px; padding: 20px; margin: 0 0 25px 0;">
                            <p style="margin: 0 0 10px; color: #888; font-size: 13px;">
                                <strong>Hoặc sao chép link sau:</strong>
                            </p>
                            <a href="${resetUrl}"
                               style="color: #ffd700; text-decoration: none; word-break: break-all; font-size: 12px; line-height: 1.5;">
                                ${resetUrl}
                            </a>
                        </div>

                        <!-- Warning Box -->
                        <table role="presentation" style="width: 100%; border-collapse: collapse; margin: 0 0 20px 0;">
                            <tr>
                                <td style="background: rgba(255, 215, 0, 0.1); border-left: 4px solid #ffd700; border-radius: 5px; padding: 15px;">
                                    <p style="margin: 0; color: #e0e0e0; font-size: 14px; line-height: 1.5;">
                                        <strong style="color: #ffd700;">Link có hiệu lực ${resetExpiryHours} giờ</strong><br>
                                        Sau thời gian này bạn cần yêu cầu link mới.
                                    </p>
                                </td>
                            </tr>
                        </table>

                        <!-- Security Notice -->
                        <table role="presentation" style="width: 100%; border-collapse: collapse; margin: 0 0 20px 0;">
                            <tr>
                                <td style="background: rgba(231, 76, 60, 0.1); border-left: 4px solid #e74c3c; border-radius: 5px; padding: 15px;">
                                    <p style="margin: 0 0 10px; color: #ff6b6b; font-size: 14px; font-weight: 600;">
                                        Bảo mật quan trọng
                                    </p>
                                    <p style="margin: 0; color: #ff6b6b; font-size: 13px; line-height: 1.6;">
                                        • Nếu bạn <strong>không yêu cầu</strong> đặt lại mật khẩu, vui lòng bỏ qua email này.<br>
                                        • Mật khẩu chỉ thay đổi khi bạn truy cập link và xác nhận.<br>
                                        • Nghi ngờ bất thường? Liên hệ ngay với chúng tôi.
                                    </p>
                                </td>
                            </tr>
                        </table>

                        <!-- Tips Box -->
                        <table role="presentation" style="width: 100%; border-collapse: collapse;">
                            <tr>
                                <td style="background: rgba(13, 202, 240, 0.1); border-left: 4px solid #0dcaf0; border-radius: 5px; padding: 15px;">
                                    <p style="margin: 0 0 10px; color: #0dcaf0; font-size: 14px; font-weight: 600;">
                                        💡 Mẹo tạo mật khẩu mạnh
                                    </p>
                                    <p style="margin: 0; color: #888; font-size: 13px; line-height: 1.6;">
                                        • Ít nhất 8 ký tự, kết hợp chữ hoa, thường, số<br>
                                        • Có ký tự đặc biệt (@, #, $, ...)<br>
                                        • Không dùng thông tin cá nhân dễ đoán<br>
                                        • Không tái sử dụng mật khẩu cũ
                                    </p>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <!-- Footer -->
                <tr>
                    <td style="background: #0f0f0f; padding: 30px 40px; border-top: 1px solid #333;">
                        <table role="presentation" style="width: 100%; border-collapse: collapse;">
                            <tr>
                                <td align="center">
                                    <p style="margin: 0 0 15px; color: #888; font-size: 14px; font-weight: 600;">
                                        Cần hỗ trợ? Liên hệ chúng tôi
                                    </p>

                                    <table role="presentation" style="margin: 0 auto 20px auto; border-collapse: collapse;">
                                        <tr>
                                            <td style="padding: 5px 15px;">
                                                <a href="mailto:support@carshowroom.com"
                                                   style="color: #ffd700; text-decoration: none; font-size: 14px;">
                                                    support@carshowroom.com
                                                </a>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="padding: 5px 15px;">
                                                <a href="tel:+84123456789"
                                                   style="color: #ffd700; text-decoration: none; font-size: 14px;">
                                                    +84 123 456 789
                                                </a>
                                            </td>
                                        </tr>
                                    </table>

                                    <!-- Social Links -->
                                    <table role="presentation" style="margin: 20px auto; border-collapse: collapse;">
                                        <tr>
                                            <td style="padding: 0 8px;">
                                                <a href="#" style="text-decoration: none;">
                                                    <img src="https://img.icons8.com/ios-filled/30/888888/facebook-new.png" alt="Facebook" style="width: 28px; height: 28px; display: block;">
                                                </a>
                                            </td>
                                            <td style="padding: 0 8px;">
                                                <a href="#" style="text-decoration: none;">
                                                    <img src="https://img.icons8.com/ios-filled/30/888888/twitter.png" alt="Twitter" style="width: 28px; height: 28px; display: block;">
                                                </a>
                                            </td>
                                            <td style="padding: 0 8px;">
                                                <a href="#" style="text-decoration: none;">
                                                    <img src="https://img.icons8.com/ios-filled/30/888888/instagram-new.png" alt="Instagram" style="width: 28px; height: 28px; display: block;">
                                                </a>
                                            </td>
                                        </tr>
                                    </table>

                                    <div style="margin-top: 20px; padding-top: 20px; border-top: 1px solid #333;">
                                        <p style="margin: 0 0 5px; color: #666; font-size: 12px;">
                                            © 2025 ${appName}. Bảo lưu mọi quyền.
                                        </p>
                                        <p style="margin: 0; color: #666; font-size: 11px;">
                                            Nguyễn Văn Cừ nối dài, Ninh Kiều, TP.Cần Thơ, Việt Nam
                                        </p>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

            </table>

            <!-- Email Client Notice -->
            <table role="presentation" style="width: 100%; max-width: 600px; margin-top: 20px; border-collapse: collapse;">
                <tr>
                    <td align="center" style="padding: 0 20px;">
                        <p style="margin: 0; color: #666; font-size: 11px; line-height: 1.5;">
                            Email này được gửi tự động. Vui lòng không trả lời email này.<br>
                            Nếu bạn gặp vấn đề với nút bấm, hãy sao chép link phía trên.
                        </p>
                    </td>
                </tr>
            </table>

        </td>
    </tr>
</table>
</body>
</html>