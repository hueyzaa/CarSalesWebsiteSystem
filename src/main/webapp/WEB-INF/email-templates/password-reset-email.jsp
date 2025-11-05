<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại mật khẩu - ${appName}</title>
</head>
<body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f5f5f5;">
<table role="presentation" style="width: 100%; border-collapse: collapse;">
    <tr>
        <td align="center" style="padding: 30px 15px;">
            <!-- Main Container -->
            <table role="presentation" style="width: 100%; max-width: 500px; border-collapse: collapse; background-color: #1a1a1a; border-radius: 8px; overflow: hidden;">

                <!-- Header -->
                <tr>
                    <td style="background-color: #ffd700; padding: 30px 20px; text-align: center;">
                        <h1 style="margin: 0; color: #000; font-size: 22px; font-weight: bold;">
                            ${appName}
                        </h1>
                    </td>
                </tr>

                <!-- Content -->
                <tr>
                    <td style="padding: 30px 20px;">
                        <h2 style="margin: 0 0 15px; color: #ffd700; font-size: 18px; text-align: center;">
                            Xin chào, ${userName}!
                        </h2>

                        <p style="margin: 0 0 25px; color: #ccc; font-size: 14px; line-height: 1.5; text-align: center;">
                            Bạn đã yêu cầu đặt lại mật khẩu. Nhấn nút bên dưới để tiếp tục:
                        </p>

                        <!-- Button -->
                        <div style="text-align: center; margin-bottom: 25px;">
                            <a href="${resetUrl}"
                               style="display: inline-block; padding: 12px 30px; background-color: #ffd700; color: #000; text-decoration: none; font-weight: bold; font-size: 14px; border-radius: 5px;">
                                Đặt lại mật khẩu
                            </a>
                        </div>

                        <!-- Link -->
                        <div style="background-color: #0f0f0f; border-radius: 5px; padding: 15px; margin-bottom: 20px;">
                            <p style="margin: 0 0 8px; color: #888; font-size: 12px;">
                                Hoặc sao chép link:
                            </p>
                            <a href="${resetUrl}"
                               style="color: #ffd700; text-decoration: none; word-break: break-all; font-size: 11px;">
                                ${resetUrl}
                            </a>
                        </div>

                        <!-- Info -->
                        <p style="margin: 0 0 15px; color: #999; font-size: 12px; text-align: center;">
                            Link có hiệu lực trong ${resetExpiryHours} giờ
                        </p>

                        <p style="margin: 0; color: #ff6b6b; font-size: 12px; text-align: center;">
                            Nếu bạn không yêu cầu, hãy bỏ qua email này
                        </p>
                    </td>
                </tr>

                <!-- Footer -->
                <tr>
                    <td style="background-color: #0f0f0f; padding: 20px; text-align: center; border-top: 1px solid #333;">
                        <p style="margin: 0 0 10px; color: #888; font-size: 12px;">
                            info@carshowroom.com | 0123 456 789
                        </p>
                        <p style="margin: 0; color: #666; font-size: 11px;">
                            © 2025 ${appName}
                        </p>
                    </td>
                </tr>

            </table>
        </td>
    </tr>
</table>
</body>
</html>