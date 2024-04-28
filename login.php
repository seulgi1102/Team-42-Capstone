<?php
// MySQL 데이터베이스 연결 정보
header("Content-Type: text/html;charset=UTF-8");
//$conn = mysqli_connect("127.0.0.1","데이터베이스 사용자명","데이터베이스 비밀번호","데이터베이스명");
$conn = mysqli_connect("127.0.0.1","root","1234","test");

// 연결 오류 체크
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// 안드로이드 앱으로부터 받은 이메일과 비밀번호
$uemail = $_POST['uemail'];
$upw = $_POST['upw'];

// 사용자 이름과 비밀번호를 이용하여 데이터베이스에서 검색
// user 부분에 각자 테이블 이름 넣기!
$sql = "SELECT * FROM user WHERE uemail = '$uemail' AND upw = '$upw'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // 사용자가 인증됨
    echo "Login successful";
} else {
    // 사용자가 인증되지 않음
    echo "Login failed";
}

// MySQL 연결 종료
$conn->close();
?>