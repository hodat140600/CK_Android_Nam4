<?php 
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$mapn = $_POST['mapn'];
	$mapk = $_POST['mapk'];
	$ngaylap = $_POST['ngaylap'];

	// $mapn = "PHIEU10";
	// $mapk = "PK03";
	// $ngaylap = "2018-02-09";



	$query = "INSERT INTO phieunhap VALUES ('$mapn','$mapk','$ngaylap')";

	if (mysqli_query($connect, $query)) {
		echo "Success";
	}else{
		echo "Error";
	}
?>