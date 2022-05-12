<?php
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$mavt = $_POST['mavt'];
	$tenvt = $_POST['tenvt'];
	$dvt = $_POST['dvt'];
	$hinh = $_POST['hinh'];
	$gianhap = $_POST['gianhap'];

	// $mavt = "VT9";
	// $tenvt = "DatB";
	// $dvt = "CUC";
	// $hinh = "";
	// $gianhap = "10000";

	$query = "UPDATE VATTU SET MAVT = '$mavt', TENVT = '$tenvt', DVT = '$dvt', HINH = '$hinh', GIANHAP ='$gianhap' WHERE MAVT = '$mavt'";

	if (mysqli_query($connect, $query)) {
		echo "Success";
	}else{
		echo "Error";
	}
?>