<?php
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$manv = $_POST['manv'];
	$query = "DELETE FROM nhanvien WHERE MANV = '$manv'";

	if (mysqli_query($connect, $query)) {
		echo "Success";
	}else{
		echo "Error";
	}
?>