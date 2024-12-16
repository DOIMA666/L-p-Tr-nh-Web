# 📚 CapitalShop

🌟 CapitalShop là một website bán quần áo trực tuyến được phát triển bởi sinh viên UTE, cung cấp nền tảng mua bán quầnáp với đầy đủ tính năng cho cả người mua và người bán.

## ✨ Tính năng chính

### 👥 Người dùng
- 🔐 Đăng ký, đăng nhập và quản lý tài khoản
- 🔍 Tìm kiếm và duyệt sách theo danh mục
- 🛒 Quản lý giỏ hàng và đặt hàng
- ⭐ Đánh giá và bình luận sách
- 📋 Thanh toán bằng VNPAY

### 🏪 Admin
- 🏢 Quản lý thông tin cửa hàng
- 📚 Quản lý sản phẩm (thêm/sửa/xóa sách)
- 📋 Quản lý đơn hàng và giao dịch
- 📊 Thống kê doanh thu
- 👥 Quản lý người dùng và cửa hàng
- 📚 Quản lý danh mục sách
- 📋 Giám sát giao dịch và đơn hàng

## 🛠️ Công nghệ sử dụng

- **🔙 Backend:** Spring Boot
- **🎨 Frontend:** Thymeleaf, Bootstrap
- **💾 Database:** SQL Server
- **🔄 ORM:** Spring Data JPA
- **⚡ Real-time Communication:** WebSocket
- **☁️ Cloud Storage:** Cloudinary
- **🔒 Security:** Spring Security

## 💻 Yêu cầu hệ thống

- ☕ JDK 17 trở lên
- 🗄️ SQL Server
- 🔨 Maven 3.8+

## 🚀 Cài đặt và Chạy

1. Clone repository:
```bash
git clone https://github.com/your-username/UTEBookStore.git
```

2. Cấu hình database trong `application.properties`:
```properties
server.port=8080
server.tomcat.accesslog.enabled=true
spring.datasource.url=jdbc:sqlserver://localhost;databaseName=OnlineShop_SWP391;encrypt=true;trustServerCertificate=true;
spring.application.name=OnlineShop_SWP391

spring.datasource.username=name
spring.datasource.password=passw```

```

🌐 Truy cập website tại `http://localhost:8080`

## 👥 Tác giả

- 👨‍💻 Lê Quỳnh Nhựt Vinh

---
© 2024 CapitalShop. All rights reserved.
