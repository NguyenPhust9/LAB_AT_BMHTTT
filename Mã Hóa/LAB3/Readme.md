LAB3_MaHoaKhoaCongKhai

Java 17 / Maven, package week_03: RSA, ECC, RSA+AES, ECDSA (bài 3.1–3.5) và lịch sử SQL Server.

Chạy
Chạy Chay_Lab3.cmd để mở menu.
Mở SQL Server: localhost, cổng 14333, database LAB3_MaHoaKhoaCongKhai, Windows Authentication, bấm Test Connection (làm lại mỗi lần mở app).
Chọn bài: Generate Keys → Encrypt → Decrypt (ECDSA: Sign → Verify).
Xem Lịch sử thao tác (dbo.History).

RSA console: Chay_RSA_Console.cmd.

Build và kiểm thử
powershell
mvn clean verify
java '-Djava.library.path=native' -jar target/LAB3_MaHoaKhoaCongKhai-1.0-SNAPSHOT.jar

Test SQL thật: thêm '-Dlab3.integration=true' '-DargLine=-Djava.library.path=native' vào lệnh mvn verify.

Kết quả: 17 tests, 0 lỗi. NetBeans: Open Project (thư mục có pom.xml), JDK 17+, chạy lớp week_03.frm_Menu.

Kết quả cần thấy
Bài	Kết quả
3.1	Giới hạn RSA-OAEP: 190 byte (2048 bit), 318 byte (3072 bit)
3.2	Mã hóa hai lần ra hai bản mã khác nhau, giải mã đúng
3.3	ECC giải mã đúng; sai khóa thì thất bại
3.4	Khôi phục đủ dữ liệu mẫu; sửa bản mã thì GCM từ chối
3.5	Đúng: VALID; sửa nội dung hoặc khác khóa: INVALID
Khóa
Save Keys: mật khẩu ≥ 8 ký tự, khóa riêng mã hóa bằng PBKDF2 + AES-256-GCM.
Load Keys: sai mật khẩu hoặc sai loại khóa sẽ bị từ chối.
Xuất khóa công khai: chỉ mã hóa/xác minh được.
Không nộp kèm khóa riêng.
SQL Server
Chạy 03_SQL_SERVER/01_Tao_CSDL.sql để tạo database và bảng (giữ dữ liệu cũ).
03_KiemTra_LichSu.sql: xem lịch sử.
Máy khác: bật TCP/IP, sửa cấu hình trên form. DLL chỉ dành cho Windows x64.
Log không chứa thông điệp, khóa hay mật khẩu.
