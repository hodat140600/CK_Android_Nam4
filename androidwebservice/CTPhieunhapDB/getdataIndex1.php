<?php 
	
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');
	 //if(isTheseParametersAvailable(array('MaPK'))){  
   	 	$mapk = $_POST['MaPK'];  
   	 	//$mapk = "PK01";
		$mangCTPNIndex = array();
		
		$query = "SELECT vattu.MAVT, TENVT, DVT, SUM(SOLUONG) AS TONGSL FROM ctphieunhap,vattu,phieunhap WHERE vattu.MAVT = ctphieunhap.MAVT AND ctphieunhap.MAPN = phieunhap.MAPN AND phieunhap.MAPK = '$mapk' GROUP BY vattu.MAVT ";
		
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

			array_push($mangCTPNIndex, new CTPhieuNhapIndex($row['MAVT'], $row['TENVT'], $row['DVT'], $row['TONGSL']));
		}
		echo json_encode($mangCTPNIndex);
?>