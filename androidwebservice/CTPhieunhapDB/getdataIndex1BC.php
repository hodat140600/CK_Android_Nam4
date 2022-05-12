<?php 
	
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');
	 //if(isTheseParametersAvailable(array('MaPK'))){  
   	 	$mapk = $_POST['MaPK'];
   	 	$mapn = $_POST['MaPN']; 
   	 	//$mapk = "PK01";
		$mangCTPNIndex = array();
		
		$query = "SELECT vattu.MAVT, TENVT, DVT, SUM(SOLUONG) AS TONGSL, (vattu.GIANHAP * SUM(ctphieunhap.SOLUONG)) AS TRIGIA FROM ctphieunhap,vattu,phieunhap WHERE vattu.MAVT = ctphieunhap.MAVT AND ctphieunhap.MAPN = phieunhap.MAPN AND phieunhap.MAPK = '$mapk' AND phieunhap.MAPN = '$mapn' GROUP BY vattu.MAVT ";
		
		$data = mysqli_query($connect, $query);


		class CTPhieuNhapIndexBC{
			public $MaVT;
			public $TenVT;
			public $DVT;
			public $TongSL;
			public $TriGia;
			public function __construct($mavt, $tenvt, $dvt, $tongsl, $trigia){
				$this->MaVT = $mavt;
				$this->TenVT = $tenvt;
				$this->DVT = $dvt;
				$this->TongSL = $tongsl;
				$this->TriGia = $trigia;
			}
		}
		while ($row = mysqli_fetch_assoc($data)) {

			array_push($mangCTPNIndex, new CTPhieuNhapIndexBC($row['MAVT'], $row['TENVT'], $row['DVT'], $row['TONGSL'], $row['TRIGIA']));
		}
		echo json_encode($mangCTPNIndex);
?>