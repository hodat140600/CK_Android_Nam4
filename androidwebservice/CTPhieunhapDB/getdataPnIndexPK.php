<?php 
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');
	// $mapk = "PK01";
	// $mavt = "VT1";
	$mavt = $_POST['MaVT'];
	$mapk = $_POST['MaPK'];
	$query = "SELECT ctphieunhap.MAPN, phieunhap.NGAYLAP
                FROM ctphieunhap, phieunhap
                WHERE phieunhap.MAPN = ctphieunhap.MAPN
                AND ctphieunhap.MAVT = '$mavt' AND phieunhap.MAPK = '$mapk'";
	$data = mysqli_query($connect, $query);

	//
	class CTPhieuNhap{
		public $MaPN;
		public $NgayLap;
		public function __construct($mapn, $ngaylap){
			$this->MaPN = $mapn;
			$this->NgayLap = $ngaylap;
		}
	}
	//
	$mangCTPN = array();
	while ($row = mysqli_fetch_assoc($data)) {
		// code...
		array_push($mangCTPN, new CTPhieuNhap($row['MAPN'], $row['NGAYLAP']));
	}
	echo json_encode($mangCTPN);
?>