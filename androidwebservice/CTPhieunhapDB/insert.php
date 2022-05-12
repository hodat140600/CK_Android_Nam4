<?php 
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$mapn = $_POST['mapn'];
	$mavt = $_POST['mavt'];
	$soluong = $_POST['soluong'];

	// $mapn = "PHIEU9";
	// $mavt = "VT2";
	// $soluong = "999";



	$query = "INSERT INTO ctphieunhap VALUES ('$mapn','$mavt','$soluong')";

	if (mysqli_query($connect, $query)) {
		echo "Success";
	}else{
		echo "Error";
	}
?>