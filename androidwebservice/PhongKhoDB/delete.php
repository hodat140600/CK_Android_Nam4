<?php
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$mapk = $_POST['mapk'];

	// $mapk = "PK09";

	$query = "DELETE FROM phongkho WHERE MAPK = '$mapk'";

	if (mysqli_query($connect, $query)) {
		echo "Success";
	}else{
		echo "Error";
	}
?>