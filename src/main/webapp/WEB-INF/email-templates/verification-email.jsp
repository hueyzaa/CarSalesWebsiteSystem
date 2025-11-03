<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác thực Email - ${appName}</title>
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
                        <div style="width: 80px; height: 80px; background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%); border-radius: 50%; margin: 0 auto 20px; display: flex; align-items: center; justify-content: center;">
                            <svg width="40" height="40" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M12 2L2 7L12 12L22 7L12 2Z" fill="#1a1a1a" stroke="#1a1a1a" stroke-width="2"/>
                                <path d="M2 17L12 22L22 17" stroke="#1a1a1a" stroke-width="2" stroke-linecap="round"/>
                                <path d="M2 12L12 17L22 12" stroke="#1a1a1a" stroke-width="2" stroke-linecap="round"/>
                            </svg>
                        </div>
                        <h1 style="margin: 0; color: #f8f9fa; font-size: 28px; font-weight: 700;">
                            ${appName}
                        </h1>
                        <p style="margin: 10px 0 0; color: #888; font-size: 14px;">
                            Xác thực tài khoản của bạn
                        </p>
                    </td>
                </tr>

                <!-- Main Content -->
                <tr>
                    <td style="padding: 40px;">
                        <h2 style="margin: 0 0 20px; color: #ffd700; font-size: 24px; font-weight: 700; text-align: center;">
                            Xin chào, ${userName}!
                        </h2>

                        <p style="margin: 0 0 20px; color: #e0e0e0; font-size: 16px; line-height: 1.6;">
                            Cảm ơn bạn đã đăng ký tài khoản tại <strong style="color: #ffd700;">${appName}</strong>.
                            Để hoàn tất quá trình đăng ký, vui lòng xác thực địa chỉ email của bạn bằng cách nhấn vào nút bên dưới:
                        </p>

                        <!-- CTA Button -->
                        <table role="presentation" style="width: 100%; border-collapse: collapse; margin: 30px 0;">
                            <tr>
                                <td align="center">
                                    <a href="${verificationUrl}"
                                       style="display: inline-block; padding: 15px 40px; background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%); color: #1a1a1a; text-decoration: none; font-weight: 600; font-size: 16px; border-radius: 10px; box-shadow: 0 4px 15px rgba(255, 215, 0, 0.3);">
                                        Xác thực Email
                                    </a>
                                </td>
                            </tr>
                        </table>

                        <!-- Alternative Link -->
                        <div style="background: #0f0f0f; border: 1px solid #333; border-radius: 10px; padding: 20px; margin: 30px 0;">
                            <p style="margin: 0 0 10px; color: #888; font-size: 14px;">
                                Nếu nút không hoạt động, vui lòng sao chép và dán link sau vào trình duyệt:
                            </p>
                            <a href="${verificationUrl}"
                               style="color: #ffd700; text-decoration: none; word-break: break-all; font-size: 13px;">
                                ${verificationUrl}
                            </a>
                        </div>

                        <!-- Warning Box -->
                        <div style="background: rgba(255, 215, 0, 0.1); border-left: 4px solid #ffd700; border-radius: 5px; padding: 15px; margin: 20px 0;">
                            <p style="margin: 0; color: #e0e0e0; font-size: 14px;">
                                <strong>Lưu ý:</strong> Link xác thực này có hiệu lực trong <strong>${tokenExpiryHours} giờ</strong>.
                                Sau thời gian này, bạn sẽ cần yêu cầu gửi lại email xác thực.
                            </p>
                        </div>

                        <!-- Security Notice -->
                        <div style="background: rgba(231, 76, 60, 0.1); border-left: 4px solid #e74c3c; border-radius: 5px; padding: 15px; margin: 20px 0;">
                            <p style="margin: 0; color: #ff6b6b; font-size: 13px;">
                                🔒 <strong>Bảo mật:</strong> Nếu bạn không thực hiện đăng ký tài khoản này,
                                vui lòng bỏ qua email này hoặc liên hệ với chúng tôi ngay lập tức.
                            </p>
                        </div>
                    </td>
                </tr>

                <!-- Footer -->
                <tr>
                    <td style="background: #0f0f0f; padding: 30px 40px; border-top: 1px solid #333;">
                        <table role="presentation" style="width: 100%; border-collapse: collapse;">
                            <tr>
                                <td align="center">
                                    <p style="margin: 0 0 15px; color: #888; font-size: 14px;">
                                        Cần hỗ trợ? Liên hệ với chúng tôi:
                                    </p>
                                    <p style="margin: 0 0 10px;">
                                        <a href="mailto:support@carshowroom.com"
                                           style="color: #ffd700; text-decoration: none; font-size: 14px;">
                                            support@carshowroom.com
                                        </a>
                                    </p>
                                    <p style="margin: 0 0 20px;">
                                        <a href="tel:+84123456789"
                                           style="color: #ffd700; text-decoration: none; font-size: 14px;">
                                            +84 123 456 789
                                        </a>
                                    </p>

                                    <!-- Social Links -->
                                    <div style="margin: 20px 0;">
                                        <a href="#" style="display: inline-block; margin: 0 10px; color: #888; text-decoration: none;">
                                            <img src="https://img.icons8.com/ios-filled/30/888888/facebook-new.png" alt="Facebook" style="width: 30px; height: 30px;">
                                        </a>
                                        <a href="#" style="display: inline-block; margin: 0 10px; color: #888; text-decoration: none;">
                                            <img src="https://img.icons8.com/ios-filled/30/888888/twitter.png" alt="Twitter" style="width: 30px; height: 30px;">
                                        </a>
                                        <a href="#" style="display: inline-block; margin: 0 10px; color: #888; text-decoration: none;">
                                            <img src="https://img.icons8.com/ios-filled/30/888888/instagram-new.png" alt="Instagram" style="width: 30px; height: 30px;">
                                        </a>
                                    </div>

                                    <p style="margin: 15px 0 0; color: #666; font-size: 12px;">
                                        © 2025 ${appName}. Bảo lưu mọi quyền.
                                    </p>
                                    <p style="margin: 5px 0 0; color: #666; font-size: 11px;">
                                        123 Đường ABC, Quận XYZ, TP.HCM, Việt Nam
                                    </p>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

            </table>
        </td>
    </tr>
</table>
</body>
</html>