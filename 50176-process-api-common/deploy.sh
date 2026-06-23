#!/bin/bash

SKIP_COMMON_BUILD=false

# args 확인
if [[ "$1" == "SKIP_COMMON_BUILD" ]]; then
	SKIP_COMMON_BUILD=true
fi

PACKAGE=common
BASE_DIR="/C/jdk/TOBE/MobileBanking/workspace"
APP_DIR="$BASE_DIR/50176-process-api-$PACKAGE"

SERVER=jbossadm@10.62.131.25
REMOTE_PATH=/APP/mobile-ng/process-api/$PACKAGE
LIB_PATH=$REMOTE_PATH/lib
BIN_PATH=$REMOTE_PATH/bin
JAR_FILE_NAME=process-api-$PACKAGE-0.0.1-SNAPSHOT.jar
JAR_NAME=target/$JAR_FILE_NAME

echo "▪ 현재 패키지 : $PACKAGE"
echo "▪ 기준 디렉터리 : $BASE_DIR"

if [ "$SKIP_COMMON_BUILD" = true ]; then
	echo "1. 공통 라이브러리 업데이트/빌드작업 SKIP"
else 
	echo "1. 공통 라이브러리 업데이트/빌드작업 시작"
	
	cd "$BASE_DIR"
	echo "▪ 현재 디렉터리 : `pwd`"
	REPO_LIST=("50176-process-api-fw-base-pom" "50176-process-api-dependencies" "50176-process-api-edmi-lib" "50176-process-api-config-lib" "50176-process-api-shared-lib")
	#REPO_LIST=("50176-process-api-config-lib")
	
	echo "▪ 1.1 업데이트 시작: $(date)"
	
	echo "----------------------------------------------------"
	
	for repo in "${REPO_LIST[@]}"; do
		TARGET_PATH="$BASE_DIR/$repo"
		
		echo "[작업 중] $repo"
		
		if [ -d "$TARGET_PATH" ]; then
			cd "$TARGET_PATH" || exit
			if git pull; then
				echo "[성공] $repo 업데이트 완료"
			else
				echo "[오류] $repo 업데이트 실패"
				exit 1
			fi
		else
			echo "[경고] 디렉토리를 찾을 수 없습니다: $TARGET_PATH"
		fi
		echo "----------------------------------------------------"
	done

	echo "▪ 1.2 빌드 시작: $(date)"
	
	for repo in "${REPO_LIST[@]}"; do
		TARGET_PATH="$BASE_DIR/$repo"
		
		echo "[작업 중] $repo"
		
		if [ -d "$TARGET_PATH" ]; then
			cd "$TARGET_PATH" || exit
			if mvn clean install -U -DskipTests; then
				echo "[성공] $repo 업데이트 완료"
			else
				echo "[오류] $repo 빌드 실패"
				exit 1
			fi
		else
			echo "[경고] 디렉토리를 찾을 수 없습니다: $TARGET_PATH"
		fi
		echo "----------------------------------------------------"
	done

	echo "공통 패키지 배포/빌드 완료"
fi

echo "2. $PACKAGE 빌드/배포 시작"

cd "$APP_DIR"
echo "▪ 현재 디렉터리 : `pwd`"

echo "▪ 2.1 $PACKAGE 업데이트 시작"
git pull;

if [ $? -ne 0 ]; then
    echo "$PACKAGE 업데이트 실패"
    exit 1
fi

echo "2.2 $PACKAGE 빌드"
mvn clean install -U -DskipTests;

if [ $? -ne 0 ]; then
    echo "$PACKAGE 빌드 실패"
    exit 1
fi

echo "2.3 기존 패키지 파일($JAR_FILE_NAME) 백업"

# ssh $SERVER << EOF
# cd $LIB_PATH
# echo "* 현재 디렉토리 : $BIN_PATH"
# echo "* 파일 : $JAR_FILE_NAME"
# if [ -f "$JAR_FILE_NAME" ]; then
#     mv $JAR_FILE_NAME $JAR_FILE_NAME.$(date +%y%m%d%H%M%S)
#     echo "기존 파일 백업 완료"
# else
#     echo "$JAR_FILE_NAME 파일 존재하지 않음"
# fi
# sleep 1
# EOF

# echo "2.4 신규 패키지 파일($JAR_FILE_NAME) 업로드"

# scp $JAR_NAME $SERVER:$LIB_PATH

# if [ $? -ne 0 ]; then
#     echo "신규 패키지 파일($JAR_FILE_NAME) 업로드 실패"
#     exit 1
# fi

# echo "2.5 $PACKAGE 서버 재기동"

# ssh $SERVER << EOF
# cd $BIN_PATH
# chmod +x stop.sh start.sh
# ./stop.sh
# sleep 3
# ./start.sh
# EOF

echo "3. $PACKAGE 빌드/배포 완료"