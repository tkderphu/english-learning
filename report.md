# BỘ THÔNG TIN VÀ TRUYỀN THÔNG
# HỌC VIỆN CÔNG NGHỆ BƯU CHÍNH VIỄN THÔNG
## KHOA CÔNG NGHỆ THÔNG TIN 1
### KHÓA 2022

----------

# PHÁT TRIỂN ỨNG DỤNG CHO CÁC THIẾT BỊ DI ĐỘNG
# BÁO CÁO BÀI TẬP LỚN
## ĐỀ TÀI: ỨNG DỤNG HỌC TIẾNG ANH FLULINGO

| Nội dung | Thông tin |
|---|---|
| Giảng viên hướng dẫn | Nguyễn Hoàng Anh |
| Nhóm môn học | N07 |
| Nhóm bài tập lớn | 15 |
| Thành Viên | 4 thành viên |

| Họ và tên | MSV |
|---|---|
| Thái Hữu Phúc | B22DCCN633 |
| Lê Đình Phát | B22DCCN609 |
| Lê Trọng Sang | B22DCCN681 |
| Nguyễn Quang Phú | B22DCCN621 |

---

# Hà Nội – 2026

---

# Ứng dụng: HỌC TIẾNG ANH FLULINGO

# Danh sách thành viên

| STT | Họ và Tên | MSV | Đóng góp |
|---|---|---|---|
| 1 | Lê Đình Phát | B22DCCN699 | Xây dựng giao diện bằng Figma<br><br>Code: Các chức năng quản lí bộ thẻ (thêm, sửa, xóa), tìm kiếm bộ thẻ, Các chức năng ôn tập bộ thẻ (Lật thẻ, trắc nghiệm, ghép thẻ) |
| 2 | Thái Hữu Phúc | B22DCCN633 | Xây dựng base cho code mobile, Thiết kế cơ sở dữ liệu<br><br>Phát triển các tính năng: onboarding, hiển thị các đầu sách theo sở thích, tác giả, lịch sử, đọc sách. |
| 3 | Lê Trọng Sang | B22DCCN681 | Xây dựng giao diện và xử lý luồng trò chuyện với AI, bao gồm: gửi/nhận tin nhắn, ghi âm giọng nói, phát âm lại, sao chép/dịch/lưu từ, hiển thị phản hồi và tóm tắt phiên học.<br><br>Phát triển các chức năng profile như: quản lí thông tin cá nhân, lịch sử hoạt động, từ vựng đã lưu, các mục sửa lỗi. |
| 4 | Nguyễn Quang Phú | B22DCCN621 | Xây dựng base code backend<br><br>Phát triển các tính năng auth: login, đăng ký, quên mật khẩu (gửi otp)<br><br>Phát triển các tính năng tạo sách, tạo chapter, tạo page cho module sách, tra cứu từ và thêm từ vựng vào flashcard khi đọc sách |

---

# Mục Lục

- Danh mục từ viết tắt
- Danh mục hình ảnh
- Danh mục bảng biểu
- Phần 1: Tài liệu kỹ thuật mô tả chức năng
  - 1.1 Danh sách các chức năng được phân công
  - 1.2 Kiến trúc chi tiết hệ thống
    - 1.2.1 Kiến trúc tổng quan hệ thống
    - 1.2.2 Kiến trúc phân lớp Backend (Spring Boot)
    - 1.2.3 Kiến trúc Client (Android MVVM & ReactJS)
    - 1.2.4 Biểu đồ tuần tự theo từng chức năng
  - 1.3 Code đáp ứng chức năng (Lớp, hàm, Bảng trong CSDL, API gọi ngoài)
    - 1.3.1 Bảng trong cơ sở dữ liệu (MySQL)
    - 1.3.2 Chức năng Đăng nhập, đăng ký và quên mật khẩu – Các lớp và hàm
    - 1.3.3 Chức năng Tạo sách, chương, trang (Admin) – Các lớp và hàm
    - 1.3.4 Chức năng Tra cứu từ và thêm từ vào flashcard – Các lớp và Hàm
    - 1.3.5 API/Dịch vụ ngoài được tích hợp
  - 1.4 Hướng dẫn và Các lưu ý khi cài đặt, triển khai
    - 1.4.1 Yêu cầu môi trường
    - 1.4.2 Cài đặt Backend
    - 1.4.3 Cài đặt Android App & Web Admin
    - 1.4.4 Biến môi trường cần cấu hình (Backend)
    - 1.4.5 Lưu ý quan trọng khi cài đặt và triển khai
- Phần 2: Code
  - 2.1 Link github và các file liên quan đến chức năng cá nhân
  - 2.2 Quy tắc comment code

---

# Danh mục từ viết tắt

(Bổ sung nếu cần)

---

# Danh mục hình ảnh

- Hình 1: Biểu đồ tuần tự chức năng Đăng ký
- Hình 2: Biểu đồ tuần tự chức năng Đăng nhập
- Hình 3: Biểu đồ tuần tự chức năng Quên mật khẩu
- Hình 4: Biểu đồ tuần tự chức năng Tạo sách (Admin)
- Hình 5: Biểu đồ tuần tự chức năng Tạo chương và trang sách (Admin)
- Hình 6: Biểu đồ tuần tự chức năng Tra cứu từ vựng khi đọc sách
- Hình 7: Biểu đồ tuần tự chức năng Thêm từ vựng vào Flashcard
- Hình 8: Biểu đồ cơ sở dữ liệu

