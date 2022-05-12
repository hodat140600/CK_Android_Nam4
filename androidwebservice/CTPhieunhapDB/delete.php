<?php
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$mapn = $_POST['mapn'];
	$mavt = $_POST['mavt'];

	// $mapn = "PHIEU9";
	// $mavt = "VT2";

	$query = "DELETE FROM ctphieunhap WHERE MAPN = '$mapn' AND MAVT = '$mavt'";

	if (mysqli_query($connect, $query)) {
		echo "Success";
	}else{
		echo "Error";
	}
?>