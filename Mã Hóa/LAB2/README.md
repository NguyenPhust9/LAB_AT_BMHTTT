# LAB 2 - Mã hóa đối xứng hiện đại (VS Code)

Bài làm theo `LAB2_HuongDan.pdf`, dùng Java Swing viết trực tiếp bằng mã thay cho trình kéo thả NetBeans. Project nằm trong `demo`, package bắt buộc `week_02`, Java 17, Maven. Đã thực hiện cả hai bài nâng cao.

## Chạy trong VS Code

1. Mở **File → Open Folder → G:\Lab2**. Cài **Extension Pack for Java** và **Python** nếu VS Code đề nghị (máy hiện tại đã có).
2. Chờ VS Code tải project Maven. Nếu được hỏi, chọn import project Java.
3. Chọn **Run and Debug → LAB2 - Menu Java Swing → F5**. Hoặc mở `demo/src/main/java/week_02/frm_Menu.java`, bấm **Run** phía trên hàm `main`.
4. Nếu chỉ mở thư mục `demo`, dùng nút **Run** phía trên hàm `main`; cấu hình F5 và tasks đi kèm đặt ở thư mục gốc `G:\Lab2`.

Menu có DES, TripleDES, AES + Login, RC4, Twofish, AES-CTR, Kết nối SQL Server và Xem lịch sử. Đóng form con sẽ trở lại menu. Mọi giao diện đều chỉnh trực tiếp trong file `.java`, không cần file `.form` của NetBeans.

## Chạy từ terminal PowerShell

Tại `G:\Lab2`:

```powershell
.\mvnw.cmd -f demo/pom.xml verify
.\mvnw.cmd -f demo/pom.xml compile exec:java
```

Maven đã được tải trong `.tools`. Nếu chép bài sang máy khác, chạy `powershell -File scripts/setup-maven.ps1` một lần hoặc cài Maven vào PATH. Cần Internet ở lần tải thư viện đầu tiên. Có thể dùng **Ctrl+Shift+B** để build và kiểm thử; **Terminal → Run Task → LAB2: Chay Java Swing** để chạy.

## SQL Server và đăng nhập AES

Máy này dùng instance **SQLEXPRESS**, TCP **1433**. Tài liệu dùng **14333**; form cho phép sửa cổng nếu máy khác được cấu hình theo tài liệu.

Thông tin trên form **Kết nối SQL Server**:

| Trường | Giá trị |
|---|---|
| Server | `127.0.0.1` |
| Port | `1433` |
| Database | `LAB2_MaHoaHienDai` |
| User | `lab2_user` |
| Password | `LabUser@2026` (mật khẩu thực hành trong tài liệu) |

Bấm **Test Connection** rồi đóng form kết nối. Vào **2.3 AES + Login**, nhập username (3–40 ký tự chữ, số, `_`, `.`), password ít nhất 8 ký tự → **Register → Login**. Ticket hiện 32 ký tự HEX. Nhập **Key AES** là mật khẩu dùng mã hóa, rồi dùng Encrypt/Decrypt. Mật khẩu tài khoản và Key AES là hai trường độc lập. Logout xóa Ticket và quyền dùng AES của form đó.

Cấu hình kết nối chỉ nằm trong bộ nhớ, cần kết nối lại sau khi thoát ứng dụng. Mật khẩu tài khoản được lưu dưới dạng PBKDF2-HMAC-SHA256 (120.000 vòng), salt 16 byte và hash 32 byte. Lịch sử chỉ ghi thuật toán, thao tác, username và độ dài; thời gian lưu là UTC.

Khi cài trên máy khác, bật TCP/IP, Mixed Mode, đặt đúng cổng và chạy lần lượt trong SSMS bằng Windows Authentication:

1. `demo/sql/01_Tao_CSDL.sql`
2. `demo/sql/02_Tao_Tai_Khoan_SQL.sql`