---

# Danh mục bảng biểu

- Bảng 1.1: Chức năng được phân công
- Bảng 1.2: Phân lớp ở Backend
- Bảng 1.3: Các thành phần liên quan bên Client (Android & Web)
- Bảng 1.4: Các bảng liên quan đến các chức năng được phân công
- Bảng 1.5: Các lớp và hàm cho chức năng Đăng nhập, đăng ký và quên mật khẩu (Backend)
- Bảng 1.6: Các lớp và hàm cho chức năng Đăng nhập, đăng ký và quên mật khẩu (Android)
- Bảng 1.7: Các lớp và hàm cho chức năng Tạo sách, chương, trang (Backend)
- Bảng 1.8: Các lớp và hàm cho chức năng Tạo sách, chương, trang (Website)
- Bảng 1.9: Các lớp và hàm cho chức năng Tra cứu từ và thêm từ vào flashcard (Backend)
- Bảng 1.10: Các lớp và hàm cho chức năng Tra cứu từ và thêm từ vào flashcard (Android)
- Bảng 1.11: Các dịch vụ/API ngoài được tích hợp
- Bảng 1.12: Các yêu cầu về môi trường
- Bảng 1.13: Cấu hình các biến môi trường
- Bảng 2.1: Các link github của dự án

---

# Phần 1: Tài liệu kỹ thuật mô tả chức năng

## 1.1 Danh sách các chức năng được phân công

Trong dự án ỨNG DỤNG HỌC TIẾNG ANH FLULINGO của Nhóm 15, em là Nguyễn Quang Phú (B22DCCN621) được phân công thực hiện các chức năng chính:

### Bảng 1.1: Chức năng được phân công

| STT | Chức năng | Mô tả tóm tắt | Công nghệ chính |
|---|---|---|---|
| 1 | Tạo sách, trang, chapter cho module Sách | Admin tạo sách, thêm nội dung các chương và trang để cung cấp nội dung đọc cho người dùng. | Web: ReactJS<br>Backend: Spring Boot REST API, Spring Data JPA, MySQL |
| 2 | Đăng ký, đăng nhập, quên mật khẩu | Đăng ký tài khoản mới, xác thực đăng nhập người dùng, cấp lại mật khẩu thông qua mã OTP gửi qua email. | Mobile: Android MVVM, Kotlin Coroutines, Retrofit<br>Backend: Spring Boot REST API, JWT |
| 3 | Tra cứu từ và thêm từ vựng vào flashcard khi đọc sách | Hỗ trợ tra từ trực tiếp qua AI khi đọc sách, cho phép thêm từ vừa tra vào một bộ thẻ (flashcard) để ôn tập. | Mobile: Android Fragment, ViewModel, BottomSheetDialog<br>Backend: Spring Boot, Groq AI API |

## 1.2 Kiến trúc chi tiết hệ thống

### 1.2.1 Kiến trúc tổng quan hệ thống

Hệ thống FLULINGO được xây dựng theo mô hình kiến trúc Client–Server với các khối chính:

- **Android Client (Kotlin + View/Data Binding):** Ứng dụng di động đóng vai trò giao diện người dùng, áp dụng mô hình Clean Architecture kết hợp MVVM (Model-View-ViewModel). Giao tiếp với backend thông qua thư viện Retrofit (HTTP/REST API).
- **Web Admin (ReactJS):** Website cho quản trị viên thực hiện các thao tác quản lý dữ liệu hệ thống (sách, tài khoản), giao tiếp với Backend thông qua thư viện Axios.
- **Spring Boot Backend (Java 17):** Đảm nhận xử lý nghiệp vụ, bảo mật, quản lý dữ liệu, và tích hợp các dịch vụ trí tuệ nhân tạo (AI Chat thông qua Groq API). Cung cấp các RESTful API cho ứng dụng di động và website.
- **Cơ sở dữ liệu:** MySQL (lưu trữ chính thông tin user, OTP, book, chapter, page, deck, flashcard...).

### 1.2.2 Kiến trúc phân lớp Backend (Spring Boot)

Ở phía máy chủ, backend được tổ chức chặt chẽ theo kiến trúc phân tầng (Layered Architecture):

### Bảng 1.2: Phân lớp ở Backend

| Lớp | Package | Vai trò |
|---|---|---|
| Controller | site.viosmash.english.controller | Tiếp nhận HTTP request từ Client, kiểm tra tính hợp lệ, gọi đến tầng Service và trả về Response JSON. |
| Service | site.viosmash.english.service | Chứa logic nghiệp vụ cốt lõi: xử lý đăng nhập, gửi email, tra cứu AI, quản lý dữ liệu sách. |
| Repository | site.viosmash.english.repository | Giao tiếp với cơ sở dữ liệu MySQL thông qua Spring Data JPA. |
| Entity | site.viosmash.english.entity | Ánh xạ trực tiếp các bảng trong cơ sở dữ liệu thành các đối tượng Java. |
| DTO & Mapper | site.viosmash.english.dto<br>site.viosmash.english.mapper | Chuyển đổi dữ liệu giữa Entity (DB) và DTO (Data Transfer Object) để trao đổi qua API. |
| Config / Security | site.viosmash.english.config<br>site.viosmash.english.security | Cấu hình bảo mật JWT, bộ lọc Request (Filter), CORS và cấp quyền truy cập theo vai trò. |
| AI Integration | site.viosmash.english.groqapi | Tích hợp gọi API Groq/OpenAI để tra cứu và dịch nghĩa từ vựng. |

