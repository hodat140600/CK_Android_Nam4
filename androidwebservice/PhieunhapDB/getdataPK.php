<?php 
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');
	$mapk = $_POST['mapk'];
	$query = "SELECT * FROM phieunhap WHERE MAPK = '$mapk'";
	$data = mysqli_query($connect, $query);

	//
	class PhieuNhap{
		public $MaPN;
		public $MaPK;
		public $NgayLap;
		public function __construct($mapn, $mapk, $ngaylap){
			$this->MaPN = $mapn;
			$this->MaPK = $mapk;
			$this->NgayLap = $ngaylap;
		}
	}
	//
	$mangPN = array();
	while ($row = mysqli_fetch_assoc($data)) {
		// code...
		array_push($mangPN, new PhieuNhap($row['MAPN'], $row['MAPK'], $row['NGAYLAP']));
	}
	echo json_encode($mangPN);
?>