Nếu cổng lỗi, kiểm tra dịch vụ và TCP/IP trong SQL Server Configuration Manager. Nếu `Login failed`, kiểm tra Mixed Mode, cổng và mật khẩu. Nếu trùng username, chọn tên khác. Các thuật toán khác vẫn chạy khi chưa kết nối SQL; trạng thái sẽ báo chưa lưu lịch sử.

## Python AES-256-CBC (bài nâng cao 02)

```powershell
python -m pip install -r demo/python/requirements.txt
python demo/python/aes_cbc_gui.py --self-test
python demo/python/aes_cbc_gui.py
```

Hoặc chọn **LAB2 - Python AES-CBC** và F5. Bấm Generate Key, Generate IV → nhập văn bản → Encrypt → xóa Plaintext → Decrypt. Save/Open lưu IV cùng bản mã, cần giữ Key riêng.

## Kiểm tra bài

| Bài | Dữ liệu và thao tác | Kết quả |
|---|---|---|
| DES | `Xin chào Lab 2 - Mã hóa hiện đại!`, key `12345678` | Giải mã đúng; hai lần Encrypt cho bản mã khác nhau |
| TripleDES | Key `123456789012345678901234` | Giải mã đúng; key 23 byte bị từ chối |
| AES-GCM | Register → Login → mã hóa và giải mã | Có Ticket; sai Key hoặc sửa bản mã bị phát hiện |
| RC4 | Plaintext `Plaintext`, Key `Key` | HEX `BBF316E8D940AF0AD3` |
| Twofish | Key `Twofish@Lab2`, Generate IV | Save/Open khôi phục IV và giải mã được |
| AES-CTR | Generate Key, Generate Nonce, văn bản > 1000 ký tự | Giải mã toàn văn; số byte bản mã bằng số byte UTF-8 bản rõ |
| Python AES-CBC | `--self-test` | In `SELF-TEST OK` |

Twofish lưu `IV=...` ở dòng đầu; AES-CTR lưu `NONCE=...`; Python CBC lưu `IV=...`. Các tệp không lưu khóa. Với CTR, tạo nonce mới cho mỗi lần mã hóa; Twofish/CBC cũng cần IV mới. DES/TripleDES tự sinh và đính IV vào bản mã. DES, TripleDES và RC4 chỉ dùng cho bài học; CBC/CTR không có cơ chế kiểm tra sửa đổi như GCM.

Kiểm thử tự động: 8 test thuật toán trong `CryptoTest`. Có thêm kiểm thử SQL thật trong `SqlIntegrationTest`, mặc định bỏ qua nếu chưa cung cấp biến môi trường:

```powershell
$env:LAB2_DB_PASSWORD = 'LabUser@2026'
$env:LAB2_DB_PORT = '1433'
.\mvnw.cmd -f demo/pom.xml verify
Remove-Item Env:LAB2_DB_PASSWORD
```

Test SQL kiểm tra đăng ký, username trùng, mật khẩu đúng/sai, salt/hash và ghi/đọc lịch sử. Nó xóa riêng dữ liệu thử do nó tạo sau khi chạy.

Đã xác minh trên máy này: `BUILD SUCCESS`, cả 9 test Java đều qua; Python in `SELF-TEST OK`; mở đủ 9 cửa sổ Swing và thử Encrypt/Decrypt trên các form không cần đăng nhập. SQLEXPRESS đã bật Mixed Mode và khởi động lại theo xác nhận của người dùng. CSDL `LAB2_MaHoaHienDai` và tài khoản lab được tạo trên SQLEXPRESS (instance dùng để chạy bài) và instance mặc định trong quá trình kiểm tra kết nối; hai CSDL độc lập.

Mã xử lý ở `demo/src/main/java/week_02/*Cipher.java`; giao diện ở `frm_*.java`; bố cục và sự kiện dùng chung ở `LabFrame.java`, `CipherFrame.java`; kết nối và truy vấn ở `DbConfig.java`, `UserDao.java`, `HistoryDao.java`.
