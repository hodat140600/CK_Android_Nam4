<?php
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$mapn = $_POST['mapn'];
	$mapk = $_POST['mapk'];
	$ngaylap = $_POST['ngaylap'];

	// $mapn = "PHIEU10";
	// $mapk = "PK06";
	// $ngaylap = "2018-06-10";

	$query = "UPDATE PHIEUNHAP SET MAPN = '$mapn', MAPK = '$mapk', NGAYLAP = '$ngaylap' WHERE MAPN = '$mapn'";

	if (mysqli_query($connect, $query)) {
		echo "Success";
	}else{
		echo "Error";
	}
?>