### 1.2.3 Kiến trúc Client (Android MVVM & ReactJS)

### Bảng 1.3: Các thành phần liên quan bên Client (Android & Web)

| Nền tảng | Tầng | Thành phần | Vai trò |
|---|---|---|---|
| **Android** | View | Activity, Fragment, BottomSheet | Lớp thụ động (Passive View): Render giao diện, thu thập trạng thái từ ViewModel. |
| **Android** | ViewModel | AuthViewModel, ReaderViewModel | Quản lý trạng thái UI (UI State), gọi API thông qua Repository và xử lý kết quả. |
| **Android** | Repository | AuthRepository, BookRepository | Trung gian gọi API Backend bằng Retrofit. |
| **Web** | Component | BookForm, ChapterForm | Hiển thị form nhập liệu, thu thập thông tin sách từ Admin. |
| **Web** | Service | BookApiService, AxiosInstance | Nơi tập trung các lệnh gọi API POST/PUT lên Backend. |

### 1.2.4 Biểu đồ tuần tự theo từng chức năng

1. **Chức năng Đăng ký**
**Hình 1:** Biểu đồ tuần tự chức năng Đăng ký

2. **Chức năng Đăng nhập**
**Hình 2:** Biểu đồ tuần tự chức năng Đăng nhập

3. **Chức năng Quên mật khẩu**
**Hình 3:** Biểu đồ tuần tự chức năng Quên mật khẩu

4. **Chức năng Tạo sách (Admin)**
**Hình 4:** Biểu đồ tuần tự chức năng Tạo sách (Admin)

5. **Chức năng Tạo chương và trang sách (Admin)**
**Hình 5:** Biểu đồ tuần tự chức năng Tạo chương và trang sách (Admin)

6. **Chức năng Tra cứu từ vựng khi đọc sách**
**Hình 6:** Biểu đồ tuần tự chức năng Tra cứu từ vựng khi đọc sách

7. **Chức năng Thêm từ vựng vào Flashcard**
**Hình 7:** Biểu đồ tuần tự chức năng Thêm từ vựng vào Flashcard

## 1.3 Code đáp ứng chức năng (Lớp, hàm, Bảng trong CSDL, API gọi ngoài)

### 1.3.1 Bảng trong cơ sở dữ liệu (MySQL)

**Hình 8:** Biểu đồ cơ sở dữ liệu

Các bảng MySQL liên quan đến các chức năng được phân công:

### Bảng 1.4: Các bảng liên quan đến các chức năng được phân công

| Bảng (Entity) | Trường chính | Mô tả | Liên quan chức năng |
|---|---|---|---|
| users | id, email, password, full_name | Thông tin người dùng hệ thống (Admin, User). | Đăng nhập, Đăng ký |
| password_reset_otps | id, email, otp, expires_at | Lưu mã OTP gửi qua email và thời hạn hiệu lực. | Quên mật khẩu |
| book | id, title, description, cover_url, language | Lưu thông tin cơ bản của cuốn sách (tựa đề, ngôn ngữ...). | Tạo sách (Admin) |
| chapter | id, book_id, title, description, number | Lưu thông tin các chương (số thứ tự, mô tả) thuộc một cuốn sách. | Tạo sách (Admin) |
| page | id, chapter_id, content, audio_id, number | Lưu nội dung chi tiết dạng văn bản của từng trang. | Tạo sách (Admin) |
| audio | id, duration, format, file_url | Lưu thông tin tệp âm thanh đính kèm của trang sách. | Tạo sách (Admin) |
| sentence | id, page_id, content, start_time, end_time | Lưu từng câu văn được Whisper AI phân tách với mốc thời gian. | Tạo sách (Admin) |
| decks | id, user_id, title | Lưu danh sách bộ thẻ từ vựng của người dùng. | Tra cứu từ và lưu Flashcard |
| flashcards | id, deck_id, term, definition, image_url | Lưu thông tin từ vựng và nghĩa. Dùng khi user tra từ và thêm vào bộ thẻ. | Tra cứu từ và lưu Flashcard |

### 1.3.2 Chức năng Đăng nhập, đăng ký và quên mật khẩu – Các lớp và hàm

#### Backend

### Bảng 1.5: Các lớp và hàm cho chức năng Đăng nhập, đăng ký và quên mật khẩu (Backend)

