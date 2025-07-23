// Call the dataTables jQuery plugin
$(document).ready(function() {
  $('#dataTable').DataTable();
});

// === TỰ ĐỘNG HIỂN THỊ DỮ LIỆU ADMIN ===

function renderTable(selector, data, columns) {
    let html = '';
    data.forEach(function(item, idx) {
        html += '<tr>';
        columns.forEach(function(col) {
            html += `<td>${item[col] !== undefined ? item[col] : ''}</td>`;
        });
        html += '</tr>';
    });
    $(`${selector} tbody`).html(html);
    $(selector).DataTable();
}

$(document).ready(function() {
    // TOUR
    if ($('#dataTable.tour-table').length) {
        $.get('/api/tour/getAllTour?pageIndex=0', function(res) {
            renderTable('#dataTable', res.data, ['id','ten_tour','so_ngay','diem_khoi_hanh','gia_tour','trang_thai']);
        });
    }
    // USER (admin)
    if ($('#dataTable.user-table').length) {
        $.get('/api/admin/user/getAll?pageIndex=0', function(res) {
            renderTable('#dataTable', res.data, ['id','username','ho_ten','email','sdt','role_id']);
        });
    }
    // BOOKING
    if ($('#dataTable.booking-table').length) {
        $.get('/api/booking/getAllBooking?pageIndex=0', function(res) {
            renderTable('#dataTable', res.data, ['id','user_id','tour_id','so_luong_nguoi','tong_tien','trang_thai']);
        });
    }
    // TIN TỨC
    if ($('#dataTable.tintuc-table').length) {
        $.get('/api/tintuc/getAllPage?pageIndex=0', function(res) {
            renderTable('#dataTable', res.data, ['id','tieuDe','tom_tat','ngay_dang','trang_thai']);
        });
    }
});
