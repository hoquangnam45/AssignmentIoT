# Nội dung họp ngày 14/10
Nội dung bài tập lớn: Tủ thông minh - Smart Locker

Mô tả :  
+ Người dùng thẻ có thể dùng tủ công cộng để đựng đồ cá nhân hoặc để cho shipper giao hàng. Hệ thống sẽ bao gồm 1 tủ gồm 4 ngăn + 1 ứng dụng điện thoại di động + 1 server lưu dữ liệu (Tình trạng tủ: Trống hay Đang sử dụng, Thông tin cá nhân của người sử dụng tủ, One-time-password để mở tủ trong trường hợp shipper cần giao hàng: Có thể có hoặc không) + 1 trang web dashboard để quản lý hệ thống tủ dành cho admin. Mỗi ngưới có 1 thẻ RFID để quét thẻ truy cập tủ cá nhân của riêng mình.
+ Khi shipper giao hàng thì người sử dụng tủ sẽ có thể yêu cầu sinh One-time-password để cho phép shipper có thể mở tủ giao hàng  

Video tham khảo cách hoạt động:  
+ https://www.youtube.com/watch?v=PgmNd5A9yro
+ https://www.youtube.com/watch?v=YrVJpChzLbk

Phân chia công việc:  
+ Quân: Mở khóa tủ và + làm bàn phím
+ Kiên: Làm phần quét thẻ + Làm cái tủ và cơ chế mở khóa tủ
+ Phong: Làm server + CSDL + Web
+ Sơn: Làm app
+ Nam: hiển thị ra màn hình LCD tủ

# Nội dung họp ngày 28/10. Điều chỉnh chức năng chi tiết của hệ thống như sau:

Video: https://www.youtube.com/watch?v=wZbFyvaQC6k&fbclid=IwAR26ngyIOuXrkIme8JqBJr9aZ-UXoPRvH1pd9eeuEo_41YiDXeq5D8I-P5M  

Mô tả chung: Hệ thống thuộc về đơn vị vận chuyển (ship hàng), đơn vị sẽ đặt tại mỗi chung cư một cái tủ(phân biệt tủ với hộc tủ)  

