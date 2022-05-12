<?php
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$mapk = $_POST['mapk'];
	$tenpk = $_POST['tenpk'];
	$diachi = $_POST['diachi'];
	$sdt = $_POST['sdt'];
	// $mapk = "PK07";
	// $tenpk = "b";
	// $diachi = "b";
	// $sdt = "3";

	$query = "UPDATE phongkho SET MAPK = '$mapk', TENPK= '$tenpk', DIACHI = '$diachi', SDT = '$sdt' WHERE MAPK = '$mapk'";

	if (mysqli_query($connect, $query)) {
		echo "Success";
	}else{
		echo "Error";
	}
?>