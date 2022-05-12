<?php
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$mavt = $_POST['mavt'];


	// $mavt = "VT10";


	$query = "DELETE FROM vattu WHERE MAVT = '$mavt'";

	if (mysqli_query($connect, $query)) {
		echo "Success";
	}else{
		echo "Error";
	}
?>