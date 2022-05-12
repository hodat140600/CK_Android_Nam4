<?php
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$mapn = $_POST['mapn'];


	// $mapn = "PHIEU10";


	$query = "DELETE FROM phieunhap WHERE MAPN = '$mapn'";

	if (mysqli_query($connect, $query)) {
		echo "Success";
	}else{
		echo "Error";
	}
?>