| Lớp/File | Hàm/Phương thức | Mô tả chi tiết |
|---|---|---|
| [AuthController.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/controller/AuthController.java) | `POST /api/auth/v1/login`<br>login(LoginRequest) | Nhận request đăng nhập, validate bằng `@Valid` rồi gọi `authService.login()`. Trả về `AuthResponse` gồm `accessToken`, `refreshToken` và thông tin user. |
| [AuthController.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/controller/AuthController.java) | `POST /api/auth/v1/forgot-password/request-otp`<br>requestOtp(ForgotPasswordRequest) | Kiểm tra email tồn tại, tạo OTP ngẫu nhiên 6 chữ số và gọi `forgotPasswordService.requestOtp()`. |
| [AuthController.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/controller/AuthController.java) | `POST /api/auth/v1/forgot-password/verify-otp`<br>verifyOtp(VerifyOtpRequest) | Gọi `forgotPasswordService.verifyOtp()` để kiểm tra OTP có hợp lệ và chưa hết hạn. |
| [AuthController.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/controller/AuthController.java) | `POST /api/auth/v1/forgot-password/reset`<br>resetPassword(ResetPasswordRequest) | Gọi `forgotPasswordService.resetPassword()` để mã hóa mật khẩu mới và đánh dấu OTP là đã dùng. |
| [UserController.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/controller/UserController.java) | `POST /api/user/v1`<br>createUser(UserCreateRequest) | Tạo tài khoản mới, gọi `userService.create()`. |
| [AuthService.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/service/AuthService.java) | login(LoginRequest)<br>buildAccessToken(User, String)<br>buildRefreshToken(User) | Xác thực email/mật khẩu bằng `passwordEncoder.matches()`, sinh `refreshToken` rồi `accessToken` (JWT đính kèm claim: email, role, expired, ...). |
| [ForgotPasswordService.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/service/ForgotPasswordService.java) | requestOtp(String email)<br>verifyOtp(String email, String otp)<br>resetPassword(String email, String otp, String newPassword) | `requestOtp` xóa OTP cũ , sinh OTP mới, gọi `mailService.sendOtpEmail()`. `verifyOtp` kiểm tra OTP chưa dùng và chưa hết hạn. `resetPassword` mã hóa và lưu mật khẩu mới. |
| [UserService.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/service/UserService.java) | create(UserCreateRequest) | Kiểm tra email trùng lặp qua `userRepository.findByEmail()`, mã hóa mật khẩu bằng `passwordEncoder.encode()`, lưu `User` mới vào CSDL. |
| [JwtService.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/service/JwtService.java) | generateToken(Map<String,Object> claims)<br>extractClaim(String claim, String token)<br>isExpired(String token) | `generateToken` ký JWT bằng thuật toán HS256. `extractClaim` giải mã và lấy giá trị claim cụ thể. `isExpired` kiểm tra token có còn hạn không. |
| [UserRepository.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/repository/UserRepository.java) | findByEmail(String email) | Spring Data JPA tự động sinh query SELECT theo email, dùng trong `AuthService` và `UserService`. |

#### Android

### Bảng 1.6: Các lớp và hàm cho chức năng Đăng nhập, đăng ký và quên mật khẩu (Android)

| Lớp/File | Hàm/Phương thức | Mô tả chi tiết |
|---|---|---|
| [AuthApiService.kt](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/data/remote/api/AuthApiService.kt) | login(LoginRequest)<br>createUser(CreateUserRequest)<br>requestForgotPasswordOtp(ForgotPasswordRequest)<br>verifyForgotPasswordOtp(VerifyOtpRequest)<br>resetPassword(ResetPasswordRequest) | Interface Retrofit định nghĩa các endpoint gọi lên Backend. Token được tự động gắn vào header thông qua `AuthInterceptor`. |
| [AuthRepositoryImpl.kt](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/data/repository/AuthRepositoryImpl.kt) | login(LoginRequest)<br>signUp(email, password, fullName)<br>requestForgotPasswordOtp(email)<br>verifyForgotPasswordOtp(email, otp)<br>resetForgotPassword(email, otp, newPassword) | Tầng Repository gọi API, xử lý Response và lưu `accessToken`/`refreshToken` vào `AuthManager` (DataStore) sau đăng nhập thành công. |
| [LoginViewModel.kt](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/login/LoginViewModel.kt) | onLoginClick()<br>setEmail(String)<br>setPassword(String)<br>handleCheckOnboarding() | Nhận sự kiện UI, gọi `LoginUseCase`, cập nhật `LoginState` (isLoading, isSuccess). Sau login thành công kiểm tra Onboarding để điều hướng. |
| [SignUpViewModel.kt](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/signup/SignUpViewModel.kt) | onSignUpClick(email, password, fullName) | Gọi `SignUpUseCase`, cập nhật `SignUpState` và emit `SignUpEvent.NavigateToLogin` khi đăng ký thành công. |
| [ForgotPasswordViewModel.kt](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/forgotpassword/ForgotPasswordViewModel.kt) | onRequestOtp()<br>setEmail(String) | Validate email, gọi `RequestForgotPasswordOtpUseCase` rồi emit `ForgotPasswordEvent.NavigateToEnterOtp`. |
| [ResetPasswordViewModel.kt](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/resetpassword/ResetPasswordViewModel.kt) | onUpdatePassword()<br>setOtp(String)<br>setPassword(String)<br>setRePassword(String) | Kiểm tra hai mật khẩu trùng nhau, gọi `ResetForgotPasswordUseCase`, emit `ResetPasswordEvent.NavigateToLogin` khi thành công. |
| [LoginFragment.kt](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/login/LoginFragment.kt) | (View Binding) | Giao diện đăng nhập, lấy email/mật khẩu, quan sát `LoginState` từ `LoginViewModel`. |
| [SignUpFragment.kt](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/signup/SignUpFragment.kt) | (View Binding) | Giao diện đăng ký, thu thập email, mật khẩu, họ tên. |
| [ForgotPasswordFragment.kt](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/forgotpassword/ForgotPasswordFragment.kt) | (View Binding) | Giao diện nhập email để nhận OTP, quan sát `ForgotPasswordState` từ `ForgotPasswordViewModel`. |
| [ResetPasswordFragment.kt](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/resetpassword/ResetPasswordFragment.kt) | (View Binding) | Giao diện nhập OTP và mật khẩu mới, quan sát `ResetPasswordState` từ `ResetPasswordViewModel`. |

