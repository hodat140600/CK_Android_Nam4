<?php 
	$mapk = $_POST['MaPK'];
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$mangCTPNIndex = array();
		//
		//$mapk = "PK01";

		$query = "SELECT vattu.MAVT, TENVT, DVT, SUM(SOLUONG) AS TONGSL FROM ctphieunhap,vattu,phieunhap WHERE vattu.MAVT = ctphieunhap.MAVT AND ctphieunhap.MAPN =phieunhap.MAPN AND phieunhap.MAPK = '$mapk' GROUP BY vattu.MAVT ";
		$data = mysqli_query($connect, $query);

		class CTPhieuNhapIndex{
			public $MaVT;
			public $TenVT;
			public $DVT;
			public $TongSL;
			public function __construct($mavt, $tenvt, $dvt, $tongsl){
				$this->MaVT = $mavt;
				$this->TenVT = $tenvt;
				$this->DVT = $dvt;
				$this->TongSL = $tongsl;
			}
		}
			while ($row = mysqli_fetch_assoc($data)) {
				// $row = str_replace("<BR>", "", $row); // this is what I changed
	   //  		// Remove closing BR just in case they did close it
	   //  		$row = str_replace("</BR>", "", $row);
				array_push($mangCTPNIndex, new CTPhieuNhapIndex($row['MAVT'], $row['TENVT'], $row['DVT'], $row['TONGSL']));
			}
		// header('Content-Type: application/json; charset=utf-8');
		// $json = json_encode($mangCTPNIndex);
		// $json = str_replace("<br>", "", $json); // this is what I changed
		// $json = str_replace("<br ", "", $json);
	 //    		// Remove closing BR just in case they did close it
	 //    $json = str_replace("</br>", "", $json);
		echo json_encode($mangCTPNIndex);
?>