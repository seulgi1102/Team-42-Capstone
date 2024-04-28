<?php
// MySQL 데이터베이스 연결 정보
header("Content-Type: text/html;charset=UTF-8");
//$conn = mysqli_connect("127.0.0.1","데이터베이스 사용자명","데이터베이스 비밀번호","데이터베이스명");
$conn = mysqli_connect("127.0.0.1","root","1234","test");

// 연결 오류 체크
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// 안드로이드 앱으로부터 받은 사용자 이름과 비밀번호
$uemail = $_POST['uemail'];

// 사용자 이름과 비밀번호를 이용하여 데이터베이스에서 검색
// user 부분에 각자 테이블 이름 넣기!
$sql = "SELECT * FROM user WHERE uemail = '$uemail'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // 이미 존재하는 아이디인 경우
    echo "Already Exist";
} else {
    // 존재하지 않는 아이디인 경우
    // 여기에서 회원가입 처리를 진행
    echo "Keep Going";
}

// MySQL 연결 종료
$conn->close();
?>