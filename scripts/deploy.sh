#!/bin/bash

echo "배포 시작..."

# 1. 최신 코드 가져오기
echo "최신 코드 가져오는 중..."
cd /home/Campusflow  # 저장소 경로
git pull origin master  # 최신 코드 가져오기

# 2. 빌드
echo "애플리케이션 빌드 중..."
./gradlew bootJar -x test  # 테스트 제외 빌드

# 3. 실행 중인 애플리케이션 종료 후 재시작
echo "서버에서 애플리케이션 재시작 중..."
pkill -f app.jar || true  # 기존 실행 중인 프로세스 종료
sudo nohup java -jar build/libs/app.jar > output.log 2>&1 &  # 애플리케이션 실행

echo "배포 완료!"