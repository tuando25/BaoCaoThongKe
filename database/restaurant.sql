CREATE DATABASE IF NOT EXISTS db_restaurant CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db_restaurant;
-- Table for Users (Nhan vien / Quan ly)
CREATE TABLE IF NOT EXISTS tblUser (
    id INT AUTO_INCREMENT PRIMARY KEY,
    taiKhoan VARCHAR(50) NOT NULL UNIQUE,
    matKhau VARCHAR(50) NOT NULL,
    tenNV VARCHAR(100) NOT NULL,
    chucVu VARCHAR(50) NOT NULL
);
-- Table for Ban (Dining Table)
CREATE TABLE IF NOT EXISTS tblBan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tenBan VARCHAR(50) NOT NULL,
    kieu VARCHAR(50),
    khuVuc VARCHAR(100)
);
-- Table for MonAn (Dish)
CREATE TABLE IF NOT EXISTS tblMonAn (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tenMon VARCHAR(100) NOT NULL,
    danhMuc VARCHAR(100),
    donGia FLOAT NOT NULL
);
-- Table for KhachHang (Client)
CREATE TABLE IF NOT EXISTS tblKhachHang (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tenKH VARCHAR(100) NOT NULL,
    soDT VARCHAR(20)
);
-- Table for DichVu (Service)
CREATE TABLE IF NOT EXISTS tblDichVu (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tenDichVu VARCHAR(100) NOT NULL,
    donGia FLOAT NOT NULL
);
-- Table for GiaoDich (Transaction)
CREATE TABLE IF NOT EXISTS tblGiaoDich (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ngayGiaoDich DATETIME NOT NULL,
    tongTien FLOAT NOT NULL,
    idBan INT,
    idKhachHang INT,
    idUser INT,
    FOREIGN KEY (idBan) REFERENCES tblBan(id) ON DELETE SET NULL,
    FOREIGN KEY (idKhachHang) REFERENCES tblKhachHang(id) ON DELETE SET NULL,
    FOREIGN KEY (idUser) REFERENCES tblUser(id) ON DELETE SET NULL
);
-- Table for ChiTietGiaoDich (Dish details in transaction)
CREATE TABLE IF NOT EXISTS tblChiTietGiaoDich (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idGiaoDich INT NOT NULL,
    idMonAn INT,
    soLuong INT NOT NULL,
    giaBan FLOAT NOT NULL,
    FOREIGN KEY (idGiaoDich) REFERENCES tblGiaoDich(id) ON DELETE CASCADE,
    FOREIGN KEY (idMonAn) REFERENCES tblMonAn(id) ON DELETE SET NULL
);
-- Table for UsedService (Service details in transaction)
CREATE TABLE IF NOT EXISTS tblUsedService (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idGiaoDich INT NOT NULL,
    idDichVu INT,
    soLuong INT NOT NULL,
    thanhTien FLOAT NOT NULL,
    FOREIGN KEY (idGiaoDich) REFERENCES tblGiaoDich(id) ON DELETE CASCADE,
    FOREIGN KEY (idDichVu) REFERENCES tblDichVu(id) ON DELETE SET NULL
);
-- Insert sample data
INSERT INTO tblUser (taiKhoan, matKhau, tenNV, chucVu) VALUES 
('admin', 'admin123', 'Nguyen Van A', 'manager'),
('seller1', '123456', 'Tran Thi B', 'seller');
INSERT INTO tblBan (tenBan, kieu, khuVuc) VALUES 
('Ban 01', 'Ban 4 nguoi', 'Khu A'),
('Ban 02', 'Ban 4 nguoi', 'Khu A'),
('Ban 03', 'Ban VIP', 'Khu VIP'),
('Ban 04', 'Ban 2 nguoi', 'Khu B');
INSERT INTO tblMonAn (tenMon, danhMuc, donGia) VALUES 
('Pho Bo', 'Mon nuoc', 50000),
('Com Tam', 'Mon kho', 45000),
('Nem Cuon', 'Khai vi', 30000),
('Lau Thai', 'Lau', 250000);
INSERT INTO tblKhachHang (tenKH, soDT) VALUES 
('Nguyen Khach Le', '0912345678'),
('Pham Khach VIP', '0988888888');
INSERT INTO tblDichVu (tenDichVu, donGia) VALUES 
('Khan lanh', 3000),
('Nuoc suoi', 10000),
('Bia Sai Gon', 20000);
-- Sample Transactions for May 2026 (For testing statistcs range 01/05/2026 to 31/05/2026)
-- Giao dich 1: Ban 01, KhachHang 1, User 1, Date: 2026-05-10
INSERT INTO tblGiaoDich (id, ngayGiaoDich, tongTien, idBan, idKhachHang, idUser) VALUES
(1, '2026-05-10 12:30:00', 143000, 1, 1, 1);
INSERT INTO tblChiTietGiaoDich (idGiaoDich, idMonAn, soLuong, giaBan) VALUES
(1, 1, 2, 50000), -- 2 Pho Bo = 100000
(1, 3, 1, 30000); -- 1 Nem Cuon = 30000
INSERT INTO tblUsedService (idGiaoDich, idDichVu, soLuong, thanhTien) VALUES
(1, 1, 1, 3000),  -- 1 Khan lanh = 3000
(1, 2, 1, 10000); -- 1 Nuoc suoi = 10000
-- Giao dich 2: Ban 01, KhachHang 2, User 1, Date: 2026-05-15
INSERT INTO tblGiaoDich (id, ngayGiaoDich, tongTien, idBan, idKhachHang, idUser) VALUES
(2, '2026-05-15 19:00:00', 313000, 1, 2, 1);
INSERT INTO tblChiTietGiaoDich (idGiaoDich, idMonAn, soLuong, giaBan) VALUES
(2, 4, 1, 250000), -- 1 Lau Thai = 250000
(2, 3, 1, 30000);  -- 1 Nem Cuon = 30000
INSERT INTO tblUsedService (idGiaoDich, idDichVu, soLuong, thanhTien) VALUES
(2, 1, 1, 3000),   -- 1 Khan lanh = 3000
(2, 3, 1, 20000),  -- 1 Bia Sai Gon = 20000
(2, 2, 1, 10000);  -- 1 Nuoc suoi = 10000
-- Giao dich 3: Ban 02, KhachHang 1, User 1, Date: 2026-05-20
INSERT INTO tblGiaoDich (id, ngayGiaoDich, tongTien, idBan, idKhachHang, idUser) VALUES
(3, '2026-05-20 18:30:00', 103000, 2, 1, 1);
INSERT INTO tblChiTietGiaoDich (idGiaoDich, idMonAn, soLuong, giaBan) VALUES
(3, 2, 2, 45000);  -- 2 Com Tam = 90000
INSERT INTO tblUsedService (idGiaoDich, idDichVu, soLuong, thanhTien) VALUES
(3, 1, 1, 3000),   -- 1 Khan lanh = 3000
(3, 2, 1, 10000);  -- 1 Nuoc suoi = 10000
