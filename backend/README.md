# CloudVault - Cloud Storage Management System

## 🚀 Quick Start với Docker

### Prerequisites
- Docker Desktop đã cài đặt và đang chạy
- File `credentials.json` (Google Drive API) đã có trong `src/main/resources/`

### 1. Khởi động với Docker (Recommended)
```bash
# Cách 1: Sử dụng script tự động
start-docker.bat

# Cách 2: Sử dụng docker-compose
docker-compose up --build -d

# Cách 3: Build và run thủ công
docker build -t cloudvault-api .
docker run -p 8080:8080 cloudvault-api
```

### 2. Truy cập ứng dụng
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health
- **MySQL**: localhost:3306 (user: cloudvault, pass: cloudvault123)

### 3. Đăng nhập Admin mặc định
- **Username**: `admin`
- **Password**: `hoangadmin@11`

### 4. Quản lý Docker
```bash
# Xem logs
docker-compose logs -f cloudvault-app

# Restart services
docker-compose restart

# Stop services
docker-compose down

# Stop và xóa data (cẩn thận!)
docker-compose down -v
```

## 📋 Tính năng chính

✅ **Authentication & Authorization**
- JWT-based authentication
- Role-based access control (USER/ADMIN)
- Tự động tạo admin mặc định

✅ **File Management**
- Upload file lên Google Drive
- Quản lý file cá nhân
- Phân quyền truy cập nghiêm ngặt
- Storage quota management

✅ **Admin Dashboard**
- Thống kê dung lượng sử dụng
- Quản lý user và file
- Xem báo cáo tổng quan

✅ **Google Drive Integration**
- Mỗi user có thư mục riêng
- Service account authentication
- Automatic folder creation

✅ **API Documentation**
- Swagger UI integration
- Comprehensive API docs
- Interactive testing interface

## 🔧 Cấu hình cần thiết

### Database (MySQL)
```sql
CREATE DATABASE CloudVault;
```

### Google Drive API
1. Tạo project trên Google Cloud Console
2. Enable Google Drive API
3. Tạo Service Account
4. Download `credentials.json` và đặt vào `src/main/resources/`

### Application Properties
Cập nhật `src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret=your-secret-key
```

## 📖 Tài liệu API

Xem file `API_DOCUMENTATION.md` để biết chi tiết về:
- Các endpoint API
- Request/Response examples
- Authentication flow
- Error handling

## 🧪 Testing

### Với Swagger UI:
1. Vào http://localhost:8080/swagger-ui.html
2. Đăng nhập admin: `admin` / `hoangadmin@11`
3. Copy JWT token và authorize
4. Test các API

### Với Postman:
Import collection từ Swagger docs hoặc test thủ công theo API_DOCUMENTATION.md

## 📁 Cấu trúc dự án

```
src/main/java/com/vti/cloudvault/
├── api/           # REST Controllers
├── config/        # Configuration classes
├── dto/           # Data Transfer Objects
├── repository/    # JPA Repositories & Entities
├── security/      # Security configuration
├── service/       # Business logic
└── util/          # Utility classes
```

## ⚡ Các lệnh hữu ích

```bash
# Biên dịch
mvn clean compile

# Chạy tests
mvn test

# Tạo JAR file
mvn clean package

# Chạy với profile khác
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## 🔍 Troubleshooting

**Lỗi thường gặp:**
- Port 8080 đã được sử dụng → Đổi port trong application.properties
- Database connection failed → Check MySQL service và credentials
- Google Drive API error → Verify credentials.json và API enabled
- JWT error → Check secret key và token expiration

**Logs:**
Check console output hoặc application logs để debug chi tiết.

## 📞 Support

Nếu gặp vấn đề, check:
1. Console logs khi startup
2. Database connection
3. Google Drive credentials
4. Port conflicts

---
**CloudVault** - Secure Cloud Storage Management System
