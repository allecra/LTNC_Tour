-- Cập nhật trạng thái tour Tokyo để hiển thị trên trang chủ
USE `jsb_tour`;

UPDATE `tour` 
SET `trang_thai` = 'dang_mo_ban' 
WHERE `id` = 4 AND `ten_tour` = 'Khám phá Tokyo và núi Phú Sĩ';

-- Kiểm tra kết quả
SELECT id, ten_tour, trang_thai, tour_type_id 
FROM tour 
WHERE tour_type_id = 2; 