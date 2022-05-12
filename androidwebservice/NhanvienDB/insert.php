<?php 
	require($_SERVER['DOCUMENT_ROOT'].'/androidwebservice/dbCon.php');
	$manv = $_POST['manv'];
	$mapk = $_POST['mapk'];
	$tennv = $_POST['tennv'];
	$ngaysinh = $_POST['ngaysinh'];
	$email = $_POST['email'];

	// $manv = "NV90";
	// $mapk = "PK01";
	// $tennv = "DAT";
	// $ngaysinh = "2000-06-06";
	// $email = "hodat@gmail.com";

	$query = "INSERT INTO nhanvien VALUES ('$manv','$mapk','$tennv','$ngaysinh', '$email')";

	if (mysqli_query($connect, $query)) {
		echo "Success";
	}else{
		echo "Error";
	}
?>