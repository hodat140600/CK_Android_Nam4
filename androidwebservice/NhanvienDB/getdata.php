<?php 
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');


	$query = "SELECT * FROM nhanvien";
	$data = mysqli_query($connect, $query);

	//
	class NhanVien{
		public $MaNV;
		public $MaPK;
		public $TenNV;
		public $NgaySinh;
		public $Email;
		public function __construct($manv ,$mapk, $tennv, $ngaysinh, $email){
			$this->MaNV = $manv;
			$this->MaPK = $mapk;
			$this->TenNV = $tennv;
			$this->NgaySinh = $ngaysinh;
			$this->Email = $email;
		}
	}
	//
	$mangNV = array();
	// for ($i = 0; $i < mysqli_num_rows($data); $i++){
 //            array_push($mangPK, mysqli_fetch_assoc($data));
 //        }
	while ($row = mysqli_fetch_assoc($data)) {
		// code...
		array_push($mangNV, new NhanVien($row['MANV'], $row['MAPK'], $row['TENNV'],$row['NGAYSINH'], $row['EMAIL']));
	}
	echo json_encode($mangNV);

?>