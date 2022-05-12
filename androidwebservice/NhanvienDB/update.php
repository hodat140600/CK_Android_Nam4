<?php
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');

	$manv = $_POST['manv'];
	$mapk = $_POST['mapk'];
	$tennv = $_POST['tennv'];
	$ngaysinh = $_POST['ngaysinh'];
	$email = $_POST['email'];

	$query = "UPDATE nhanvien SET MANV = '$manv', MAPK = '$mapk', TENNV= '$tennv', NGAYSINH = '$ngaysinh', EMAIL = '$email' WHERE MANV = '$manv'";

	if (mysqli_query($connect, $query)) {
		echo "Success";
	}else{
		echo "Error";
	}
?>