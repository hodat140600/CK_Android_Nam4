<?php 
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$query = "SELECT * FROM vattu";
	$data = mysqli_query($connect, $query);

	//
	class VatTu{
		public $MaVT;
		public $TenVT;
		public $Dvt;
		public $Hinh;
		public $GiaNhap;
		public function __construct($mavt, $tenvt, $dvt, $hinh, $gianhap){
			$this->MaVT = $mavt;
			$this->TenVT = $tenvt;
			$this->Dvt = $dvt;
			$this->Hinh = $hinh;
			$this->GiaNhap = $gianhap;
		}
	}
	//
	$mangVT = array();
	while ($row = mysqli_fetch_assoc($data)) {
		// code...
		array_push($mangVT, new VatTu($row['MAVT'], $row['TENVT'], $row['DVT'], $row['HINH'], $row['GIANHAP']));
	}
	echo json_encode($mangVT)
?>