<?php 
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$mapk = $_POST['mapk'];
	$tenpk = $_POST['tenpk'];
	$diachi = $_POST['diachi'];
	$sdt = $_POST['sdt'];

	$query = "INSERT INTO phongkho VALUES ('$mapk','$tenpk','$diachi','$sdt')";

	if (mysqli_query($connect, $query)) {
		echo "Success";
	}else{
		echo "Error";
	}
?>