#### Phần ứng dụng android cho shipper:  
- Khi cần giao hàng, shipper đăng nhập ứng dụng bằng Account và Password.  
- Server kiểm tra tính hợp lệ của Account và Password, trả về đăng nhập Success hoặc Fail.  
1. Khi thành công, server gửi về code 0, ID của shipper và cơ sở dữ liệu về hệ thống tủ:  
```
{  
    "Code": "0",  
    "shipper": {  
        "id": "111222",  
        "name": "Ho Quang Nam",  
        "Age": "20"  
    }  
    "dresserDatabase": [  
        {  
            "dresser": "A5555",  
            "address": "268 Ly Thuong Kiet, q10, TPHCM",  
            "numberDrawers": 4,  
            "drawers": [  
                {  
                    "no": "0",  
                    "size": "small",  
                    "status": "available"  
                },  
                {  
                    "no": "1",  
                    "size": "small",  
                    "status": "occupied"  
                },  
                {  
                    "no": "2",  
                    "size": "medium",  
                    "status": "available"  
                },  
                {  
                    "no": "3",  
                    "size": "small",  
                    "status": "booked"  
                }  
            ]  
        },  
        {  
            "dresser": "T2323",  
            "address": "123 Au Co, quan Tan Binh, TPHCM",  
            "numberDrawers": 6,  
            "drawers": [  
                {  
                    "no: "1",  
                    "size": "small",  
                    "status": "available"  
                },  
                ...  
                ...  
            ]  
        },  
    ]  
}   
```
2. Khi thất bại, server gửi về code 1:  
```
{  
    "Code": "1"  
}  
```
- Ứng dụng hiển cơ sở dữ liệu theo cách trực quan nào đó cho shipper, shipper chọn tủ mà anh ta cần giao hàng tới, danh sách hộc tủ (còn trống), số điện thoại, địa chỉ email của người nhận tương ứng từng hộc tủ. Một record có dạng như sau:  
```
{  
    "shipperId": "111222",  
    "dresser": "A5555",  
    "drawers": [  
        {  
            "no": "1",  
            "receiverEmail": "truongson@gmail.com",  
            "receiverPhone": "0961111111"  
        },  
        {   
            "no": "2",  
            "receiverEmail": "trungkien@gmail.com",  
            "receiverPhone": "0962222222"  
        },  
    ]  
}  
```
- Server nhận thông tin và làm 2 việc  
1. Sinh mã OTP(one time password) cho shipper, lưu vào cơ sở dữ liệu record:  
```
{  
    "OTP": "11223344",  
    "shipperId": "111222",  
    "dresserId": "A5555",  
    "drawers": ["1","2"]  
}  
```
sau đó gửi cho ứng dụng OTP vừa sinh ra.  
```
{  
    "OTP": "11223344"  
}  
```
1. Sinh ID, mã OTP cho từng người nhận hàng và lưu vào database:  
```
{  
    "receiverEmail": "truongson@gmail.com",  
    "receiverPhone": "0961111111",  
    "id": "222333",  
    "OTP": "66778899",  
    "drawers": ["1"]  
}  
```
và  
```  
{  
    "receiverEmail": "trungkien@gmail.com",  
    "receiverPhone": "0962222222",  
    "id": "333444",  
    "OTP": "33557799",  
    "drawers": ["2"]  
}  
```
#### Phần ứng dụng của tủ: tủ sẽ luôn hiển thì hai lựa chọn là "Send" và "Pick-up".  
- Shipper chọn "Send", ứng dụng sẽ yêu cầu nhập ID, OTP. Sau khi nhận ID, OTP ứng dụng sẽ gửi dữ liệu lên server  
```
{  
    "OTP": "11223344",  
    "shipperId": "111222",  
    "dresserId": "A5555"  
}  
```
server sẽ kiểm tra:  
1. Thành công, server gửi về danh sách tủ đã cấp  
```
{  
    "code": "0",  
    "drawers": ["1", "2"]  
}  
```
2. Thất bại, server gửi về   
```
{  
    "code": "-1"  
}  
```
khi thành công, tủ tự động mở những hộc tủ như đã cấp. Ứng dụng chuyển sang màn hình yêu cầu shipper xác nhận đã bỏ hàng vào rồi. Sau khi shipper xác nhận, tủ sẽ gửi ID tủ, OTP lên server  
```
{  
    "OTP": "11223344",  
    "dresserId": "A5555"  
}  
```
yêu cầu đồng bộ, server sẽ chuyển các hộc tủ sang trạng thái hiện đang chứa hàng; đồng thời server sẽ gửi thông tin đã có hàng qua email, tin nhắn cho người nhận. Tủ chuyển sang màn hình chờ.  

- Người nhận hàng chọn "Pick-up", ứng dụng yêu cầu nhập ID người nhận, OTP. Ứng dụng gửi thông tin này lên server 
``` 
{  
    "OTP": "66778899",  
    "receiverId": "222333",  
    "dresserId": "A5555"  
}  
```
server sẽ kiểm tra:  
1. Thành công: server gửi về danh sách hộc tủ  
```
{  
    "code": "0",  
    "drawers": ["2"]  
}  
```
tủ sẽ tự động mở những hộc tủ này.  
2. Thất bại, server gửi về mã 0  
```
{  
    "code": "-1"  
}  
```
khi thành công, tủ tự động mở những hộc tủ như đã cấp. Ứng dụng chuyển sang màn hình yêu cầu người nhận xác nhận đã lấy hàng. Sau khi shipper xác nhận, tủ sẽ gửi ID tủ, OTP lên server  
```
{  
    "OTP": "66778899",  
    "dresserId": "A5555"  
}  
```
yêu cầu đồng bộ, server sẽ chuyển các hộc tủ sang trạng thái trống. Tủ chuyển sang màn hình chờ.  


