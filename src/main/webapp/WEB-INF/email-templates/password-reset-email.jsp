<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại mật khẩu - ${appName}</title>
</head>
<body style="margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; background: linear-gradient(135deg, #0a0a0a 0%, #1a1a1a 100%);">
<table role="presentation" style="width: 100%; border-collapse: collapse;">
    <tr>
        <td align="center" style="padding: 50px 20px;">
            <!-- Main Container -->
            <table role="presentation" style="width: 100%; max-width: 600px; border-collapse: collapse; background-color: #1c1c1c; border-radius: 16px; overflow: hidden; box-shadow: 0 8px 32px rgba(0,0,0,0.4);">

                <!-- Header Accent -->
                <tr>
                    <td style="padding: 0;">
                        <div style="height: 5px; background: linear-gradient(90deg, #ffd700 0%, #ffed4e 50%, #ffd700 100%);"></div>
                    </td>
                </tr>

                <!-- Logo Section -->
                <tr>
                    <td style="background: #1c1c1c; padding: 48px 40px 36px; text-align: center;">
                        <div style="width: 72px; height: 72px; background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%); border-radius: 50%; margin: 0 auto 24px; display: inline-flex; align-items: center; justify-content: center; box-shadow: 0 4px 20px rgba(255, 215, 0, 0.25);">
                            <svg width="36" height="36" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <rect x="5" y="11" width="14" height="10" rx="2" stroke="#1a1a1a" stroke-width="2"/>
                                <path d="M8 11V7C8 4.79086 9.79086 3 12 3C14.2091 3 16 4.79086 16 7V11" stroke="#1a1a1a" stroke-width="2" stroke-linecap="round"/>
                                <circle cx="12" cy="16" r="1.5" fill="#1a1a1a"/>
                            </svg>
                        </div>
                        <h1 style="margin: 0; color: #ffffff; font-size: 26px; font-weight: 700; letter-spacing: -0.5px;">
                            ${appName}
                        </h1>
                        <p style="margin: 12px 0 0; color: #999; font-size: 14px; font-weight: 500;">
                            Yêu cầu đặt lại mật khẩu
                        </p>
                    </td>
                </tr>

                <!-- Content Section -->
                <tr>
                    <td style="padding: 0 40px 48px;">
                        <h2 style="margin: 0 0 24px; color: #ffd700; font-size: 22px; font-weight: 700; text-align: center; letter-spacing: -0.3px;">
                            Xin chào, ${userName}! 👋
                        </h2>

                        <p style="margin: 0 0 18px; color: #d1d1d1; font-size: 15px; line-height: 1.7;">
                            Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn tại <strong style="color: #ffd700;">${appName}</strong>.
                        </p>

                        <p style="margin: 0 0 32px; color: #d1d1d1; font-size: 15px; line-height: 1.7;">
                            Để tiếp tục, vui lòng nhấn vào nút bên dưới:
                        </p>

                        <!-- CTA Button -->
                        <div style="text-align: center; margin: 36px 0;">
                            <a href="${resetUrl}"
                               style="display: inline-block; padding: 16px 48px; background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%); color: #1a1a1a; text-decoration: none; font-weight: 700; font-size: 16px; border-radius: 8px; box-shadow: 0 4px 16px rgba(255, 215, 0, 0.3); transition: all 0.3s ease;">
                                🔐 Đặt lại mật khẩu
                            </a>
                        </div>

                        <!-- Alternative Link Box -->
                        <div style="background: #141414; border: 1px solid #2a2a2a; border-radius: 8px; padding: 20px; margin: 32px 0;">
                            <p style="margin: 0 0 12px; color: #999; font-size: 13px; font-weight: 500;">
                                Hoặc sao chép link sau vào trình duyệt:
                            </p>
                            <a href="${resetUrl}"
                               style="color: #ffd700; text-decoration: none; word-break: break-all; font-size: 12px; line-height: 1.6;">
                                ${resetUrl}
                            </a>
                        </div>

                        <!-- Expiry Notice -->
                        <div style="background: rgba(255, 215, 0, 0.08); border-left: 3px solid #ffd700; border-radius: 6px; padding: 16px 20px; margin: 24px 0;">
                            <p style="margin: 0; color: #e0e0e0; font-size: 14px; line-height: 1.6;">
                                <strong style="color: #ffd700;">⏰ Thời hạn:</strong> Link này có hiệu lực trong <strong>${resetExpiryHours} giờ</strong>. Sau đó bạn cần yêu cầu link mới.
                            </p>
                        </div>

                        <!-- Security Alert -->
                        <div style="background: rgba(239, 68, 68, 0.08); border-left: 3px solid #ef4444; border-radius: 6px; padding: 18px 20px; margin: 24px 0;">
                            <p style="margin: 0 0 12px; color: #ef4444; font-size: 14px; font-weight: 700;">
                                🔒 Bảo mật quan trọng
                            </p>
                            <ul style="margin: 0; padding-left: 20px; color: #fca5a5; font-size: 13px; line-height: 1.8;">
                                <li style="margin-bottom: 6px;">Nếu bạn <strong>không yêu cầu</strong> đặt lại mật khẩu, hãy bỏ qua email này</li>
                                <li style="margin-bottom: 6px;">Mật khẩu chỉ thay đổi khi bạn xác nhận qua link</li>
                                <li style="margin-bottom: 0;">Nghi ngờ bất thường? Liên hệ ngay với chúng tôi</li>
                            </ul>
                        </div>

                        <!-- Password Tips -->
                        <div style="background: rgba(59, 130, 246, 0.08); border-left: 3px solid #3b82f6; border-radius: 6px; padding: 18px 20px; margin: 24px 0;">
                            <p style="margin: 0 0 12px; color: #60a5fa; font-size: 14px; font-weight: 700;">
                                💡 Mẹo tạo mật khẩu mạnh
                            </p>
                            <ul style="margin: 0; padding-left: 20px; color: #93c5fd; font-size: 13px; line-height: 1.8;">
                                <li style="margin-bottom: 6px;">Ít nhất 8 ký tự, kết hợp chữ hoa, chữ thường và số</li>
                                <li style="margin-bottom: 6px;">Sử dụng ký tự đặc biệt (@, #, $, !, ...)</li>
                                <li style="margin-bottom: 6px;">Tránh thông tin cá nhân dễ đoán</li>
                                <li style="margin-bottom: 0;">Không tái sử dụng mật khẩu cũ</li>
                            </ul>
                        </div>
                    </td>
                </tr>

                <!-- Footer -->
                <tr>
                    <td style="background: #141414; padding: 40px 40px 32px; border-top: 1px solid #2a2a2a;">
                        <div style="text-align: center;">
                            <p style="margin: 0 0 20px; color: #999; font-size: 14px; font-weight: 600;">
                                Cần hỗ trợ? Liên hệ với chúng tôi
                            </p>

                            <div style="margin-bottom: 24px;">
                                <p style="margin: 0 0 8px;">
                                    <a href="mailto:info@carshowroom.com"
                                       style="color: #ffd700; text-decoration: none; font-size: 14px; font-weight: 500;">
                                        📧 info@carshowroom.com
                                    </a>
                                </p>
                                <p style="margin: 0;">
                                    <a href="tel:0123456789"
                                       style="color: #ffd700; text-decoration: none; font-size: 14px; font-weight: 500;">
                                        📞 0123 456 789
                                    </a>
                                </p>
                            </div>

                            <!-- Social Icons -->
                            <div style="margin: 28px 0 24px;">
                                <a href="#" style="display: inline-block; margin: 0 8px; opacity: 0.7; transition: opacity 0.3s;">
                                    <img src="https://img.icons8.com/fluency/48/facebook-new.png" alt="Facebook" style="width: 32px; height: 32px; display: block;">
                                </a>
                                <a href="#" style="display: inline-block; margin: 0 8px; opacity: 0.7; transition: opacity 0.3s;">
                                    <img src="https://img.icons8.com/fluency/48/instagram-new.png" alt="Instagram" style="width: 32px; height: 32px; display: block;">
                                </a>
                                <a href="#" style="display: inline-block; margin: 0 8px; opacity: 0.7; transition: opacity 0.3s;">
                                    <img src="https://img.icons8.com/fluency/48/youtube-play.png" alt="YouTube" style="width: 32px; height: 32px; display: block;">
                                </a>
                            </div>

                            <div style="padding-top: 24px; border-top: 1px solid #2a2a2a;">
                                <p style="margin: 0 0 8px; color: #666; font-size: 12px; font-weight: 500;">
                                    © 2025 ${appName}. Bảo lưu mọi quyền.
                                </p>
                                <p style="margin: 0; color: #555; font-size: 11px; line-height: 1.6;">
                                    📍 Cần Thơ, Việt Nam
                                </p>
                            </div>
                        </div>
                    </td>
                </tr>

            </table>

            <!-- Disclaimer -->
            <table role="presentation" style="width: 100%; max-width: 600px; margin-top: 24px;">
                <tr>
                    <td style="text-align: center; padding: 0 20px;">
                        <p style="margin: 0; color: #666; font-size: 11px; line-height: 1.6;">
                            Email này được gửi tự động từ hệ thống. Vui lòng không trả lời trực tiếp email này.<br>
                            Nếu bạn gặp vấn đề với nút bấm, hãy sao chép link phía trên vào trình duyệt.
                        </p>
                    </td>
                </tr>
            </table>

        </td>
    </tr>
</table>
</body>
</html>
