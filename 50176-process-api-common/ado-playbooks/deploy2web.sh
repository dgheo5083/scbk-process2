#!/bin/bash 

SSH_KEY="~/.ssh/ado_rsa"
SERVER=wasadmin@10.62.130.132
REMOTE_PATH=/APP/srcupload/ado/50176
JAR_PATH=/APP/srcupload/ado/50176

for arg in "$@"; do
    case "$arg" in
        --artifact=*) artifact="${arg#*=}" ;;
    esac
done
JAR_FILE_NAME="$artifact"

echo "업로드 대상 서버 : $SERVER"	

scp ${JAR_PATH}/${artifact} $SERVER:$REMOTE_PATH

if [ $? -ne 0 ]; then
    echo "신규 패키지 파일($JAR_FILE_NAME) 업로드 실패"
    exit 1
fi

ssh -p 22 -i ${SSH_KEY} $SERVER \
    "${REMOTE_PATH}/deploy_was.sh --artifact=${artifact} --package=process-api --module=common"

if [ $? -ne 0 ]; then
    echo "배포 쉘 실행 실패"
    exit 1
fi