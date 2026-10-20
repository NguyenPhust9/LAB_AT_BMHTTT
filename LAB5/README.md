Tạo mô hình mạng có pfSense để quản lý kết nối giữa WAN, LAN và DMZ. Sử dụng VirtualBox và các máy ảo để kiểm tra firewall rule.
Các phần thực hiện
- Tạo máy ảo pfSense CE 2.7.2 và cấu hình card mạng.
- Cấu hình địa chỉ IP LAN và DMZ, thiết lập NAT.
- Tạo rule để kiểm soát việc truy cập giữa các mạng.
- Kiểm tra các trường hợp: chặn truy cập từ DMZ vào LAN, chỉ cho một máy LAN ra Internet và port forward từ WAN đến web server trong DMZ.
Kiểm tra
Dùng các máy trong LAN và DMZ để thử ping, truy cập web và đối chiếu với các rule đã tạo. Nếu kết quả chưa đúng thì kiểm tra lại IP, thứ tự rule và firewall log.
Ghi chú
Các bước trên là nội dung cần thực hiện theo tài liệu LAB 5. Kết quả thực tế và ảnh minh chứng bổ sung sau khi chạy trên máy ảo.
