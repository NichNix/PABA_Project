PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE `news` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `imageUrl` TEXT NOT NULL);
INSERT INTO news VALUES(3,'Langganan Netflix dan Spotify Kena PPN 12 Persen','Pihak DJP Kemenkeu memastikan biaya langganan platform digital seperti Netflix, Spotify, hingga YouTube Premium akan dikenakan PPN sebesar 12 persen. Direktur Penyuluhan, Pelayanan, dan Hubungan Masyarakat Dirjen Pajak Kementerian Keuangan, Dwi Astuti, mengatakan PPN pada jasa layanan platform digital bukan pajak baru.','https://media-origin.kompas.tv/library/image/thumbnail/1735052013674/1735052013674.a_316_177.jpg');
INSERT INTO news VALUES(4,'OJK Cabut 20 Izin Usaha BPR/S pada 2024: untuk Lindungi Konsumen dan Perkuat Industri','Sepanjang tahun 2024, Otoritas Jasa Keuangan (OJK) telah mencabut izin usaha 20 bank perekonomian rakyat (BPR) dan bank perekonomian rakyat syariah (BPRS). Kepala Eksekutif Pengawas Perbankan (PBKN) OJK Dian Ediana Rae mengatakan, hal itu dilakukan untuk menjaga dan memperkuat industri BPR/BPRS.','https://media-origin.kompas.tv/library/image/content_article/desktop_thumbnail/20240229004928.jpg');
INSERT INTO news VALUES(5,'Harga Promo Minyak Goreng Selama Nataru di Beberapa Swalayan Indonesia, Serbu Diskonnya!','Selama masa Natal 2024 dan Tahun Baru 2025, sejumlah swalayan di Indonesia menawarkan harga promo untuk minyak goreng yang termasuk salah satu kebutuhan pokok masyarakat Indonesia.','https://media-origin.kompas.tv/library/image/content_article/desktop_thumbnail/20230203065533.jpg');
INSERT INTO news VALUES(6,'[FULL] Penjelasan Ditjen Pajak soal QRIS hingga E-Money Kena PPN 12 Persen | SERIAL HARGA NAIK','Direktorat Jenderal (Ditjen) Pajak Kementerian Keuangan menegaskan QRIS, uang elektronik atau e-money dan dompet digital atau e-wallet dikenakan PPN 12 persen. Namun, hanya saat transaksi penyerahan jasa sistem pembayaran saja.','https://media-origin.kompas.tv/library/image/thumbnail/1735013486721/1735013486721.a_316_177.jpg');
INSERT INTO news VALUES(8,'dummy test1','lorem ipsum abcdefgh','www.google.com');
INSERT INTO news VALUES(9,'dummy32','alksdjlaksjdlasd','www.ggoog');
COMMIT;