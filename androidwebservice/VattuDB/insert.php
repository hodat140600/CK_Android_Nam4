<?php 
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$mavt = $_POST['mavt'];
	$tenvt = $_POST['tenvt'];
	$dvt = $_POST['dvt'];
	$hinh = $_POST['hinh'];
	$gianhap = $_POST['gianhap'];

	// $mavt = "VT10";
	// $tenvt = "DatA";
	// $dvt = "Vien";
	// $hinh = "";
	// $gianhap = "10000";

	$query = "INSERT INTO vattu VALUES ('$mavt','$tenvt','$dvt', '$gianhap','$hinh' )";

	if (mysqli_query($connect, $query)) {
		echo "Success";
	}else{
		echo "Error";
	}
?>