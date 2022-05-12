<?php 
	
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');
	 //if(isTheseParametersAvailable(array('MaPK'))){  
   	 	//$mapk = $_POST['MaPK'];  
   	 	//$mapk = "PK01";
		$mangCTPNIndex = array();
		
		$query = "SELECT phieunhap.MAPK ,sum(ctphieunhap.SOLUONG) AS TONGSL FROM ctphieunhap,phongkho,phieunhap WHERE ctphieunhap.MAPN = phieunhap.MAPN AND phieunhap.MAPK = phongkho.MAPK GROUP BY phieunhap.MAPK";
		
		$data = mysqli_query($connect, $query);


		class CTPhieuNhapIndex{
			public $MaPK;
			public $TongSL;
			public function __construct($mapk, $tongsl){
				$this->MaPK = $mapk;
				$this->TongSL = $tongsl;
			}
		}
		while ($row = mysqli_fetch_assoc($data)) {

			array_push($mangCTPNIndex, new CTPhieuNhapIndex($row['MAPK'], $row['TONGSL']));
		}
		echo json_encode($mangCTPNIndex);
?>