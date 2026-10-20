Mô hình
- pfSense-LAB6: 10.6.0.1 (LAN)
- WEB-PROXY: Ubuntu Server, 10.6.0.30
- DC01: 10.6.0.10 (DNS)
- Client-A/B: 10.6.0.21 và 10.6.0.22
- DMZ-WEB: 172.16.6.10
Client sẽ truy cập web qua proxy 10.6.0.30:8080.
3. Nội dung thực hiện
Mình đã tạo máy WEB-PROXY trên VirtualBox, nối vào mạng lab6-lan và đặt IP 10.6.0.30 tạm thời. Sau đó kiểm tra ping tới pfSense 10.6.0.1 thì thành công (4/4 gói, mất gói 0%).

Theo yêu cầu bài lab, các website china, mideast, jobs và blockedgame.lab7.test phải bị chặn. Các trang weapons, politics, religion và streaming.lab7.test hiện cảnh báo và cho phép bỏ chặn tạm. normal và education.lab7.test được truy cập. entertainment và games được giới hạn theo khung giờ 60 phút. File tải xuống cần được ClamAV kiểm tra và thử giới hạn 100 MB.
4. Kết quả và nhận xét
Hiện mình đã kết nối được WEB-PROXY với pfSense. Tuy nhiên chưa kiểm thử đầy đủ các chính sách chặn web, cảnh báo hay quét virus. IP WEB-PROXY cũng cần lưu cố định để không mất khi khởi động lại.