### 1.3.3 Chức năng Tạo sách, chương, trang (Admin) – Các lớp và hàm

#### Backend

### Bảng 1.7: Các lớp và hàm cho chức năng Tạo sách, chương, trang (Backend)

| Lớp/File | Hàm/Phương thức | Mô tả chi tiết |
|---|---|---|
| [BookController.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/controller/BookController.java) | `POST /api/book/v1`<br>create(BookCreateRequest) | Nhận request tạo sách, gọi `bookService.create()`. Không yêu cầu xác thực (public endpoint). |
| [ChapterController.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/controller/ChapterController.java) | `POST /api/chapter/v1`<br>create(ChapterCreateRequest) | Nhận `bookId`, `title`, `description`, `number` rồi gọi `chapterService.create()`, trả về `chapterId` mới. |
| [PageController.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/controller/PageController.java) | `POST /api/page/v1`<br>create(PageRequest) | Nhận `chapterId`, `content`, `audioId`, `number`, gọi `pageService.create()`. Nếu có lỗi I/O sẽ ném `ServiceException`. |
| [AudioController.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/controller/AudioController.java) | `POST /api/audio/v1/upload`<br>upload(MultipartFile) | Nhận file âm thanh, dùng `Mp3File` trích xuất metadata (thời lượng, sample rate), lưu vào ổ cứng và DB, sau đó trả về `audioId` để dùng cho việc tạo Trang (Page). |
| [FileController.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/controller/FileController.java) | `POST /api/file/v1/upload`<br>upload(MultipartFile) | Endpoint upload file dùng chung (được gọi từ web Admin để tải lên ảnh bìa của sách). Trả về URL để gán vào `coverUrl`. |
| [BookService.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/service/BookService.java) | create(BookCreateRequest)<br>recordReadingProgress(int userId, int bookId, BookReadingProgressRequest) | `create()` lưu `Book`, sau đó lặp danh sách `authorIds` để lưu `AuthorBook`, lặp `genreIds` để lưu `BookGenre`. `recordReadingProgress()` cập nhật `BookProgress` và ghi heatmap nếu đọc ≥ 30 giây. |
| [ChapterService.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/service/ChapterService.java) | create(ChapterCreateRequest) | Set các field `title`, `description`, `bookId`, `number` vào entity `Chapter` rồi gọi `chapterRepository.save()`. |
| [PageService.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/service/PageService.java) | create(PageRequest) | Lưu entity `Page`, sau đó gọi `whisperService.loadSamplePage()` để lấy danh sách câu tương ứng, rồi `sentenceRepository.saveAll()` để lưu toàn bộ `Sentence` của trang đó. |
| [AuthorBookRepository.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/repository) | save(AuthorBook) | Spring Data JPA lưu liên kết giữa tác giả và sách (bảng trung gian `author_book`). |
| [BookGenreRepository.java](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/repository) | save(BookGenre) | Spring Data JPA lưu liên kết giữa thể loại và sách (bảng trung gian `book_genre`). |

#### Website (ReactJS – Admin Portal)

### Bảng 1.8: Các lớp và hàm cho chức năng Tạo sách, chương, trang (Website)

