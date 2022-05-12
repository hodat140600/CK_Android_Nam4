<?php
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$mapn = $_POST['mapn'];
	$mavt = $_POST['mavt'];
	$soluong = $_POST['soluong'];

	// $mapn = "PHIEU9";
	// $mavt = "VT2";
	// $soluong = "7777";

	$query = "UPDATE ctphieunhap SET MAPN = '$mapn', MAVT = '$mavt', SOLUONG = '$soluong' WHERE MAPN = '$mapn' AND MAVT = '$mavt'";

	if (mysqli_query($connect, $query)) {
		echo "Success";
	}else{
		echo "Error";
	}
?>