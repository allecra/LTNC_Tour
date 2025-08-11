# Voucher API Documentation

## Tổng quan
Hệ thống đã được cập nhật để hỗ trợ đầy đủ chức năng voucher trong booking. Các API sau đây đã được thêm mới:

## 1. Kiểm tra voucher hợp lệ
**Endpoint:** `POST /api/user/voucher/validate`

**Request Body:**
```json
{
    "voucherCode": "DISCOUNT20"
}
```

**Response:**
- **Success (200):** `{"message": "Voucher hợp lệ", "data": true}`
- **Error (400):** `{"message": "Voucher không hợp lệ hoặc đã hết hạn", "data": false}`

## 2. Tính toán giá sau khi áp dụng voucher
**Endpoint:** `POST /api/user/voucher/calculate`

**Request Body:**
```json
{
    "originalPrice": 1000000,
    "voucherCode": "DISCOUNT20"
}
```

**Response:**
```json
{
    "message": "Tính toán giá thành công",
    "data": {
        "originalPrice": 1000000,
        "finalPrice": 800000,
        "discountAmount": 200000,
        "voucherCode": "DISCOUNT20"
    }
}
```

## 3. Tạo booking mới với voucher
**Endpoint:** `POST /api/user/{userId}/bookings`

**Request Body:**
```json
{
    "tourId": 1,
    "soLuongNguoi": 2,
    "paymentMethod": "Chuyển khoản",
    "ghiChu": "Ghi chú đặt tour",
    "voucherCode": "DISCOUNT20",
    "ngayKhoiHanh": "2024-12-25T08:00:00"
}
```

**Response:**
- **Success (201):** `{"message": "Tạo booking thành công", "data": null}`
- **Error (400):** `{"message": "Tạo booking thất bại", "data": null}`

## 4. Lấy lịch sử booking với thông tin voucher
**Endpoint:** `GET /api/user/{userId}/bookings`

**Response:**
```json
{
    "message": "Lấy lịch sử đặt tour thành công",
    "data": [
        {
            "id": 1,
            "user_id": 1,
            "tour_id": 1,
            "ten_tour": "Tour Đà Nẵng - Hội An",
            "so_luong_nguoi": 2,
            "tong_tien": 800000,
            "trang_thai": "cho_xac_nhan",
            "payment_method": "Chuyển khoản",
            "voucherCode": "DISCOUNT20",
            "giaTriVoucher": 20,
            "tongTienSauVoucher": 800000
        }
    ]
}
```

## Cấu trúc dữ liệu

### Voucher Entity
```java
@Entity
@Table(name = "voucher", schema = "jsb_tour")
public class Voucher {
    private Long id;
    private String maGiamGia;        // Mã giảm giá
    private BigDecimal giaTri;       // Giá trị giảm (%)
    private Date ngayHetHan;         // Ngày hết hạn
    private String dieuKienApDung;   // Điều kiện áp dụng
}
```

### Booking Entity (đã cập nhật)
```java
@Entity
@Table(name = "booking", schema = "jsb_tour")
public class Booking {
    // ... các trường khác
    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;         // Quan hệ với voucher
}
```

## Logic xử lý voucher

1. **Validation:** Kiểm tra voucher có tồn tại và còn hạn sử dụng không
2. **Tính toán:** Giá sau voucher = Giá gốc - (Giá gốc × Giá trị voucher / 100)
3. **Lưu trữ:** Lưu thông tin voucher vào booking
4. **Hiển thị:** Trả về thông tin đầy đủ bao gồm giá gốc, giá sau voucher, và số tiền được giảm

## Lưu ý quan trọng

- Voucher được tính theo phần trăm (%)
- Hệ thống tự động kiểm tra hạn sử dụng voucher
- Giá sau voucher không được âm (tối thiểu = 0)
- Mỗi booking chỉ áp dụng được 1 voucher
- Voucher code phải có ít nhất 3 ký tự

## Error Handling

- **Voucher không tồn tại:** Trả về lỗi 400 với thông báo phù hợp
- **Voucher hết hạn:** Trả về lỗi 400 và ghi log warning
- **Lỗi hệ thống:** Trả về lỗi 500 với thông báo lỗi chi tiết
- **Validation error:** Trả về lỗi 400 với thông báo validation cụ thể