| Lớp/File | Hàm/Phương thức | Mô tả chi tiết |
|---|---|---|
| [axios.js](https://github.com/tkderphu/english-learning/blob/main/admin-portal/src/api/axios.js) | `axios.create({ baseURL: '/api' })`<br>interceptors.response | Cấu hình Axios chung: `baseURL = /api`, response interceptor tự động bóc `response.data` (BaseResponse) ra. Không cấu hình JWT header riêng (proxy qua Vite). |
| [BookForm.jsx](https://github.com/tkderphu/english-learning/blob/main/admin-portal/src/components/BookForm.jsx) | handleSubmit()<br>handleAudioUpload(e)<br>fetchAuthors()<br>fetchGenres() | Dialog nhập thông tin sách (`title`, `language`, `coverUrl`, `authorIds`, `genreIds`). `handleAudioUpload()` upload file âm thanh tới `POST /audio/v1/upload` lấy URL rồi điền vào `coverUrl`. `handleSubmit()` gọi `POST /book/v1`. |
| [ChapterForm.jsx](https://github.com/tkderphu/english-learning/blob/main/admin-portal/src/components/ChapterForm.jsx) | handleSubmit() | Dialog nhập `number`, `title`, `description` của chương. `handleSubmit()` gọi `POST /chapter/v1` kèm `bookId` lấy từ props. |
| [PageForm.jsx](https://github.com/tkderphu/english-learning/blob/main/admin-portal/src/components/PageForm.jsx) | handleSubmit()<br>handleAudioUpload(e) | Dialog tạo trang sách. `handleAudioUpload()` upload audio tới `POST /audio/v1/upload`, lưu `audioId` trả về vào state. `handleSubmit()` gọi `POST /page/v1` với `{ chapterId, content, audioId, number }`. |
| [BookFormPage.jsx](https://github.com/tkderphu/english-learning/blob/main/admin-portal/src/pages/BookFormPage.jsx) | (Page component) | Trang chứa `BookForm` cho Admin, điều hướng đến sau khi tạo sách thành công. |
| [ChapterFormPage.jsx](https://github.com/tkderphu/english-learning/blob/main/admin-portal/src/pages/ChapterFormPage.jsx) | (Page component) | Trang chứa `ChapterForm`, nhận `bookId` từ URL param để tạo chapter thuộc đúng sách. |
| [PageFormPage.jsx](https://github.com/tkderphu/english-learning/blob/main/admin-portal/src/pages/PageFormPage.jsx) | (Page component) | Trang chứa `PageForm`, nhận `chapterId` từ URL param để tạo page thuộc đúng chapter. |

### 1.3.4 Chức năng Tra cứu từ và thêm từ vào flashcard – Các lớp và Hàm

#### Backend

### Bảng 1.9: Các lớp và hàm cho chức năng Tra cứu từ và thêm từ vào flashcard (Backend)

| Lớp/File | Hàm/Phương thức | Mô tả chi tiết |
|---|---|---|
| AiLookupController.java | `POST /api/lookup/v1`<br>lookup(TextLookupRequest) | Endpoint nhận từ/cụm từ được chọn, gọi `aiLookupService.lookupText()` và trả về JSON chứa `meaning`, `phonetic`, `examples`, `audioUrl`. |
| AiLookupServiceImpl.java | lookupText(String text) | Build prompt yêu cầu AI trả về đúng JSON, gọi `groqAiClient.createTextResponse()`, sau đó parse chuỗi kết quả thành `TextLookupResponse` bằng `ObjectMapper.readValue()`. Có fallback `extractJson()` nếu AI trả về markdown kèm code block. |
| GroqAiClient.java | createTextResponse(OpenAiTextRequest) | Gửi HTTP POST đến Groq/OpenAI bằng Spring WebClient. Tự động chuyển đổi format request giữa Groq Chat Completions và OpenAI Responses API. Tích hợp retry tối đa 3 lần khi gặp lỗi 429/5xx. |
| DeckService.java | updateDeck(Integer userId, int deckId, DeckUpdateRequest) | Logic thêm flashcard mới vào deck có sẵn: lấy danh sách thẻ hiện tại, merge với danh sách mới từ request, gọi `flashcardRepository.saveAll()` để batch insert. |
| DeckController.java | `PUT /api/deck/v1/{id}`<br>updateDeck(Integer id, DeckUpdateRequest) | Endpoint cập nhật deck (thêm thẻ mới). Client Android gọi API này với danh sách flashcard đầy đủ (cũ + thẻ mới vừa tra). |
| FlashcardRepository.java | findByDeckIdAndStatus(int deckId, int status)<br>saveAll(List<Flashcard>) | Truy vấn tất cả thẻ active của deck; lưu hàng loạt flashcard (batch insert) vào bảng `flashcards`. |

#### Android

### Bảng 1.10: Các lớp và hàm cho chức năng Tra cứu từ và thêm từ vào flashcard (Android)

| Lớp/File | Hàm/Phương thức | Mô tả chi tiết |
|---|---|---|
| [ReadBookViewModel.kt](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/readbook/ReadBookViewModel.kt) | onTextSelected(String selectedText)<br>lookupText(String text)<br>retryLookup()<br>dismissLookupDialog() | Khi người dùng bôi đen chữ trên trang sách, `onTextSelected()` chuẩn hóa chuỗi rồi gọi `lookupText()`. Hàm này gọi `LookupTextUseCase` và cập nhật `lookupStatus` (Loading → Success/Error) vào StateFlow. |
| [LookupTextUseCase.kt](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/domain/usecase/page/LookupTextUseCase.kt) | invoke(String text) | UseCase tầng Domain, gọi `pageRepository.lookupText(text)` và trả về `Result<TextLookupResult>`. |
| [ReadBookFragment.kt](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/readbook/ReadBookFragment.kt) | renderLookupDialogState()<br>ensureLookupDialog()<br>setupLookupBottomSheetResults() | Fragment quan sát `lookupStatus` từ ViewModel, hiển thị `AlertDialog` với nội dung tra từ (phiên âm, nghĩa, ví dụ). Khi nhấn "Thêm vào Flashcard" sẽ mở `ChooseDeckBottomSheet`. |
| [ChooseDeckBottomSheet.kt](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/readbook/lookup/ChooseDeckBottomSheet.kt) | show(FragmentManager)<br>loadDecks() (qua DeckPickerViewModel) | BottomSheet cho người dùng chọn bộ thẻ muốn thêm từ vừa tra vào. Dùng `FragmentResult` để trả `deckId` và `deckTitle` về Fragment cha. |
| [AddFlashcardBottomSheet.kt](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/readbook/lookup/AddFlashcardBottomSheet.kt) | show(FragmentManager, deckId, deckTitle, defaultTerm, defaultDefinition) | BottomSheet cuối cùng trong luồng: hiển thị form với `term` và `definition` đã điền sẵn từ kết quả tra từ, người dùng xác nhận và nhấn nút thêm. |
| [AddFlashcardViewModel.kt](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/readbook/lookup/AddFlashcardViewModel.kt) | submit(Int deckId, String term, String definition) | Lấy deck hiện tại bằng `GetDeckByIdUseCase`, tạo `UpdateDeckRequest` gộp flashcard mới vào danh sách cũ rồi gọi `UpdateDeckUseCase` để gửi lên Backend. |

### 1.3.5 API/Dịch vụ ngoài được tích hợp

### Bảng 1.11: Các dịch vụ/API ngoài được tích hợp

| Dịch vụ | Mục đích trong chức năng | Các tích hợp |
|---|---|---|
| Spring Data JPA & MySQL | Lưu trữ dữ liệu: User, sách, nội dung trang sách, bộ thẻ. | Tích hợp ở tầng Repository trên Spring Boot. |
| Security JWT Token | Xác thực & Phân quyền: Ngăn chặn người ngoài truy cập API tạo sách (chỉ dành cho Admin) và bảo mật thông tin tài khoản người học. | Trên Android/React, Token được lưu (DataStore/LocalStorage) và tự động gắn vào Header Authorization: Bearer. |
| JavaMailSender | Gửi Email OTP | Sử dụng SMTP của Gmail để gửi mã xác nhận khi người dùng chọn "Quên mật khẩu". |
| Groq AI / Llama | Dịch nghĩa từ vựng ngữ cảnh | Dùng để tra cứu nhanh từ vựng với độ chính xác cao dựa trên ngữ cảnh câu khi đọc sách. |

## 1.4 Hướng dẫn và Các lưu ý khi cài đặt, triển khai

### 1.4.1 Yêu cầu môi trường

### Bảng 1.12: Các yêu cầu về môi trường

| Thành phần | Phiên bản | Ghi chú |
|---|---|---|
| Java JDK | 17+ | Sử dụng Amazon Corretto 17 hoặc OpenJDK 17 cho Spring Boot. |
| Apache Maven | 3.9+ | Hoặc chạy trực tiếp file `./mvnw` có trong source. |
| Node.js & npm | 18+ | Môi trường build cho ứng dụng ReactJS (Web Admin). |
| MySQL Server | 8.0+ | Sử dụng lưu trữ cơ sở dữ liệu chính. |
| Android Studio | Hedgehog (2023.1.1)+ | IDE phát triển ứng dụng Android. |

### 1.4.2 Cài đặt Backend

Source code backend: https://github.com/tkderphu/english-learning.git

1. Clone repository về máy tính thông qua Git.
2. Mở thư mục dự án bằng IntelliJ IDEA hoặc Eclipse.
3. Tạo cơ sở dữ liệu trên MySQL với tên `english`.
4. Cấu hình biến môi trường trong file `src/main/resources/application.properties` (xem Bảng 1.13).
5. Khởi chạy ứng dụng:
   - Chạy file `EnglishApplication.java` từ IDE.
   - Hoặc dùng lệnh: `./mvnw spring-boot:run`.
6. Kiểm tra API tại Swagger UI: `http://localhost:7000/swagger-ui/index.html`

### 1.4.3 Cài đặt Android App & Web Admin

**Android App:**
Source code: https://github.com/MADLearningEnglish/english_mobile.git
1. Mở code bằng Android Studio. Sync Project with Gradle Files.
2. Đổi `BASE_URL` trong NetworkModule sang địa chỉ IP IPv4 của máy tính (ví dụ `http://192.168.1.10:7000/api/`).
3. Build và Run trên thiết bị giả lập hoặc điện thoại thật.

**Web Admin (ReactJS):**
1. Mở Terminal, di chuyển vào thư mục web admin.
2. Chạy lệnh `npm install` để tải thư viện.
3. Cấu hình `.env` cho URL Backend.
4. Chạy `npm start` để khởi động server frontend ở cổng `3000`.

### 1.4.4 Biến môi trường cần cấu hình (Backend)

### Bảng 1.13: Cấu hình các biến môi trường

| Biến môi trường | Mô tả | Ví dụ |
|---|---|---|
| DB_USERNAME | Tên đăng nhập MySQL | root |
| DB_PASSWORD | Mật khẩu MySQL | 123456 |
| MAIL_USERNAME | Tài khoản Gmail để gửi OTP | yourapp@gmail.com |
| MAIL_PASSWORD | App Password của Gmail | xxxx xxxx xxxx xxxx |
| GROQ_API_KEY | API Key của Groq để chạy AI (Tra từ) | gsk_abc123... |
| JWT_SECRET | Chuỗi ký hiệu bí mật JWT | mySecretKey123... |
| JWT_EXPIRATION | Thời gian sống của Token (ms) | 86400000 |

### 1.4.5 Lưu ý quan trọng khi cài đặt và triển khai

- **Cấu hình SMTP:** Để chức năng Quên mật khẩu hoạt động, bạn cần dùng tài khoản Google có bật xác minh 2 bước và tạo "App Password" cho `MAIL_PASSWORD`.
- **IP Kết nối (Mobile):** Khi test trên điện thoại, không dùng `localhost` để làm địa chỉ server backend.
- **Groq API Key:** Yêu cầu đăng ký tài khoản Groq miễn phí để lấy API key phục vụ việc tra nghĩa.
- **CORS:** Đảm bảo Backend cấu hình đúng CORS Filter để ReactJS chạy ở `localhost:3000` có thể gửi Request mà không bị chặn.

---

# Phần 2: Code

## 2.1 Link github và các file liên quan đến chức năng cá nhân

### Bảng 2.1: Các link github của dự án

| Repository | Link | Nội dung |
|---|---|---|
| Backend (Spring Boot) | https://github.com/tkderphu/english-learning.git | Java 17, Spring Boot 3, MySQL. |
| Android App (Kotlin) | https://github.com/MADLearningEnglish/english_mobile.git | Kotlin, MVVM, Clean Architecture. |
| Web Admin (ReactJS) | Đang cập nhật | Giao diện quản lý sách cho Admin. |

### Các file liên quan đến chức năng cá nhân do Nguyễn Quang Phú (B22DCCN621) đảm nhiệm:

#### Backend (Chức năng Xác thực & Quản lý người dùng)
- [`controller/AuthController.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/controller/AuthController.java)
- [`controller/UserController.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/controller/UserController.java)
- [`service/AuthService.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/service/AuthService.java)
- [`service/UserService.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/service/UserService.java)
- [`repository/UserRepository.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/repository/UserRepository.java)
- [`entity/User.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/entity/User.java)
- [`entity/OtpToken.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/entity/OtpToken.java)
- [`security/JwtService.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/security/JwtService.java)

#### Backend (Chức năng Quản lý Sách, Tra từ vựng)
- [`controller/BookController.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/controller/BookController.java), [`controller/ChapterController.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/controller/ChapterController.java), [`controller/PageController.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/controller/PageController.java)
- [`controller/AiLookupController.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/controller/AiLookupController.java)
- [`service/BookService.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/service/BookService.java), [`service/ChapterService.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/service/ChapterService.java), [`service/PageService.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/service/PageService.java)
- [`service/AiLookupService.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/service/AiLookupService.java)
- [`groqapi/GroqAiClient.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/groqapi/GroqAiClient.java)
- [`repository/BookRepository.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/repository/BookRepository.java), [`repository/ChapterRepository.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/repository/ChapterRepository.java), [`repository/PageRepository.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/repository/PageRepository.java)
- [`entity/Book.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/entity/Book.java), [`entity/Chapter.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/entity/Chapter.java), [`entity/Page.java`](https://github.com/tkderphu/english-learning/blob/main/src/main/java/site/viosmash/english/entity/Page.java)

#### Android App (Đăng nhập, Đăng ký, Quên mật khẩu)
- [`presentation/auth/LoginFragment.kt`](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/login/LoginFragment.kt), [`LoginViewModel.kt`](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/login/LoginViewModel.kt)
- [`presentation/auth/RegisterFragment.kt`](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/signup/SignUpFragment.kt), [`RegisterViewModel.kt`](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/signup/SignUpViewModel.kt)
- [`presentation/auth/ForgotPasswordFragment.kt`](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/forgotpassword/ForgotPasswordFragment.kt), [`ForgotPasswordViewModel.kt`](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/forgotpassword/ForgotPasswordViewModel.kt)
- [`data/remote/api/AuthApiService.kt`](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/data/remote/api/AuthApiService.kt)
- [`data/repository/AuthRepository.kt`](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/data/repository/AuthRepositoryImpl.kt)

#### Android App (Tra cứu từ vựng)
- [`presentation/reader/BookReaderFragment.kt`](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/readbook/ReadBookFragment.kt), [`BookReaderViewModel.kt`](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/readbook/ReadBookViewModel.kt)
- [`presentation/reader/DictionaryBottomSheetFragment.kt`](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/presentation/feature/readbook/lookup/ChooseDeckBottomSheet.kt)
- [`data/remote/api/DictionaryApiService.kt`](https://github.com/MADLearningEnglish/english_mobile/blob/main/app/src/main/java/com/mit/learning_english/data/remote/api/DictionaryApiService.kt)

#### Web Admin (Quản lý Sách)
- [`src/components/books/BookForm.jsx`](https://github.com/tkderphu/english-learning/blob/main/admin-portal/src/components/BookForm.jsx)
- [`src/components/books/ChapterForm.jsx`](https://github.com/tkderphu/english-learning/blob/main/admin-portal/src/components/ChapterForm.jsx)
- [`src/components/books/PageForm.jsx`](https://github.com/tkderphu/english-learning/blob/main/admin-portal/src/components/PageForm.jsx)
- [`src/api/BookApi.js`](https://github.com/tkderphu/english-learning/blob/main/admin-portal/src/api/axios.js)

## 2.2 Quy tắc comment code

Để đảm bảo tính nhất quán và dễ bảo trì cho dự án, toàn bộ các file cá nhân của tôi đều tuân thủ chặt chẽ chuẩn comment sau:

1. **Java (Backend) – Javadoc cho class và method:** Được áp dụng tại các tầng Controller và Service (ví dụ: `AuthService.java`). Javadoc mô tả mục đích hàm, các tham số (`@param`), giá trị trả về (`@return`) và các ngoại lệ có thể xảy ra.

2. **Kotlin (Android) – KDoc cho ViewModel và Repository:** Áp dụng tại tầng UI (ViewModel) và Data (Repository). KDoc mô tả cách thức hàm tác động đến trạng thái giao diện (UI State) hoặc nguồn truy xuất dữ liệu.

3. **Comment inline cho logic phức tạp:**
- Giải thích luồng sinh OTP: Tại `AuthService.java`, comment lý do tại sao OTP chỉ có thời hạn 5 phút và cách xóa OTP cũ đi trước khi sinh mới.
- Xử lý JWT Token: Comment về cách cấu hình Claims và bước kiểm tra chữ ký ở Filter bảo mật.
- Gọi AI Tra từ: Comment tại `AiLookupService` về việc tạo system prompt như thế nào để ép AI trả về dữ liệu chuẩn JSON.
- Coroutines (Android): Giải thích lý do sử dụng `viewModelScope.launch(Dispatchers.IO)` để đảm bảo luồng I/O mạng (Gọi API đăng nhập, tra từ) không bị giật lag trên Main Thread giao diện.

