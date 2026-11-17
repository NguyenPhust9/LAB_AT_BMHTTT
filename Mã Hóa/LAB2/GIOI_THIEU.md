# LAB 2 - MÃ HÓA ĐỐI XỨNG HIỆN ĐẠI

## Thông tin sinh viên

- Họ và tên: Nguyễn Văn Phú
- MSSV: 1150080030

## Giới thiệu

Chương trình được xây dựng bằng Java Swing trên VS Code, gồm:
- DES, TripleDES, AES-GCM, RC4, Twofish.
- Bài nâng cao AES-CTR bằng Java.
- Bài nâng cao AES-CBC bằng Python.
- Đăng ký, đăng nhập và lưu lịch sử bằng SQL Server.

## Yêu cầu

- JDK 17 trở lên.
- VS Code và Extension Pack for Java.
- SQL Server để sử dụng đăng ký, đăng nhập và lịch sử.
- Python 3 và thư viện cryptography để chạy AES-CBC.

## Chạy chương trình Java

1. Mở thư mục `Lab2` trong VS Code.
2. Chờ VS Code tải dự án Maven.
3. Chọn Run and Debug → LAB2 - Menu Java Swing.
4. Nhấn F5 để chạy.
5. Chọn thuật toán trên menu để sử dụng.

Hoặc mở terminal tại thư mục `Lab2` và chạy:

```powershell
.\mvnw.cmd -f demo/pom.xml compile exec:java
```

Nếu chưa có Maven, chạy trước:

```powershell
powershell -File scripts/setup-maven.ps1
```

## Kết nối SQL Server

Trên máy khác, bật TCP/IP và Mixed Mode cho SQL Server,
sau đó chạy lần lượt hai tệp bằng tài khoản quản trị trong SSMS:

1. `demo/sql/01_Tao_CSDL.sql`
2. `demo/sql/02_Tao_Tai_Khoan_SQL.sql`

Mở mục Kết nối SQL Server trong chương trình và nhập:

- Server: 127.0.0.1
- Port: 1433
- Database: LAB2_MaHoaHienDai
- User: lab2_user
- Password: LabUser@2026

Cổng phải trùng với cấu hình SQL Server thực tế.

Bấm Test Connection. Sau khi kết nối thành công:
- Mở AES + Login.
- Nhập username và mật khẩu ít nhất 8 ký tự.
- Bấm Register, sau đó Login để nhận Ticket.
- Nhập Key AES để mã hóa hoặc giải mã.
- Chọn Xem lịch sử để xem các thao tác đã lưu.

## Sử dụng các thuật toán

1. Nhập Plaintext và Key.
2. Với Twofish, bấm Generate IV.
3. Với AES-CTR, bấm Generate Key và Generate Nonce.
4. Bấm Encrypt để mã hóa.
5. Xóa Plaintext rồi bấm Decrypt để kiểm tra.
6. Dùng Save File và Open File để lưu hoặc mở bản mã.

Giữ lại đúng Key để giải mã. Tệp bản mã không lưu Key.
Tạo IV hoặc nonce mới cho mỗi lần mã hóa.

## Chạy Python AES-CBC

Mở terminal tại thư mục `Lab2`:

```powershell
python -m pip install -r demo/python/requirements.txt
python demo/python/aes_cbc_gui.py
```

Bấm Generate Key và Generate IV, nhập văn bản rồi sử dụng
Encrypt, Decrypt, Save File hoặc Open File.

## Kiểm thử

Kiểm thử Java:

```powershell
.\mvnw.cmd -f demo/pom.xml verify
```

Tự kiểm thử Python:

```powershell
python demo/python/aes_cbc_gui.py --self-test
```

Kết quả Python thành công sẽ hiển thị SELF-TEST OK.
