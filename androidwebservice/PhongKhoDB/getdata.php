<?php 
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$query = "SELECT * FROM phongkho";
	$data = mysqli_query($connect, $query);

	//
	class PhongKho{
		public $MaPK;
		public $TenPK;
		public $DiaChi;
		public $SDT;
		public function __construct($mapk, $tenpk, $diachi, $sdt){
			$this->MaPK = $mapk;
			$this->TenPK = $tenpk;
			$this->DiaChi = $diachi;
			$this->SDT = $sdt;
		}
	}
	//
	$mangPK = array();
	// for ($i = 0; $i < mysqli_num_rows($data); $i++){
 //            array_push($mangPK, mysqli_fetch_assoc($data));
 //        }
	while ($row = mysqli_fetch_assoc($data)) {
		// code...
		array_push($mangPK, new PhongKho($row['MAPK'], $row['TENPK'], $row['DIACHI'], $row['SDT']));
	}
	echo json_encode($mangPK);

?>