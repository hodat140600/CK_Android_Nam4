<?php 
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$query = "SELECT ctphieunhap.MAPN, ctphieunhap.MAVT, vattu.TENVT, vattu.DVT, ctphieunhap.SOLUONG FROM ctphieunhap, vattu WHERE ctphieunhap.MAVT = vattu.MAVT";
	$data = mysqli_query($connect, $query);

	//
	class CTPhieuNhapAll{
		public $MaPN;
		public $MaVT;
		public $TenVT;
		public $DVT;
		public $SL;
		public function __construct($mapn, $mavt, $tenvt, $dvt, $sl){
			$this->MaPN = $mapn;
			$this->MaVT = $mavt;
			$this->TenVT = $tenvt;
			$this->DVT = $dvt;
			$this->SL = $sl;
		}
	}
	//
	$mangCTPN = array();
	while ($row = mysqli_fetch_assoc($data)) {
		// code...
		array_push($mangCTPN, new CTPhieuNhapAll($row['MAPN'], $row['MAVT'], $row['TENVT'], $row['DVT'],$row['SOLUONG']));
	}
	echo json_encode($mangCTPN);
?>