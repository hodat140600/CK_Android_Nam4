<?php 
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$query = "SELECT * FROM ctphieunhap";
	$data = mysqli_query($connect, $query);

	//
	class CTPhieuNhap{
		public $MaPN;
		public $MaVT;
		public $SL;
		public function __construct($mapn, $mavt, $sl){
			$this->MaPN = $mapn;
			$this->MaVT = $mavt;
			$this->SL = $sl;
		}
	}
	//
	$mangCTPN = array();
	while ($row = mysqli_fetch_assoc($data)) {
		// code...
		array_push($mangCTPN, new CTPhieuNhap($row['MAPN'], $row['MAVT'], $row['SOLUONG']));
	}
	echo json_encode($mangCTPN)
?>