# LAB 6 – Xác thực Active Directory và chính sách truy cập trên pfSense

Môn: Thực hành An toàn hệ thống thông tin
Sinh viên: NGUYỄN VĂN PHÚ – MSSV: 1150080030 – Lớp: CNPM1
Ngày thực hiện: 13/10/2026

## 1. Mục tiêu
Dựng pfSense riêng cho LAB 6 (4 interface), tích hợp AD qua LDAP, Captive Portal,
Alias/Schedule và kiểm thử 7 kịch bản chính sách truy cập.

## 2. Mô hình mạng

| Thiết bị | Interface | Địa chỉ | Vai trò |
|---|---|---|---|
| pfSense-LAB6 | WAN (em0) | DHCP 192.168.250.x/24 | Ra Internet qua NAT Network LAB6-WAN |
| pfSense-LAB6 | MGMT (em1) | 192.168.56.254/24 | WebGUI cho máy thật (192.168.56.1) |
| pfSense-LAB6 | LAN (em2) | 10.6.0.1/24 | Gateway LAN-DOMAIN |
| pfSense-LAB6 | DMZ (em3) | 172.16.6.1/24 | Gateway DMZ |
| DC01 | – | 10.6.0.10/24 | AD DS + DNS, domain lab6.test |
| Client-A | – | 10.6.0.21/24 | Đại diện sv_a |
| Client-B | – | 10.6.0.22/24 | Đại diện sv_b |
| DMZ-WEB | – | 172.16.6.10 (+ .20/.30/.40) | Nginx, mô phỏng website |

## 3. Môi trường ảo hóa (VirtualBox)
- NAT Network `LAB6-WAN`: 192.168.250.0/24, bật DHCP
- Host-Only: 192.168.56.0/24, tắt DHCP server
- Internal Network: `lab6-lan`, `lab6-dmz`
- VM pfSense-LAB6: pfSense CE 2.7.2, 2 GB RAM, 2 vCPU, 16 GB disk (ZFS stripe)

### Bảng adapter của pfSense-LAB6
| Adapter | Kiểu | Tên mạng | MAC | Interface pfSense |
|---|---|---|---|---|
| 1 | NAT Network | LAB6-WAN | 08:00:27:4D:E3:32 | em0 = WAN |
| 2 | Host-Only | VirtualBox Host-Only Ethernet Adapter | 08:00:27:21:42:B0 | em1 = MGMT |
| 3 | Internal | lab6-lan | 08:00:27:A9:12:8E | em2 = LAN |
| 4 | Internal | lab6-dmz | 08:00:27:76:4F:10 | em3 = DMZ |

Gán interface theo MAC, không theo tên em0/em1.

## 4. Tiến độ
- [x] B1. Tạo mạng ảo VirtualBox
- [x] B2. Tạo VM pfSense-LAB6, gán 4 adapter
- [x] B3. Cài pfSense, gán interface, đặt IP (console)
- [x] B4. Vào WebGUI qua https://192.168.56.254
- [ ] B4b. Rule MGMT, timezone, bỏ block private/bogon trên WAN, xóa rule allow-all trên LAN
- [ ] B5. DC01 (AD DS, DNS, OU LAB6-Users, sv_a, sv_b, svc_pfsense_ldap)
- [ ] B6. Client-A, Client-B
- [ ] B7. DMZ-WEB (Nginx + IP .20/.30/.40)
- [ ] C. LDAP (Diagnostics → Authentication: sv_a, sv_b)
- [ ] D. Captive Portal LAB6_USERS
- [ ] E. Alias
- [ ] F. Schedule
- [ ] G. Kịch bản 1–7
- [ ] H. Ruleset tổng hợp + bảng kiểm thử bắt buộc
- [ ] Xuất config.xml

## 5. Ghi chú cấu hình
- Timezone: Asia/Ho_Chi_Minh (cần cho Schedule)
- WAN: tắt "Block private networks" và "Block bogon networks" vì WAN thuộc dải 192.168.x
- MGMT: rule Pass TCP 192.168.56.1 → This Firewall : 443
- LAN: đã xóa rule "Default allow LAN to any" (IPv4 và IPv6)

## 6. Ruleset LAN (thứ tự first-match)
| # | Action | Source | Destination / Port | Schedule | Log |
|---|---|---|---|---|---|
| 1 | Block | CLIENT_A | JOB_SITE:80 | – | ✔ |
| 2 | Block | POLICY_CLIENTS | RESTRICTED_SITE:80 | – | ✔ |
| 3 | Pass | CLIENT_B | SOCIAL_SITE:80 | LUNCH_WINDOW | |
| 4 | Block | CLIENT_B | SOCIAL_SITE:80 | – | ✔ |
| 5 | Pass | POLICY_CLIENTS | GENERAL_SITE:80 | – | |
| 6 | Pass | POLICY_CLIENTS | Any : WEB_PORTS | WORK_HOURS | |
| 7 | Block | POLICY_CLIENTS | Any | – | ✔ |

## 7. Kết quả kiểm thử
| Nguồn | Đích | Dự đoán | Thực tế | Ảnh |
|---|---|---|---|---|
| Client-A | 172.16.6.10 | Allow | | |
| Client-A | 172.16.6.20 | Block | | |
| Client-A/B | 172.16.6.40 | Block | | |
| Client-B | 172.16.6.30 (trong giờ nghỉ) | Allow | | |
| Client-B | 172.16.6.30 (ngoài giờ) | Block | | |
| DMZ-WEB | 10.6.0.10 | Block | | |

## 8. Sự cố đã gặp và cách xử lý
| Hiện tượng | Nguyên nhân | Cách xử lý |
|---|---|---|
| Sau cài đặt lại hiện màn hình Install | ISO vẫn gắn, máy boot lại từ ISO | Remove ISO khỏi Optical Drive rồi Reset |
| ZFS báo "Not enough disks selected" | Chưa tick ổ ada0 | Bấm Space để đánh dấu [*] ada0, rồi OK |
| Mặc định pfSense gán LAN = em1 | em1 thực ra là Adapter 2 (Host-Only) | Assign Interfaces lại theo MAC |
| Không vào được WebGUI sau khi Apply | Firewall chặn MGMT khi chưa có rule | `pfctl -d` qua console, rồi tạo rule MGMT |

## 9. Ảnh minh chứng
Lưu trong thư mục `screenshots/`, đặt tên theo bước, ví dụ `B2-adapters.png`,
`C-ldap-sv_a.png`, `G1-rule.png`, `G1-test.png`.

## 10. Tệp kèm theo
- `config.xml` (Diagnostics → Backup & Restore)
- Báo cáo `[Mã lớp]-Lab6_MSSV-TenSV.docx`
